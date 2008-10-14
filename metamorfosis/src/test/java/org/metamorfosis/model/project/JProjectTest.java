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

import junit.framework.TestCase;

/**
 *
 * @author iberck
 */
public class JProjectTest extends TestCase {

    public JProjectTest(String testName) {
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
     * Testea los proyectos java básicos de netbeans
     */
    public void testNbJProject() {

        JProject nbProject = new NbJProject(".\\src\\test\\resources\\NbApplication");

        // validar nombre del proyecto
        assertEquals(nbProject.getProjectName(), "NbApplication");

        // validar estructura del proyecto
        try {
            nbProject.validateProject();
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que agregue el proyecto al classpath
        try {
            InternalClassPath.addJProject(nbProject);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que pueda que las clases del proyecto externo esten en el classpath    
        try {
            Object instance = Class.forName("nbapplication.Main").newInstance();
            assertNotNull(instance);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }
    }

    /**
     * Testea los proyectos java web de netbeans
     */
    public void testNbJWebProject() {

        JProject nbProject = new NbJWebProject(".\\src\\test\\resources\\NbWebApplicationTest");

        // validar nombre del proyecto
        assertEquals(nbProject.getProjectName(), "NbWebApplicationTest");

        // validar estructura del proyecto
        try {
            nbProject.validateProject();
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que agregue el proyecto al classpath
        try {
            InternalClassPath.addJProject(nbProject);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que pueda que las clases del proyecto externo esten en el classpath    
        try {
            Object instance = Class.forName("org.iberck.test.Foo").newInstance();
            assertNotNull(instance);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

    }

    /**
     * Testea los proyectos con estructura libre
     */
    public void testFreeJProject() {

        JProject nbProject = new FreeJProject(".\\src\\test\\resources\\FreeJProject", "src", "bin", "test");


        // validar nombre del proyecto
        assertEquals(nbProject.getProjectName(), "FreeJProject");

        // validar estructura del proyecto
        try {
            nbProject.validateProject();
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que agregue el proyecto al classpath
        try {
            InternalClassPath.addJProject(nbProject);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }

        // validar que pueda que las clases del proyecto externo esten en el classpath    
        try {
            Object instance = Class.forName("org.test.Clase").newInstance();
            assertNotNull(instance);
        } catch (Exception ex) {
            throw new AssertionError(ex.getCause());
        }
    }
}
