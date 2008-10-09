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
package org.metamorfosis.model.project;

import org.metamorfosis.model.project.InternalClassPath;
import java.io.File;
import java.lang.reflect.Field;
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
public class InternalClassPathTest extends TestCase {

    private static final Log log = LogFactory.getLog(TestCase.class);

    public InternalClassPathTest(String testName) {
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
     * Test of addFile method, of class RuntimeClassPath.
     */
    public void testAddClassesDir() throws Exception {
        log.debug("addFile");

        // verificar que no se puede crear un objeto de la clase
        try {
            Class.forName("testproject.ToClassPathPojo");
            throw new AssertionError("El objeto no se debería haber creado");
        } catch (Exception ex) {
            assertTrue(true);
        }

        // verificar que SI se puede crear un objeto de la clase
        File classes = new File("./src/test/resources/classes/");
        InternalClassPath.addResource(classes);

        Class<?> classForName = null;
        try {
            classForName = Class.forName("testproject.ToClassPathPojo");
        } catch (Exception ex) {
            throw new AssertionError("No se pudo leer el objeto del classpath");
        }

        // verificar campos de la clase que se cargo en el classpath interno
        assertNotNull(classForName);

        Object o = classForName.newInstance();
        assertEquals(o.getClass().getName(), "testproject.ToClassPathPojo");
        Field[] declaredFields = o.getClass().getDeclaredFields();

        assertEquals(declaredFields[0].getName(), "name");
        assertEquals(declaredFields[0].getType().getName(), "java.lang.String");
    }

    public void testAddResource() throws Exception {
        log.debug("addResourcesDir");

        try {
            BeanFactory factory = new XmlBeanFactory(
                    new ClassPathResource("up-to-classpath.xml"));
            throw new AssertionError("No tendría que haber encontrado el recurso en el classpath");
        } catch (Throwable t) {
        }

        File resources = new File("./src/test/resources/up-to-classpath.xml");
        InternalClassPath.addResource(resources);

        BeanFactory factory = new XmlBeanFactory(
                new ClassPathResource("up-to-classpath.xml"));
        assertNotNull(factory);
        assertNotNull(factory.getBean("stringBean"));
    }
}
