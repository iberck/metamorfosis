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
package org.metamorfosis.template.directive;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author iberck
 */
public class AppendFileSectionLogicTest extends TestCase {

    private static final Log log = LogFactory.getLog(AppendFileSectionLogicTest.class);

    public void testAppendFileSection() {
        String originalStr = "<beans></beans>";
        String append = "<bean id=.../>";

        AppendFileSectionLogic logic = new AppendFileSectionLogic(originalStr, append);
        String result = logic.process("after", "last", "<beans>");
        assertEquals("<beans><bean id=.../></beans>", result);
        result = logic.process("after", "first", "<beans>");
        assertEquals("<beans><bean id=.../></beans>", result);
        result = logic.process("after", "first", "</beans>");
        assertEquals("<beans></beans><bean id=.../>", result);
        result = logic.process("after", "1", "</beans>");
        assertEquals("<beans></beans><bean id=.../>", result);
        result = logic.process("after", "1", "</beans>");
        assertEquals("<beans></beans><bean id=.../>", result);

        result = logic.process("before", "first", "<beans>");
        assertEquals("<bean id=.../><beans></beans>", result);
        result = logic.process("before", "first", "</beans>");
        assertEquals("<beans><bean id=.../></beans>", result);
        result = logic.process("before", "last", "</beans>");
        assertEquals("<beans><bean id=.../></beans>", result);
        result = logic.process("before", "1", "</beans>");
        assertEquals("<beans><bean id=.../></beans>", result);


        originalStr = "<beans><beans></beans>";
        append = "<bean id=.../>";

        logic = new AppendFileSectionLogic(originalStr, append);
        result = logic.process("after", "last", "<beans>");
        assertEquals("<beans><beans><bean id=.../></beans>", result);
        result = logic.process("after", "2", "<beans>");
        assertEquals("<beans><beans><bean id=.../></beans>", result);
        result = logic.process("after", "first", "<beans>");
        assertEquals("<beans><bean id=.../><beans></beans>", result);
        result = logic.process("after", "last", "</beans>");
        assertEquals("<beans><beans></beans><bean id=.../>", result);
        result = logic.process("before", "last", "</beans>");
        assertEquals("<beans><beans><bean id=.../></beans>", result);

        originalStr = "<beans><beans><_></beans></beans>";
        append = "appendText";

        logic = new AppendFileSectionLogic(originalStr, append);
        result = logic.process("after", "last", "<beans>");
        assertEquals("<beans><beans>appendText<_></beans></beans>", result);

        result = logic.process("after", "first", "<_>");
        assertEquals("<beans><beans><_>appendText</beans></beans>", result);
        result = logic.process("after", "1", "<_>");
        assertEquals("<beans><beans><_>appendText</beans></beans>", result);
        result = logic.process("after", "2", "</beans>");
        assertEquals("<beans><beans><_></beans></beans>appendText", result);
        result = logic.process("after", "last", "</beans>");
        assertEquals("<beans><beans><_></beans></beans>appendText", result);
        result = logic.process("before", "last", "</beans>");
        assertEquals("<beans><beans><_></beans>appendText</beans>", result);
    }
}
