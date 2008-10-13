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

import org.metamorfosis.model.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.metamorfosis.template.engine.FreemarkerEngine;
import org.metamorfosis.template.engine.TemplateEngine;
import org.metamorfosis.template.freemarker.AbstractFreemarkerTestCase;
import org.metamorfosis.template.wrapper.MetaPojosWrapper;

/**
 *
 * @author iberck
 */
public class MetaPojosFreemarkerWrapTest extends AbstractFreemarkerTestCase {

    

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testMetaPojosFreemarker() throws Exception {
        // crear lista de metapojos
        List<MetaPojo> jMetaPojos = new ArrayList();
        MetaPojo bean1 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean1");
        MetaPojo bean2 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean2");
        MetaPojo bean3 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean3");

        TemplateEngine engine = new FreemarkerEngine();
        MetaPojosWrapper metaPojosWrapper = engine.getEngineWrappersFactory().getMetaPojosWrapper();

        // injectar propiedad
        bean1.injectPojoProperty("injectedProperty1", "value");

        jMetaPojos.add(bean1);
        jMetaPojos.add(bean2);
        jMetaPojos.add(bean3);


        Map metaPojosWrapped = (Map) metaPojosWrapper.wrap(jMetaPojos);

        // template1 test (simpleName)
        StringBuilder t1 = new StringBuilder();
        t1.append("<#list metaPojos as pojo>");
        t1.append("${pojo.class.simpleName},");
        t1.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "Bean1,Bean2,Bean3,", t1.toString());

