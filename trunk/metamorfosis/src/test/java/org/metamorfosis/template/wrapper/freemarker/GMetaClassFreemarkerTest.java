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
package org.metamorfosis.template.wrapper.freemarker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.metamorfosis.model.FieldProperty;
import org.metamorfosis.model.GMetaClass;
import org.metamorfosis.model.SimpleBean;
import org.metamorfosis.template.freemarker.AbstractFreemarkerTestCase;

/**
 *
 * @author iberck
 */
public class GMetaClassFreemarkerTest extends AbstractFreemarkerTestCase {

    public void testCreate() throws Exception {
        // from object
        SimpleBean simpleBean = new SimpleBean();
        simpleBean.setPropertyOne("propertyOneValue");
        GMetaClass metaClass = new GMetaClass(simpleBean);
        metaClass.initialize();

        Map root = new HashMap();
        root.put("bean", metaClass);

        StringBuilder template = new StringBuilder();
        template.append("${bean.propertyOne.class.name}");

        assertEqualsFreemarkerTemplate(root, "java.lang.String", template.toString());

        // from className
        metaClass = new GMetaClass(SimpleBean.class.getName());
        metaClass.initialize();

        root = new HashMap();
        root.put("bean", metaClass);

        template = new StringBuilder();
        template.append("${bean.propertyOne.class.name}");

        // Object por que inicialmente no tenia un valor, si se obtiene de metaClass.properties
        // dara java.lang.String
        assertEqualsFreemarkerTemplate(root, "java.lang.Object", template.toString());
    }

    public void testMetaClass() throws Exception {
        // from className
        GMetaClass metaClass = new GMetaClass(SimpleBean.class.getName());
        metaClass.initialize();

        Map root = new HashMap();
        root.put("bean", metaClass);

        // MetaClass reflected fields
        StringBuilder template = new StringBuilder();
        template.append("<#list bean.class.declaredFields as field>");
        template.append("${field.name},");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "injectedClassProperties,injectedFieldProperties,", template.toString());

        // Metaclass
        template = new StringBuilder();
        template.append("${bean.metaClass.class.simpleName}");
        assertEqualsFreemarkerTemplate(root, "MetaClassObject", template.toString());

        // Metaclass properties
        template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("${property.name}, ${property.type.name}");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "propertyOne, java.lang.String", template.toString());
    }

    public void testInjectionMetaClass() throws Exception {
        // from className
        GMetaClass metaClass = new GMetaClass(SimpleBean.class.getName());
        Map injectedProperties = new HashMap();
        injectedProperties.put("myproperty", "value_");
        injectedProperties.put("myproperty2", "value_2");
        injectedProperties.put("bean2", new Bean2());
        metaClass.setInjectedClassProperties(injectedProperties);

        // inject fields
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

        fieldProperty = new FieldProperty();
        fieldProperty.setFieldName("bean2");
        fieldProperty.setPropertyName("inView");
        fieldProperty.setPropertyValue("yes");
        fieldProperties.add(fieldProperty);
        metaClass.setInjectedFieldProperties(fieldProperties);

        fieldProperty = new FieldProperty();
        fieldProperty.setFieldName("bean2");
        fieldProperty.setPropertyName("stateless");
        fieldProperty.setPropertyValue("not");
        fieldProperties.add(fieldProperty);
        metaClass.setInjectedFieldProperties(fieldProperties);

        metaClass.initialize();

        Map root = new HashMap();
        root.put("bean", metaClass);

        // Metaclass properties
        StringBuilder template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("${property.name},");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "bean2,myproperty,myproperty2,propertyOne,", template.toString());

        // injected properties
        template = new StringBuilder();
        template.append("${bean.myproperty}");
        assertEqualsFreemarkerTemplate(root, "value_", template.toString());

        // injected properties 2
        template = new StringBuilder();
        template.append("${bean.myproperty2}");
        assertEqualsFreemarkerTemplate(root, "value_2", template.toString());

        template = new StringBuilder();
        template.append("${bean.propertyOne.newProperty}");
        assertEqualsFreemarkerTemplate(root, "newPropertyValue", template.toString());

        template = new StringBuilder();
        template.append("${bean.myproperty.primaryKey}");
        assertEqualsFreemarkerTemplate(root, "true", template.toString());

        template = new StringBuilder();
        template.append("${bean.myproperty.type.simpleName}");//injecto tambien las
        // propiedades name y type
        assertEqualsFreemarkerTemplate(root, "GMetaClass", template.toString());

        template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("<#if property.newProperty??>");
        template.append("${property.name},${property.newProperty}");
        template.append("</#if>");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "propertyOne,newPropertyValue", template.toString());

        template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("<#if property.primaryKey??>");
        template.append("${property.name},${property.primaryKey}");
        template.append("</#if>");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "myproperty,true", template.toString());

        // object injection
        template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("<#if property.inView??>");
        template.append("${property.name},${property.inView}");
        template.append("</#if>");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "bean2,yes", template.toString());
        
        // double field injection
        template = new StringBuilder();
        template.append("<#list bean.metaClass.properties as property>");
        template.append("<#if property.stateless??>");
        template.append("${property.name},${property.stateless}");
        template.append("</#if>");
        template.append("</#list>");
        assertEqualsFreemarkerTemplate(root, "bean2,not", template.toString());
    }
}
