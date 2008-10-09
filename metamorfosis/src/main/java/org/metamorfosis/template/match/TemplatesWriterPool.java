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

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.metamorfosis.model.project.ExternalProject;

/**
 * Invoker
 * @author iberck
 */
public class TemplatesWriterPool implements Observer {

    private List<TemplateProjectWriter> templatesWriter = new ArrayList();

    public void addTemplateWriter(TemplateProjectWriter tw) {
        templatesWriter.add(tw);
    }

    public void writeTemplates(ExternalProject project) {
        for (TemplateProjectWriter tw : templatesWriter) {
            tw.write(project);
        }
    }

    public void rollbackTemplates(ExternalProject project) {
        for (TemplateProjectWriter tw : templatesWriter) {
            if (tw.wasWrited()) {
                tw.rollback(project);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        TemplateProcessed tp = (TemplateProcessed) arg;
        TemplateProjectWriter tpw = new TemplateProjectWriter();
        tpw.setTemplateProcessed(tp);

        addTemplateWriter(tpw);
    }
}
