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
package org.metamorfosis.template.directive.freemarker;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.conf.SpringUtils;
import org.metamorfosis.model.project.ExternalProject;
import org.metamorfosis.template.directive.DirectiveException;
import org.metamorfosis.template.directive.FileProcessor;
import org.metamorfosis.template.directive.TemplateDirective;
import org.metamorfosis.template.match.TemplateProcessed;
import org.metamorfosis.template.match.TemplatesWriterPool;

/**
 *
 * @author iberck
 */
public class FileSection extends Observable
        implements TemplateDirectiveModel, TemplateDirective {

    private static final Log log = LogFactory.getLog(FileSection.class);
    private static final String FILE_PATH = "filePath";
    private static final String APPEND_BEFORE = "appendBefore";
    private static final String APPEND_AFTER = "appendAfter";

    public FileSection() {
        TemplatesWriterPool pool = (TemplatesWriterPool) SpringUtils.getBean("templatesWriterPool");
        addObserver(pool);
    }

    @Override
    public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        log.debug("Ejecutando <@" + getDirectiveName() + " .../>");

        // Validar parametros
        if (params.isEmpty()) {
            throw new DirectiveException(
                    "<@" + getDirectiveName() + " .../> 'filePath' es requerido en los parámetros.");
        }

        if (loopVars.length != 0) {
            throw new DirectiveException(
                    "<@" + getDirectiveName() + " .../> doesn't allow loop variables.");
        }

        // If there is non-empty nested content:
        if (body != null) {
            try {
                // Executes the nested body. Same as <#nested> in FTL, except
                // that we use our own writer instead of the current output writer.
                String filePath = null;
                String appendBefore = null;
                String appendAfter = null;

                Set<Entry<String, SimpleScalar>> entrySet = params.entrySet();
                for (Entry<String, SimpleScalar> entry : entrySet) {
                    String key = entry.getKey();
                    SimpleScalar value = entry.getValue();
                    if (key.equals(FILE_PATH)) {
                        filePath = value.getAsString();
                    } else if (key.equals(APPEND_BEFORE)) {
                        appendBefore = value.getAsString();
                    } else if (key.equals(APPEND_AFTER)) {
                        appendAfter = value.getAsString();
                    } else {
                        throw new DirectiveException("<@" + getDirectiveName() + " .../> doesn't allow " + key + " parameter");
                    }
                }

                // Escribir resultado del procesamiento en un buffer
                StringWriter sw = new StringWriter();
                BufferedWriter bw = new BufferedWriter(sw);
                body.render(bw);
                env.getOut().flush();
                bw.flush();
                sw.flush();
                bw.close();
                sw.close();

                // =============================================================
                ExternalProject project = SpringUtils.getProject();
                File fFilePath = project.getInProjectPath(filePath);
                String originalFileStr = FileUtils.readFileToString(fFilePath);
                String result = FileProcessor.process(getDirectiveName(), filePath, appendBefore, appendAfter, originalFileStr, sw.toString());

                TemplateProcessed tp = new TemplateProcessed();
                tp.setTemplateResult(result);

                // TODO: Put outputfoler and filename
                //tp.setOutputFolder(filePath);
                //tp.setOutputFileName(name);

                // notificar a los observadores
                super.setChanged();
                super.notifyObservers(tp);

            } catch (TemplateException ex) {
                throw new DirectiveException("Error al ejecutar la directiva '" + getDirectiveName() + "'", ex);
            } catch (IOException ex) {
                throw new DirectiveException("Error al ejecutar la directiva '" + getDirectiveName() + "'", ex);
            }

        } else {
            throw new DirectiveException("missing body");
        }
    }

    @Override
    public String getDirectiveName() {
        return "FileSection";
    }
}
