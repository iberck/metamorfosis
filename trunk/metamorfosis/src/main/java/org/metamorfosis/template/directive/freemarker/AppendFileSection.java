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
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.conf.SpringUtils;
import org.metamorfosis.model.project.ExternalProject;
import org.metamorfosis.template.directive.AppendFileSectionLogic;
import org.metamorfosis.template.directive.DirectiveException;
import org.metamorfosis.template.directive.TemplateDirective;
import org.metamorfosis.template.match.TemplateProcessed;
import org.metamorfosis.template.match.TemplatesWriterPool;

/**
 *
 * @author iberck
 */
public class AppendFileSection extends Observable
        implements TemplateDirectiveModel, TemplateDirective {

    private static final Log log = LogFactory.getLog(AppendFileSection.class);
    private static final String FILE_PATH = "filePath";
    private static final String POSITION = "position";
    private static final String OCURRENCE_COUNT = "ocurrenceCount";
    private static final String OCURRENCE = "ocurrence";
    private String msgErr;

    public AppendFileSection() {
        TemplatesWriterPool pool = (TemplatesWriterPool) SpringUtils.getBean("templatesWriterPool");
        addObserver(pool);

        msgErr = "Error en la directiva, sintaxis: <@" + getDirectiveName() + " filePath=\"" +
                "position=\"[before | after]\"," +
                "ocurrenceCount=\"[first | last | number]\"," +
                "ocurrence=\"[ocurrencia a buscar dentro de filePath]\"/>";
    }

    @Override
    public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) {
        log.debug("Ejecutando <@" + getDirectiveName() + " .../>");

        // Validar parametros
        if (params.isEmpty()) {
            throw new DirectiveException(msgErr);
        }

        if (loopVars.length != 0) {
            throw new DirectiveException(
                    "<@" + getDirectiveName() + " .../> no acepta loop variables.");
        }

        // If there is non-empty nested content:
        if (body != null) {
            try {
                // Executes the nested body. Same as <#nested> in FTL, except
                // that we use our own writer instead of the current output writer.
                String filePath = null;
                String position = null;
                String ocurrenceCount = null;
                String ocurrence = null;

                Set<Entry<String, SimpleScalar>> entrySet = params.entrySet();
                for (Entry<String, SimpleScalar> entry : entrySet) {
                    String key = entry.getKey();
                    SimpleScalar value = entry.getValue();
                    if (key.equals(FILE_PATH)) {
                        filePath = value.getAsString();
                    } else if (key.equals(POSITION)) {
                        position = value.getAsString();
                    } else if (key.equals(OCURRENCE_COUNT)) {
                        ocurrenceCount = value.getAsString();
                    } else if (key.equals(OCURRENCE)) {
                        ocurrence = value.getAsString();
                    } else {
                        throw new DirectiveException("<@" + getDirectiveName() + " .../> no acepta el parámetro '" + key + "'");
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
                // 1. El filePath debe existir dentro del proyecto
                ExternalProject project = SpringUtils.getProject();
                File absProjectPath = project.getInProjectPath(filePath);
                if (!project.existsInProject(filePath)) {
                    throw new DirectiveException("<@AppendFileSection .../> " +
                            "No se encuentra el archivo '" + absProjectPath + "', " +
                            "se requiere que exista el filePath '" + filePath + "' " +
                            "dentro del proyecto original");
                }

                File fFilePath = project.getInProjectPath(filePath);
                String originalFileStr = FileUtils.readFileToString(fFilePath);
                AppendFileSectionLogic logic = new AppendFileSectionLogic(originalFileStr, sw.toString());
                String result = logic.process(position, ocurrenceCount, ocurrence);

                String outputFolder = FilenameUtils.getFullPath(filePath);
                String outputFileName = FilenameUtils.getName(filePath);

                TemplateProcessed tp = new TemplateProcessed(originalFileStr, 
                        outputFolder, outputFileName, result);

                // notificar a los observadores
                super.setChanged();
                super.notifyObservers(tp);

            } catch (TemplateException ex) {
                throw new DirectiveException("Error al ejecutar la directiva '" + getDirectiveName() + "'", ex);
            } catch (IOException ex) {
                throw new DirectiveException("Error al ejecutar la directiva '" + getDirectiveName() + "'", ex);
            }

        } else {
            throw new DirectiveException("<@" + getDirectiveName() + "missing body");
        }
    }

    @Override
    public String getDirectiveName() {
        return "AppendFileSection";
    }
}
