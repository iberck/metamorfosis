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

import java.util.Map;
import org.metamorfosis.model.project.JProject;
import org.metamorfosis.model.project.JWebProject;

/**
 *
 * @author iberck
 */
public class FreemarkerJWebProjectWrapper extends FreemarkerJProjectWrapper {

    protected final static String PROJECT_WEB_INF_PATH = "webInfPath";

    FreemarkerJWebProjectWrapper() {
    }

    /**
     * Realiza el wrapper de un proyecto java básico, hasta el momento se cuenta con
     * las siguientes propiedades:
     * @see JProjectWrapper
     * 
     * ${project.webInfPath} -> Ruta relativa a la carpeta WEB-INF del proyecto
     * ${project.webInfAbsPath} -> Ruta absoluta a la carpeta WEB-INF del proyecto
     * @param project
     * @return
     */
    @Override
    public Map wrap(JProject project) {
        // hace el wrapper de las propiedades padre
        JWebProject webProject = (JWebProject) project;
        Map m = super.wrap(webProject);

        Map properties = (Map) m.get(PROJECT_PROPERTIES);
        properties.put(PROJECT_WEB_INF_PATH, webProject.getWebInfPath());

        return m;
    }
}
