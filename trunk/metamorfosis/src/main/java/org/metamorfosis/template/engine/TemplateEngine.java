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
package org.metamorfosis.template.engine;

import org.metamorfosis.model.GroupTemplatesMatch;
import org.metamorfosis.model.SingleTemplateMatch;
import org.metamorfosis.model.project.ExternalProject;
import org.metamorfosis.template.wrapper.EngineWrappersFactory;

/**
 *
 * @author iberck
 */
public interface TemplateEngine {

    public void setUpEnvironment(ExternalProject project);

    public void setUpDirectives();

    public EngineWrappersFactory getEngineWrappersFactory();

    public void match(SingleTemplateMatch singleTemplateMatch);

    public void match(GroupTemplatesMatch groupTemplatesMatch);
}
