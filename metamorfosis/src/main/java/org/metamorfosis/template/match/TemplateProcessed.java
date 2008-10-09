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

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.model.project.ExternalProject;

/**
 * Receiver
 * @author iberck
 */
public class TemplateProcessed {

    private static final Log log = LogFactory.getLog(TemplateProcessed.class);
    private String outputFolder;
    private String outputFileName;
    private String templateResult;

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    public String getTemplateResult() {
        return templateResult;
    }

    public void setTemplateResult(String templateResult) {
        this.templateResult = templateResult;
    }

    public void write(ExternalProject project) {
        File sourceFile = null;

        try {
            // crear el directorio si no existiera
            File sourceDir = new File(project.getPath() + getOutputFolder());
            if (!sourceDir.exists()) {
                log.warn("El directorio '" + sourceDir + "' no existe");
                log.info("Creando el directorio '" + sourceDir + "'");
                sourceDir.mkdirs();
            }

            // crear el archivo
            sourceFile = new File(sourceDir, getOutputFileName());
            FileUtils.writeStringToFile(sourceFile, getTemplateResult());
        } catch (IOException ex) {
            throw new MatchException("Error al escribir el archivo '" + sourceFile + "'", ex);
        }
    }

    public void rollback(ExternalProject project) {
        // destruir el archivo generado
        File f = new File(project.getPath() + getOutputFolder() + File.separator + getOutputFileName());
        f.delete();
    }
}
