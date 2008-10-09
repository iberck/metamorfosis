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
package org.metamorfosis.template.match;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.model.project.ExternalProject;

/**
 * Command
 * @author iberck
 */
public class TemplateProjectWriter implements TemplateWriter {

    private static final Log log = LogFactory.getLog(TemplateProjectWriter.class);
    private boolean wasWrited;
    // Invoker
    private TemplateProcessed templateProcessed;

    public TemplateProcessed getTemplateProcessed() {
        return templateProcessed;
    }

    public void setTemplateProcessed(TemplateProcessed templateProcessed) {
        this.templateProcessed = templateProcessed;
    }

    @Override
    public void write(ExternalProject project) {
        templateProcessed.write(project);
        wasWrited = true;
    }

    @Override
    public void rollback(ExternalProject project) {
        templateProcessed.rollback(project);
    }

    @Override
    public boolean wasWrited() {
        return wasWrited;
    }
}
