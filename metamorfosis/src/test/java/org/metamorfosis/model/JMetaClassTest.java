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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.model.AbstractJMetaClass.MetaClassObject;
import org.metamorfosis.model.AbstractJMetaClass.MetaClassProperty;

/**
 *
 * @author iberck
 */
public class JMetaClassTest extends TestCase {

    private static final Log log = LogFactory.getLog(JMetaClassTest.class);

    public void testCreate() throws Exception {
        JMetaClass metaClass = new JMetaClass(SimpleBean.class.getName());
        metaClass.initialize();

        MetaClassObject _metaClass = metaClass.getMetaClass();
        Collection<MetaClassProperty> properties = _metaClass.getProperties();
        for (MetaClassProperty metaClassProperty : properties) {
            String name = metaClassProperty.getName();
            Class type = metaClassProperty.getType();
            assertEquals("propertyOne", name);
            assertEquals("java.lang.String", type.getName());
        }
    }

    public void testInjectPropertyClass() throws Exception {
        JMetaClass metaClass = new JMetaClass(SimpleBean.class.getName());

        Map injectedProperties = new HashMap();
        injectedProperties.put("myproperty", "value_");
        metaClass.setInjectedClassProperties(injectedProperties);

        metaClass.initialize();

        /*MetaClassObject _metaClass = metaClass.getMetaClass();
        MetaClassProperty[] properties = _metaClass.getProperties().toArray(new MetaClassProperty[0]);
        assertEquals("myproperty", properties[0].getName());
        assertEquals("java.lang.String", properties[0].getType().getName());

        assertEquals("propertyOne", properties[1].getName());
        assertEquals("java.lang.String", properties[1].getType().getName());
         */ 
    }

    public void testMetaClassValues() throws Exception {
        SimpleBean simpleBean = new SimpleBean();
        simpleBean.setPropertyOne("this is a property one");
        JMetaClass metaClass = new JMetaClass(simpleBean);

        Map injectedProperties = new HashMap();
        injectedProperties.put("myproperty", "value_");
        metaClass.setInjectedClassProperties(injectedProperties);

        metaClass.initialize();

        Object nestedProperty = PropertyUtils.getNestedProperty(metaClass, "propertyOne");
        assertEquals("java.lang.String", nestedProperty.getClass().getName());
        assertEquals("this is a property one", nestedProperty);
        nestedProperty = PropertyUtils.getNestedProperty(metaClass, "myproperty");
        assertEquals("value_", nestedProperty);
        assertEquals("java.lang.String", nestedProperty.getClass().getName());
    }

    public void testInjectFieldPropertyClass() throws Exception {
        JMetaClass metaClass = new JMetaClass(SimpleBean.class.getName());
        // inyectar propiedades a nivel clase
        Map injectedClassProperties = new HashMap();
        injectedClassProperties.put("myproperty", "value_");
        metaClass.setInjectedClassProperties(injectedClassProperties);
        
        Collection<FieldProperty> fieldProperties = new ArrayList();
        FieldProperty fieldProperty = new FieldProperty();
        fieldProperty.setFieldName("propertyOne");
        fieldProperty.setPropertyName("newProperty");
        fieldProperty.setPropertyValue("newPropertyValue");
        fieldProperties.add(fieldProperty);

        fieldProperty = new FieldProperty();
        fieldProperty.setFieldName("myproperty");
        fieldProperty.setPropertyName("primaryKey");
        fieldProperty.setPropertyValue("true");
        fieldProperties.add(fieldProperty);
        
        metaClass.setInjectedFieldProperties(fieldProperties);
        // initialize
        metaClass.initialize();

        // test inyectar un field original
        Object propertyOne = PropertyUtils.getNestedProperty(metaClass, "propertyOne");
        assertEquals("JMetaClass", propertyOne.getClass().getSimpleName());
        
        Object newProperty = PropertyUtils.getNestedProperty(propertyOne, "newProperty");
        assertEquals("newPropertyValue", newProperty);
        
        newProperty = PropertyUtils.getNestedProperty(metaClass, "propertyOne.newProperty");
        assertEquals("newPropertyValue", newProperty);

        // test inyectar un field inyectado
        JMetaClass injectedField = (JMetaClass) PropertyUtils.getNestedProperty(metaClass, "myproperty");
        assertEquals("value_", injectedField.toString());
        
        Object primaryKey = PropertyUtils.getNestedProperty(injectedField, "primaryKey");
        assertEquals("true", primaryKey.toString());
        
        primaryKey = PropertyUtils.getNestedProperty(metaClass, "myproperty.primaryKey");
        assertEquals("true", primaryKey.toString());
        
        // test metaclass
        /*MetaClassProperty[] properties = metaClass.getMetaClass().getProperties().toArray(new MetaClassProperty[0]);
        assertEquals("myproperty", properties[0].getName());
        assertEquals("JMetaClass", properties[0].getType().getSimpleName());
        assertEquals("propertyOne", properties[1].getName());
        assertEquals("java.lang.String", properties[1].getType().getName());
         */ 
    }
}
