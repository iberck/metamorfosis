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
import org.metamorfosis.template.freemarker.AbstractFreemarkerTestCase;

/**
 *
 * @author iberck
 */
public class GMetaClassFreemarkerTest extends AbstractFreemarkerTestCase {

    public void testClassObject2() throws Exception {
        Map root = new HashMap();
        Bean2 bean2 = new Bean2();
        bean2.setProp2("p2");
        root.put("bean2", bean2);

        // class name
        String expected = "Bean2";
        String templateDef = "${bean2.class.simpleName}";
        assertEqualsFreemarkerTemplate(root, expected, templateDef);

        // solo esta exponiendo los fields, por lo tanto al pedir el name
        // del field pide la propiedad con getName() y no get(name)
        expected = "java.lang.String";
        StringBuilder sb = new StringBuilder();
        sb.append("<#list bean2.class.declaredFields as field>");
        sb.append("${field.type.name}");
        sb.append("</#list>");
        assertEqualsFreemarkerTemplate(root, expected, sb.toString());

        // solo dará true cuando la propiedad prop2 tenga un valor.
        expected = "true";
        sb = new StringBuilder();
        sb.append("<#if bean2.prop2?? >");
        sb.append("true");
        sb.append("</#if>");
        assertEqualsFreemarkerTemplate(root, expected, sb.toString());
    }
}
