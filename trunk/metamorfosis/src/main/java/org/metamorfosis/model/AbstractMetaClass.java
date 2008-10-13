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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.LazyDynaClass;
import org.apache.commons.beanutils.PropertyUtils;

/**
 *
 * @author iberck
 */
public abstract class AbstractMetaClass extends LazyDynaBean {

    private Object original;
    private MetaClass metaClass;

    public AbstractMetaClass(Object source) {
        this.original = source;
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

    public Object getOriginal() {
        return original;
    }

    protected void copyOriginalProperties() {
        try {
            // copia todas las propiedades y las pone al servicio con get('propertyName')
            PropertyUtils.copyProperties(this, original);
        } catch (Exception ex) {
            Logger.getLogger(AbstractMetaClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void injectProperty(String propName, Object propValue) throws MetaPojoException {
        try {
            // Pone la propiedad al servicio con get('propName')
            PropertyUtils.setNestedProperty(this, propName, propValue);
        } catch (Exception ex) {
            throw new MetaPojoException("Error al inyectar la propiedad '[" + propName + ", " + propValue + "']", ex);
        }
    }

    public void injectFieldProperty(String fieldName, String propertyName, Object propertyValue) {
        Object fieldObj = null;
        try {
            // 1.validar que exista el field y obtenerlo
            fieldObj = PropertyUtils.getNestedProperty(this, fieldName);
        } catch (Exception ex) {
        }
    }

    protected void replaceProperty(String propertyName, Object propertyValue) {
        try {
            removeProperty(propertyName);
            PropertyUtils.setNestedProperty(this, propertyName, propertyValue);
        } catch (Exception ex) {
            Logger.getLogger(AbstractMetaClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Borra dinamicamente la propiedad
     * @param name Property name
     */
    protected void removeProperty(String propertyName) {
        ((LazyDynaClass) getDynaClass()).remove(propertyName);
    }

    public class MetaClass {

        private List<MetaClassProperty> properties;

        public List<MetaClassProperty> getProperties() {
            return properties;
        }

        public void setProperties(List<MetaClassProperty> properties) {
            this.properties = properties;
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

    // lazy init
    public abstract void initialize();
}
