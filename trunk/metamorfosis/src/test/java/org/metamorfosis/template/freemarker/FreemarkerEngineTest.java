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
package org.metamorfosis.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author iberck
 */
public class FreemarkerEngineTest extends TestCase {

    private static final Log log = LogFactory.getLog(FreemarkerEngineTest.class);

    public void testCreateTemplateFromReader() throws IOException {
        Configuration cfg = new Configuration();
        ClassPathResource resource = new ClassPathResource("templates/internalTemplate.ftl");
        InputStream is = resource.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);

        Template template = new Template("nombreTemplate", reader, cfg);
        assertEquals("nombreTemplate", template.getName());
        log.debug("template: " + template);
    }
}
