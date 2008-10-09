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

import org.metamorfosis.template.wrapper.*;
import java.util.HashMap;
import java.util.Map;
import org.metamorfosis.model.project.JProject;

/**
 *
 * @author iberck
 */
public class FreemarkerJProjectWrapper implements ProjectWrapper<JProject> {

    protected final static String PROJECT_PROPERTIES = "project";
    protected final static String PROJECT_PATH = "path";
    protected final static String PROJECT_NAME = "name";
    protected final static String PROJECT_SRC_PATH = "srcPath";
    protected final static String PROJECT_CLASSES_PATH = "classesPath";
    protected final static String PROJECT_TEST_PATH = "testPath";

    FreemarkerJProjectWrapper() {
    }

    /**
     * Realiza el wrapper de un proyecto java básico, hasta el momento se cuenta con
     * las siguientes propiedades:
     * 
     * ${project.path} -> Path absoluto del proyecto
     * ${project.name} -> Nombre del proyecto
     * ${project.srcPath} -> Carpeta relativa a los sources del proyecto
     * ${project.classesPath} -> Carpeta relativa a los .class del proyecto
     * ${project.testPath} -> Carpeta relativa a los test del proyecto
     * 
     * @param project
     * @return
     */
    @Override
    public Map wrap(JProject project) {
        Map root = new HashMap();
        Map properties = new HashMap();

        root.put(PROJECT_PROPERTIES, properties);
        properties.put(PROJECT_PATH, project.getPath());
        properties.put(PROJECT_NAME, project.getProjectName());
        properties.put(PROJECT_SRC_PATH, project.getSrcPath());
        properties.put(PROJECT_CLASSES_PATH, project.getClassesPath());
        properties.put(PROJECT_TEST_PATH, project.getTestPath());

        return root;
    }
}
