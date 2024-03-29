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
package org.metamorfosis.framework;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author iberck
 */
public class SpringContextTest extends TestCase {

    private static final Log log = LogFactory.getLog(SpringContextTest.class);

    public SpringContextTest(String testName) {
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

    public void testSpringContext() {
        BeanFactory factory = new XmlBeanFactory(
                new ClassPathResource("application-context.xml"));

        assertNotNull(factory);
    }

    public void testClassPathResource() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/internalTemplate.ftl");
        File file = resource.getFile();
        log.debug("classpath file: " + file);
        String filename = resource.getFilename();
        log.debug("filename: " + filename);
        String path = resource.getPath();
        log.debug("path: " + path);
        String absolutePath = file.getAbsolutePath();
        log.debug("absolutePath: " + absolutePath);

        resource = new ClassPathResource("overview.html");
        InputStream inputStream = resource.getInputStream();
        log.debug(inputStream);
    }

    public void testClassPathResourceLocation() {
        BeanFactory factory = new XmlBeanFactory(
                new ClassPathResource("application-context-test.xml"));
        ResourceClassTest resourceClassTest = (ResourceClassTest) factory.getBean("resourceClass");
        String filename = resourceClassTest.getResource().getFilename();
        log.debug("classpath resource filename: " + filename);
    }
}
