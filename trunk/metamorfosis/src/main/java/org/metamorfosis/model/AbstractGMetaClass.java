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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Sigue cuatro principios básicos.
 *
 * 1. Se le pueden inyectar propiedades a la clase
 * 2. Se le pueden inyectar propiedades a los fields
 * 
 * 3. Tiene un objeto metaClass el cual contiene una lista de propiedades
 *    originales e inyectadas.
 * 4. Las propiedades originales e inyectadas serán accesibles por medio de
 *    instance.get('propertyName')
 *
 * @author iberck
 */
public abstract class AbstractGMetaClass extends LazyDynaBean implements MetaClass {

    private static final Log log = LogFactory.getLog(AbstractGMetaClass.class);
    private String className;
    private MetaClassObject metaClass;
    private Object source;

    protected AbstractGMetaClass(Object instance) {
        this.source = instance;
    }

    protected AbstractGMetaClass(String className) {
        this.className = className;
    }

    public MetaClassObject getMetaClass() {
        return metaClass;
    }

    private static Object instantiateClass(String className) throws MetaClassException {
        try {
            return Class.forName(className).newInstance();
        } catch (Exception ex) {
            throw new MetaClassException("Error al instanciar la clase '" + className + "'", ex);
        }
    }

    /**
     * Borra dinamicamente la propiedad
     * @param name Property name
     */
    private void removeProperty(String propertyName) {
        ((LazyDynaClass) getDynaClass()).remove(propertyName);
    }

    private void replaceProperty(String propertyName, Object newPropertyValue) {
        try {
            removeProperty(propertyName);
            PropertyUtils.setNestedProperty(this, propertyName, newPropertyValue);
        } catch (Exception ex) {
        }
    }

    protected void copySourceProperties() {

        if (className != null) {
            source = instantiateClass(className);
        }

        try {
            // copia todas las propiedades del objeto original
            // y las pone al servicio con get('originalPropertyName')
            PropertyUtils.copyProperties(this, source);
        } catch (Exception ex) {
            throw new MetaClassException("Error al crear el metapojo", ex);
        }
    }

    @Override
    public void injectClassProperty(String propName, Object propValue) throws MetaClassException {
        try {
            // Pone la propiedad al servicio con get('injectedPropertyName')
            PropertyUtils.setNestedProperty(this, propName, propValue);
        } catch (Exception ex) {
            throw new MetaClassException("Error al inyectar la propiedad '[" + propName + ", " + propValue + "']", ex);
        }
    }

    @Override
    public void injectFieldProperty(String fieldName, String propertyName, Object propertyValue) {
        try {
            // 1.validar que exista el field y obtenerlo
            Object fieldObj = PropertyUtils.getNestedProperty(this, fieldName);

            // 2. MetaClass
            // datos del objeto original (copySource)
            GMetaClass metaField = new GMetaClass(fieldObj);
            Map<String, Object> metaFieldClassProperties = new HashMap();

            // datos inyectados (propertyName:propertyValue)
            metaFieldClassProperties.put(propertyName, propertyValue);
            metaField.setInjectedClassProperties(metaFieldClassProperties);

            // datos del metaClassProperty (name, type)
            HashMap propertiesHash = getMetaClass().getPropertiesHash();
            MetaClassProperty metaClassProperty = new MetaClassProperty();
            metaClassProperty.setName(fieldName);
            metaClassProperty.setType(this.getClass());
            PropertyUtils.copyProperties(metaField, metaClassProperty);

            metaField.initialize();

            // 3. reemplazar propiedad original con el metaField
            replaceProperty(fieldName, metaField); // accesible por medio de get
            propertiesHash.put(fieldName, metaField); // accesible por medio de metaclass.properties

        } catch (Exception ex) {
            throw new MetaClassException("No existe el field '" + fieldName + "' " +
                    "dentro de la clase '" + source + "'", ex);
        }
    }

    /**
     * Pone las propiedades existentes dentro del objeto
     * 
     * metaClass.properties
     */
    protected void createMetaClass() {
        this.metaClass = new MetaClassObject();

        HashMap propertiesHash = new HashMap();

        // obtener las propiedades inyectadas y meterlas en un hash
        DynaProperty[] dynaProperties = getDynaClass().getDynaProperties();
        for (DynaProperty dynaProperty : dynaProperties) {
            if (!dynaProperty.getName().equals("class")) {
                MetaClassProperty mcProperty = new MetaClassProperty();
                mcProperty.setName(dynaProperty.getName());
                mcProperty.setType(dynaProperty.getType());

                propertiesHash.put(mcProperty.getName(), mcProperty);
            }
        }

        // obtener las propiedades del source, si existiera en el hash se
        // reemplazaría para tener el type correcto ya que los dynaProperties
        // no tienen el type original.
        Field[] declaredFields = source.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            MetaClassProperty mcProperty = new MetaClassProperty();
            mcProperty.setName(field.getName());
            mcProperty.setType(field.getType());

            propertiesHash.put(mcProperty.getName(), mcProperty);
        }

        // crear el objeto metaClass
        this.metaClass.setPropertiesHash(propertiesHash);
    }

    public class MetaClassObject {

        private HashMap propertiesHash;

        public Collection getProperties() {
            return propertiesHash.values();
        }

        public HashMap getPropertiesHash() {
            return propertiesHash;
        }

        public void setPropertiesHash(HashMap propertiesHash) {
            this.propertiesHash = propertiesHash;
        }
    }

    public class MetaClassProperty {

        private String name;
        private Class type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class getType() {
            return type;
        }

        public void setType(Class type) {
            this.type = type;
        }
    }

    @Override
    public String toString() {
        return source != null ? source.toString() : super.toString();
    }
}
