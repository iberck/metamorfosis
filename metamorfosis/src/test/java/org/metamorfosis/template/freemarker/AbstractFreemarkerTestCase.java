/**
 *  Copyright (c) 2007-2008 by Carlos G�mez Montiel <iberck@gmail.com>
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

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleObjectWrapper;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import junit.framework.TestCase;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author iberck
 */
public abstract class AbstractFreemarkerTestCase extends TestCase {

    private BeansWrapper bw_instance;
    Configuration cfg;
    /**
     * setMethodsShadowItems
     *
     * Sets whether methods shadow items in beans. When true (this is the default
     * value), ${object.name} will first try to locate a bean method or property with
     * the specified name on the object, and only if it doesn't find it will it
     * try to call object.get(name), the so-called "generic get method" that is
     * usually used to access items of a container (i.e. elements of a map).
     * When set to false, the lookup order is reversed and generic get method is
     * called first, and only if it returns null is method lookup attempted.
     */


    {
        bw_instance = SimpleObjectWrapper.getDefaultInstance();
        bw_instance.setMethodsShadowItems(true);
        //bw_instance.setUseCache(true);
    }

    public AbstractFreemarkerTestCase(String name) {
        super(name);
    }

    public AbstractFreemarkerTestCase() {
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        File tmp = new File(System.getProperty("java.io.tmpdir"));

        cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(tmp);
        cfg.setObjectWrapper(bw_instance);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected void assertEqualsFreemarkerTemplate(Object rootMap, String expectedResult, String template) throws Exception {
        File fTemplate = File.createTempFile("fmt_", ".ftl");
        FileWriter fw = new FileWriter(fTemplate);
        fw.write(template);
        fw.flush();

        String relativeTemplate = FilenameUtils.getName(fTemplate.getAbsolutePath());
        freemarker.template.Template freeMarkerTemplate = cfg.getTemplate(relativeTemplate);

        StringWriter sw = new StringWriter();
        Environment env = freeMarkerTemplate.createProcessingEnvironment(rootMap, sw);
        env.process(); // process the template

        assertEquals(expectedResult, sw.toString());
        sw.close();
        fw.close();
        fTemplate.delete();
    }

    protected void assertEqualsFreemarkerTemplate(Object rootMap, String expectedResult, File template) throws Exception {
        String fullPath = FilenameUtils.getFullPath(template.getAbsolutePath());
        cfg.setDirectoryForTemplateLoading(new File(fullPath));

        String relativeTemplate = FilenameUtils.getName(template.getAbsolutePath());
        freemarker.template.Template freeMarkerTemplate = cfg.getTemplate(relativeTemplate);

        StringWriter sw = new StringWriter();
        Environment env = freeMarkerTemplate.createProcessingEnvironment(rootMap, sw);
        env.process(); // process the template

        assertEquals(expectedResult, sw.toString());
        sw.close();
    }
}
