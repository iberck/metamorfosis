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

import java.util.HashMap;
import java.util.Map;
import org.metamorfosis.model.MetaProperty;
import org.metamorfosis.model.MetaPropertyMap;
import org.metamorfosis.model.MetaPropertyObject;
import org.metamorfosis.template.engine.FreemarkerEngine;
import org.metamorfosis.template.engine.TemplateEngine;
import org.metamorfosis.template.freemarker.AbstractFreemarkerTestCase;
import org.metamorfosis.template.wrapper.EngineWrappersFactory;

/**
 *
 * @author iberck
 */
public class PropertyWrapTest extends AbstractFreemarkerTestCase {

    public PropertyWrapTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFreemarkerPropertyMapTest() throws Exception {
        TemplateEngine engine = new FreemarkerEngine();
        EngineWrappersFactory wrappersFactory = engine.getEngineWrappersFactory();

        MetaProperty metaPropertyMap = new MetaPropertyMap();

        Map m = new HashMap();
        m.put("llave", "_valor_");
        metaPropertyMap.setValue(m);

        Map rootMap = (Map) wrappersFactory.getMetaPropertyWrapper().wrap(metaPropertyMap);
        assertEqualsFreemarkerTemplate(rootMap, "_valor_", "${metaProperty.llave}");
    }

    public void testFreemarkerPropertyObjectTest() throws Exception {
        TemplateEngine engine = new FreemarkerEngine();
        EngineWrappersFactory wrappersFactory = engine.getEngineWrappersFactory();

        MetaProperty metaPropertyObject = new MetaPropertyObject();

        Cadena cad = new Cadena("hola mundo");
        metaPropertyObject.setValue(cad);

        Map rootMap = (Map) wrappersFactory.getMetaPropertyWrapper().wrap(metaPropertyObject);

        assertEqualsFreemarkerTemplate(rootMap, "hola mundo", "${metaProperty.valor}");
        assertEqualsFreemarkerTemplate(rootMap, "Cadena", "${metaProperty.class.simpleName}");
    }
}