        // test de los fields y propiedad inyectada a la clase
        StringBuilder t2 = new StringBuilder();
        t2.append("<#list metaPojos as pojo>");
        t2.append("<#list pojo.class.declaredFields as field>");
        t2.append("${field.name},");
        t2.append("</#list>");
        t2.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "injectedProperty1,prop1,prop1_1,prop2,propBean3,prop3,", t2.toString());
    }

    public void testMetaPojosInjectedFieldsFreemarker() throws Exception {

        // crear lista de metapojos
        List<MetaPojo> jMetaPojos = new ArrayList();
        JMetaPojo bean1 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean2");

        // injectar field
        bean1.injectFieldProperty("prop2", "primaryKey", "value");
        jMetaPojos.add(bean1);

        TemplateEngine engine = new FreemarkerEngine();
        MetaPojosWrapper metaPojosWrapper = engine.getEngineWrappersFactory().getMetaPojosWrapper();

        Map metaPojosWrapped = (Map) metaPojosWrapper.wrap(jMetaPojos);

        // solo debe tener la propiedad prop2
        StringBuilder t2 = new StringBuilder();
        t2.append("<#list metaPojos as pojo>");
        t2.append("<#list pojo.class.declaredFields as field>");
        t2.append("${field.name},");
        t2.append("</#list>");
        t2.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "prop2,", t2.toString());

        // Field inyectado a nivel clase
        StringBuilder t3 = new StringBuilder();
        t3.append("<#list metaPojos as pojo>");
        t3.append("<#if pojo.prop2.primaryKey??>si existe<#else>no existe</#if>");
        t3.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "si existe", t3.toString());

        // test ??
        StringBuilder t4 = new StringBuilder();
        t4.append("<#list metaPojos as pojo>");
        t4.append("<#if pojo.prop2.anyProperty??>si existe<#else>no existe</#if>");
        t4.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "no existe", t4.toString());

        // field inyectado a nivel declaredFields
        StringBuilder t6 = new StringBuilder();
        t6.append("<#list metaPojos as pojo>");
        t6.append("<#list pojo.class.declaredFields as field>");
        t6.append("<#if field.primaryKey??>");
        t6.append("declaredField inyectado satisfactoriamente");
        t6.append("<#else>");
        t6.append("error");
        t6.append("</#if>");
        t6.append("</#list>");
        t6.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "declaredField inyectado satisfactoriamente", t6.toString());
    }

    public void testGeneralInjection() throws Exception {
        // crear lista de metapojos
        List<MetaPojo> jMetaPojos = new ArrayList();
        JMetaPojo bean1 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean2");

        // injectar field
        bean1.injectPojoProperty("nuevaPropiedad", new Integer(5));
        bean1.injectFieldProperty("nuevaPropiedad", "validate", "InView");//inyectar propiedad nueva
        bean1.injectFieldProperty("prop2", "primaryKey", "value");//inyectar propiedad existente
        jMetaPojos.add(bean1);

        TemplateEngine engine = new FreemarkerEngine();
        MetaPojosWrapper metaPojosWrapper = engine.getEngineWrappersFactory().getMetaPojosWrapper();
        Map metaPojosWrapped = (Map) metaPojosWrapper.wrap(jMetaPojos);

        // propiedades
        StringBuilder t2 = new StringBuilder();
        t2.append("<#list metaPojos as pojo>");
        t2.append("<#list pojo.class.declaredFields as field>");
        t2.append("${field.name},");
        t2.append("</#list>");
        t2.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "prop2,nuevaPropiedad,", t2.toString());

        // fields existente inyectado
        StringBuilder t3 = new StringBuilder();
        t3.append("<#list metaPojos as pojo>");
        t3.append("<#list pojo.class.declaredFields as field>");
        t3.append("<#if field.name=='prop2' && field.primaryKey??>");
        t3.append("field existente inyectado ${field.name} ${field.primaryKey}");
        t3.append("</#if>");
        t3.append("</#list>");
        t3.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "field existente inyectado prop2 value", t3.toString());

        // fields nuevo inyectado
        StringBuilder t4 = new StringBuilder();
        t4.append("<#list metaPojos as pojo>");
        t4.append("<#list pojo.class.declaredFields as field>");
        t4.append("<#if field.name=='nuevaPropiedad' && field.validate??>");
        t4.append("metafield inyectado ${field.name} ${field.validate}");
        t4.append("</#if>");
        t4.append("</#list>");
        t4.append("</#list>");
        assertEqualsFreemarkerTemplate(metaPojosWrapped, "metafield inyectado nuevaPropiedad InView", t4.toString());
    }

    public void testInjectionTypes() throws Exception {
        // crear lista de metapojos
        List<MetaPojo> jMetaPojos = new ArrayList();
        JMetaPojo bean2 = new JMetaPojo("org.metamorfosis.template.wrapper.freemarker.Bean2");

        Bean3 bean3 = new Bean3();
        // injectar field
        bean2.injectPojoProperty("nuevaPropiedad", new Integer(5));
        bean2.injectPojoProperty("nuevaPropiedad2", "dos");
        bean2.injectPojoProperty("propiedadObjeto", bean3);

        jMetaPojos.add(bean2);

        TemplateEngine engine = new FreemarkerEngine();
        MetaPojosWrapper metaPojosWrapper = engine.getEngineWrappersFactory().getMetaPojosWrapper();
        Map metaPojosWrapped = (Map) metaPojosWrapper.wrap(jMetaPojos);

        // pojo.class.simpleName, pojo.class.name, pojo.toString
        // pojo.class.toString
        StringBuilder t2 = new StringBuilder();
        t2.append("<#list metaPojos as pojo>");
        t2.append("1.[${pojo.class.simpleName}], [${pojo.class.name}]");
        t2.append("<#if pojo?contains('JMetaPojo')>");
        t2.append("2.[pojo es de tipo JMetaPojo]");
        t2.append("</#if>");
        t2.append("<#if pojo.class?contains('JMetaPojo')>");
        t2.append("3.[pojo.class es de tipo JMetaPojo]");
        t2.append("</#if>");
        t2.append("4.[${pojo.nuevaPropiedad}]");
        t2.append("5.[${pojo.nuevaPropiedad2}]");

        t2.append("<#if pojo.propiedadObjeto?contains('Bean3')>");
        t2.append("5.1[pojo.propiedadObjeto es de tipo Bean3]");
        t2.append("</#if>");
//
        t2.append("6.[${pojo.nuevaPropiedad.class.name}]");
        t2.append("7.[${pojo.nuevaPropiedad2.class.name}]");
        t2.append("8.[${pojo.propiedadObjeto.class.name}]");

//        t2.append("<#list pojo.propiedadObjeto.class.declaredFields as f>");
//        t2.append("9.[${f.name}]");
//        t2.append("</#list>");
//
//        t2.append("<#list pojo.prop2.class.declaredFields as f>");
//        t2.append("10.[${f.name}]");
//        t2.append("</#list>");
//

        t2.append("</#list>");

        StringBuilder exp = new StringBuilder();
        exp.append("1.[Bean2], [org.metamorfosis.template.wrapper.freemarker.Bean2]");
        exp.append("2.[pojo es de tipo JMetaPojo]");
        exp.append("3.[pojo.class es de tipo JMetaPojo]");

        exp.append("4.[5]");
        exp.append("5.[dos]");
        exp.append("5.1[pojo.propiedadObjeto es de tipo Bean3]");
        exp.append("6.[java.lang.Integer]");
        exp.append("7.[java.lang.String]");
        exp.append("8.[org.metamorfosis.template.wrapper.freemarker.Bean3]");

//        exp.append("9.[prop3]");
//        exp.append("10.[]");

        assertEqualsFreemarkerTemplate(metaPojosWrapped, exp.toString(), t2.toString());
    }
}
