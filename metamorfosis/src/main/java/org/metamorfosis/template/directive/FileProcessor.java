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
package org.metamorfosis.template.directive;

import org.metamorfosis.conf.SpringUtils;
import org.metamorfosis.model.project.ExternalProject;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author iberck
 */
public class FileProcessor {

    public static String process(String directiveName, String filePath, String appendBefore, String appendAfter, String originalFileStr, String matchOutputStr) {
        ExternalProject project = SpringUtils.getProject();
        String result = null;

        // No pueden existir las instrucciones appendAfter y appendBefore
        if (appendBefore != null || appendAfter != null) {
            throw new DirectiveException("<@" + directiveName + " .../> " +
                    "no permite appendBefore y appendLast al mismo tiempo");
        } else if (appendBefore != null || appendAfter != null) {
            // append

            // si esta presente appendBefore o appendLast hay q validar que
            // exista el archivo dentro del proyecto
            if (!project.existsInProject(filePath)) {
                throw new DirectiveException("<@" + directiveName + " .../> " +
                        "cuando utiliza appendBefore o appendLast es necesario " +
                        "que exista el archivo filePath dentro del proyecto");
            }

            AppendParser.Result appendResult = null;
            // append
            if (appendBefore != null && appendAfter == null) {
                appendResult = AppendParser.parseAppendInstruction(appendBefore, originalFileStr);
            } else if (appendBefore == null && appendAfter != null) {
                appendResult = AppendParser.parseAppendInstruction(appendAfter, originalFileStr);
            }

            // validar que la ocurrencia no sea mayor a las que existen
            int ocurrenceNum = appendResult.getOcurrenceNum();
            String sub = appendResult.getString();
            int matchesInFile = StringUtils.countMatches(originalFileStr, sub);

            if (ocurrenceNum > matchesInFile) {
                throw new DirectiveException("<@" + directiveName + " .../> " +
                        "El número de ocurrencia solicitada '" + ocurrenceNum + "' " +
                        "supera el numero de ocurrencias en el archivo original '" + matchesInFile + "'");
            }

            StringBuilder integrateOutput = new StringBuilder(originalFileStr);

            int initIndex = StringUtils.ordinalIndexOf(originalFileStr, sub, ocurrenceNum);
            String lineSeparator = System.getProperty("line.separator");
            integrateOutput.insert(initIndex, lineSeparator);
            integrateOutput.insert(lineSeparator.length(), matchOutputStr);
            result = integrateOutput.toString();
        }
        // =============================================================

        return result;
    }
}
