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
package org.metamorfosis.template.wrapper.freemarker;

import org.metamorfosis.template.wrapper.EngineWrappersFactory;
import org.metamorfosis.template.wrapper.MetaClassesWrapper;
import org.metamorfosis.template.wrapper.MetaPropertyWrapper;
import org.metamorfosis.template.wrapper.ProjectWrapperFactory;

/**
 *
 * @author iberck
 */
public class FreemarkerWrappersFactory implements EngineWrappersFactory {

    @Override
    public ProjectWrapperFactory getProjectWrapperFactory() {
        return new FreemarkerProjectWrapperFactory();
    }

    @Override
    public MetaPropertyWrapper getMetaPropertyWrapper() {
        return new FreemarkerMetaPropertyWrapper();
    }

    @Override
    public MetaClassesWrapper getMetaPojosWrapper() {
        return new FreemarkerMetaClassesWrapper();
    }
}
