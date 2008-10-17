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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final String outputFolder;
    private final String outputFileName;
    private final String templateResult;
    private final String originalContent;

    public TemplateProcessed(String originalContent, String outputFolder, String outputFileName, String templateResult) {
        this.originalContent = originalContent;
        this.outputFolder = outputFolder;
        this.outputFileName = outputFileName;
        this.templateResult = templateResult;
    }

    public TemplateProcessed(String outputFolder, String outputFileName, String templateResult) {
        this(null, outputFolder, outputFileName, templateResult);
    }

    public void write(ExternalProject project) {
        File fSourceFile = null;

        try {
            // crear el directorio si no existiera
            File fullPath = new File(project.getPath() + File.separator + outputFolder);
            if (!fullPath.exists()) {
                log.warn("El directorio '" + fullPath + "' no existe");
                log.info("Creando el directorio '" + fullPath + "'");
                fullPath.mkdirs();
            }

            // crear el archivo
            fSourceFile = new File(fullPath + File.separator + outputFileName);

            FileUtils.writeStringToFile(fSourceFile, templateResult);
            log.info("Escribiendo el archivo '" + fullPath + "'");
        } catch (IOException ex) {
            throw new MatchException("Error al escribir el archivo '" +
                    fSourceFile + templateResult + "'", ex);
        }
    }

    public void rollback(ExternalProject project) {
        log.info("Haciendo rollback");

        File file = new File(project.getPath() + File.separator + outputFolder + File.separator + outputFileName);

        // No existia un archivo original, entonces hay que borrar el generado
        if (originalContent == null) {
            log.info("Borrando el archivo '" + file + "'");
            file.delete();
        } else {
            try {
                // Reemplazar el archivo original por el generado.
                FileUtils.writeStringToFile(file, originalContent);
            } catch (IOException ex) {
                throw new MatchException("Error al hacer rollback del " +
                        "archivo '" + file + "'", ex);
            }
        }
    }
}
