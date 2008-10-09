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

import org.metamorfosis.template.wrapper.ProjectWrapper;

/**
 *
 * Esta clase abstracta modela los proyectos externos de tipo java web
 * @author iberck
 */
public abstract class JWebProject extends JProject {

    protected JWebProject() {
    }

    protected JWebProject(String path) {
        setPath(path);
    }

    /**
     * 
     * Path donde se encuentra la carpeta WEB-INF del proyecto web
     * esta ruta debe ser relativa al proyecto.
     * @return
     */
    public abstract String getWebInfPath();

    /**
     * Valida que sea un proyecto web correcto
     * @throws ProjectException
     */
    @Override
    public void validateProject() throws ProjectException {
        super.validateProject();

        // valida que se encuentre la carpeta classes
        String webInf = getWebInfPath();
        if (!existsInProject(webInf)) {
            throw new ProjectException("No existe la carpeta WEB-INF '" + webInf + "' dentro del " +
                    "proyecto");
        }
    }

    /**
     * Obtiene el wrapper para proyectos web
     * @return
     */
    @Override
    public ProjectWrapper getProjectWrapper() {
        return projectWrapperFactory.getJWebProjectWrapper();
    }
}
