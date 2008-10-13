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
import java.util.Map;

/**
 *
 * @author iberck
 */
public class GMetaPojo implements MetaPojo {

    @Override
    public void injectPojoProperty(String propertyName, Object propertyValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void injectFieldProperty(String fieldName, String propertyName, Object propertyValue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object createInjectedObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInjectedPropertiesMap(Map<String, Object> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInjectedFields(List<FieldProperty> injectedFields) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
