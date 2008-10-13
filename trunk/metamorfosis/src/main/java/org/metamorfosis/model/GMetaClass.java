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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author iberck
 */
public class GMetaClass extends AbstractMetaClass {

    private Map<String, Object> properties = new HashMap();
    private List<InjectedField> fields = new ArrayList();

    public GMetaClass(Object original) {
        super(original);
    }

    public void addProperty(String propName, Object propValue) {
        properties.put(propName, propValue);
    }

    public void addField(String fieldName, String propertyName, Object propertyValue) {
        InjectedField field = new InjectedField();
        field.setFieldName(fieldName);
        field.setInjectedPropertyName(propertyName);
        field.setInjectedPropertyValue(propertyValue);

        fields.add(field);
    }

    @Override
    public void initialize() {
        // copy original properties
        super.copyOriginalProperties();

        // inject properties
        Set<Entry<String, Object>> entrySet = properties.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            String propName = entry.getKey();
            Object propValue = entry.getValue();
            injectProperty(propName, propValue);
        }

        // inject field properties
        for (InjectedField injectedField : fields) {
            injectFieldProperty(injectedField.getFieldName(),
                    injectedField.getInjectedPropertyName(),
                    injectedField.getInjectedPropertyValue());
        }
    }
}
