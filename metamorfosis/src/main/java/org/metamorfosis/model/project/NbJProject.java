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

import java.io.File;

/**
 * Esta clase modela un proyecto java básico en netbeans
 * @author iberck
 */
public class NbJProject extends JProject {

    public NbJProject() {
    }

    public NbJProject(String path) {
        setPath(path);
    }

    /**
     * @see ExternalProject
     * @return
     */
    @Override
    public String getClassesPath() {
        return "build" + File.separator + "classes";
    }

    /**
     * @see ExternalProject
     * @return
     */
    @Override
    public String getSrcPath() {
        return "src";
    }

    /**
     * @see ExternalProject
     * @return
     */
    @Override
    public String getTestPath() {
        return "test";
    }
}
