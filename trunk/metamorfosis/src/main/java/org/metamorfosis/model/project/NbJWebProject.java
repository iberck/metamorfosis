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
 * Esta clase modela un proyecto java web en netbeans
 * @author iberck
 */
public class NbJWebProject extends JWebProject {

    public NbJWebProject() {
    }

    public NbJWebProject(String projectFolder) {
        super(projectFolder);
    }

    @Override
    public String getWebInfPath() {
        return "web" + File.separator + "WEB-INF";
    }

    @Override
    public String getClassesPath() {
        return "build" + File.separator + "web" + File.separator + "WEB-INF" + File.separator + "classes";
    }

    @Override
    public String getSrcPath() {
        return "src";
    }

    @Override
    public String getTestPath() {
        return "test";
    }
}
