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

import java.io.File;
import org.metamorfosis.model.project.JProject;
import org.metamorfosis.model.project.NbJProject;
import org.metamorfosis.template.engine.FreemarkerEngine;
import org.metamorfosis.template.engine.TemplateEngine;

/**
 *
 * @author iberck
 */
public final class AbstractFreemarkerTestCaseTest extends AbstractFreemarkerTestCase {

    public AbstractFreemarkerTestCaseTest(String testName) {
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

    /**
     * Testea el testcase de freemarker
     */
    public void testAbstractFreemarkerTestCase() throws Exception {
        // un proyecto cualquiera
        JProject nbProject = new NbJProject();
        nbProject.setPath("./src/test/resources/NbApplication");
        TemplateEngine engine = new FreemarkerEngine();
        nbProject.setProjectWrapperFactory(engine.getEngineWrappersFactory().getProjectWrapperFactory());

        Object projectWrap = nbProject.getProjectWrapper().wrap(nbProject);

        String templateDef = "${project.name}, ${project.path}, ${project.srcPath}," +
                "${project.classesPath}, ${project.testPath}";

        String expected = "NbApplication, ./src/test/resources/NbApplication, src," +
                "build" + File.separator + "classes, test";

        // test case de las cadenas.
        assertEqualsFreemarkerTemplate(projectWrap, expected, templateDef);

        // test case de los archivos.
        File temFile = new File("./src/test/resources/templates/test1.ftl");
        assertEqualsFreemarkerTemplate(projectWrap, "src", temFile);
    }
}
