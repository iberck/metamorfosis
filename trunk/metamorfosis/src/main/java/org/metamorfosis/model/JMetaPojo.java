/**
 *  Copyright (c) 2007-2008 by Carlos Gómez Montiel <iberck@gmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  his program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.metamorfosis.model;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author iberck
 */
public class JMetaPojo extends LazyDynaBean implements MetaPojo {

    private static final Log log = LogFactory.getLog(JMetaPojo.class);
    private Object sourceObject;
    private Map<String, JMetaPojo> declaredFields = new HashMap<String, JMetaPojo>();

    public JMetaPojo(String className) {
        this(instantiateClass(className));
    }

    /**
     * Crea una JMetaPojo en base a un objeto
     * @param object
     * @throws MetaPojoException
     */
    protected JMetaPojo(Object instance) throws MetaPojoException {
        this.sourceObject = instance;

        try {
            // copia todas las propiedades y las pone al servicio con get('propertyName')
            PropertyUtils.copyProperties(this, instance);
        } catch (Exception ex) {
            throw new MetaPojoException("Error al crear el metapojo", ex);
        }
    }

    public Object getSourceObject() {
        return sourceObject;
    }

    protected static Object instantiateClass(String className) throws MetaPojoException {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new MetaPojoException("Error al instanciar la clase '" + className + "'", ex);
        }
    }

    /**
     * Borra dinamicamente la propiedad
     * @param name Property name
     */
    protected void removeProperty(String propertyName) {
        ((LazyDynaClass) getDynaClass()).remove(propertyName);
    }

    /**
     * Obtiene todas las propiedades menos la propiedad class
     * @return
     */
    protected Object[] getDynaProperties() {
        // quitar objeto class de las propiedades
        DynaProperty[] dynaProps = getDynaClass().getDynaProperties();
        Object[] dynaPropsWithoutClass = new Object[dynaProps.length - 1];

        int i = 0;
        for (DynaProperty dp : dynaProps) {
            if (!dp.getName().equals("class")) {
                dynaPropsWithoutClass[i++] = dp;
            }
        }

        return dynaPropsWithoutClass;
    }

    @Override
    public void injectPojoProperty(String propName, Object propValue) throws MetaPojoException {
        try {

            // Pone la propiedad al servicio con get('propName')
            PropertyUtils.setNestedProperty(this, propName, propValue);
            PropertyUtils.setNestedProperty(this, "name", propName);
            PropertyUtils.setNestedProperty(this, "type", propValue.getClass());

            declaredFields.put(propName, this);
        } catch (Exception ex) {
            throw new MetaPojoException("Error al inyectar la propiedad '[" + propName + ", " + propValue + "']", ex);
        }
    }

    @Override
    public void injectFieldProperty(String fieldName, String propertyName, Object propertyValue) {
        Object fieldObj = null;
        try {
            // 1.validar que exista el field y obtenerlo
            fieldObj = PropertyUtils.getNestedProperty(this, fieldName);

            // 2. Metapojo a partir del objeto field
            JMetaPojo metaField = new JMetaPojo(fieldObj);

            // 3. inyectar propiedad al field(la hace accesible por medio de get() y la pone en sus dynaProperties)
            PropertyUtils.setNestedProperty(metaField, propertyName, propertyValue);
            //metaField.createInjectedObject();

            // 4. borrar el field viejo del pojo, TODO: Obtener sus annotations
            this.removeProperty(fieldName);

            // 5. inyectar nuevo field al pojo (la hace accesible por medio de get() y la pone en sus dynaProperties)
            //this.injectPojoProperty(fieldName, metaField);
            PropertyUtils.setNestedProperty(this, fieldName, metaField);

            // TODO: crear un nuevo declared Field para no aplastar posibles name y type originales
            PropertyUtils.setNestedProperty(metaField, "name", fieldName);
            PropertyUtils.setNestedProperty(metaField, "type", fieldObj.getClass());
            declaredFields.put(fieldName, metaField);

        } catch (Exception ex) {
            throw new MetaPojoException("No existe el field '" + fieldName + "' " +
                    "dentro de la clase '" + sourceObject + "'", ex);
        }
    }

    @Override
    public Object createInjectedObject() throws MetaPojoException {
        try {
            // se crea a partir del objeto original para preservar el nombre de clase y otros atributos,
            // lo único que cambiara son sus declaredFields
            JMetaPojo clazz = new JMetaPojo(sourceObject.getClass());

            // crear los declaredFields
            Field[] oldDeclaredFields = sourceObject.getClass().getDeclaredFields();

            for (Field f : oldDeclaredFields) {
                if (!declaredFields.containsKey(f.getName())) {
                    declaredFields.put(f.getName(), new JMetaPojo(f));
                }
            }

            // Modificar los declaredFields del objeto original
            clazz.removeProperty("declaredFields");
            PropertyUtils.setNestedProperty(clazz, "declaredFields", declaredFields.values().toArray());

            // reemplazar el objeto class del MetaPojo
            this.removeProperty("class");
            PropertyUtils.setNestedProperty(this, "class", clazz);

        } catch (Exception ex) {
            throw new MetaPojoException("Error al crear el objeto inyectado", ex);
        }

        return this;
    }

    @Override
    public void setInjectedPropertiesMap(Map<String, Object> m) {
        Set<Entry<String, Object>> entrySet = m.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            injectPojoProperty(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void setInjectedFields(List<InjectedField> injectedFields) {
        for (InjectedField injectedField : injectedFields) {
            injectFieldProperty(injectedField.getFieldName(),
                    injectedField.getInjectedPropertyName(),
                    injectedField.getInjectedPropertyValue());
        }
    }
}
