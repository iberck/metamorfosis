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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Lazy
 * @author iberck
 */
public class GMetaClass extends AbstractMetaClass {

    private Map<String, Object> injectedClassProperties;
    private Collection<FieldProperty> injectedFieldProperties;

    public GMetaClass(String className) {
        super(className);
    }

    public GMetaClass(Object instance) {
        super(instance);
    }

    public void setInjectedClassProperties(Map<String, Object> injectedClassProperties) {
        this.injectedClassProperties = injectedClassProperties;
    }

    public void setInjectedFieldProperties(Collection<FieldProperty> injectedFieldProperties) {
        this.injectedFieldProperties = injectedFieldProperties;
    }

    @Override
    public void initialize() {
        super.copySourceProperties();

        // inject class properties
        if (injectedClassProperties != null) {
            Set<Entry<String, Object>> entrySet = injectedClassProperties.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                String propName = entry.getKey();
                Object propValue = entry.getValue();
                super.injectClassProperty(propName, propValue);
            }
        }

        super.createMetaClass();
        
        // inject field properties
        if (injectedFieldProperties != null) {
            for (FieldProperty injectedField : injectedFieldProperties) {
                super.injectFieldProperty(injectedField.getFieldName(),
                        injectedField.getPropertyName(),
                        injectedField.getPropertyValue());
            }
        }
    }
}
