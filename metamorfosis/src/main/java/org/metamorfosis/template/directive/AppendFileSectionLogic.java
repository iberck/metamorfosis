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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author iberck
 */
public class AppendFileSectionLogic {

    private static final Log log = LogFactory.getLog(AppendFileSectionLogic.class);
    private final String matchOutputStr;
    private final String originalStr;

    public AppendFileSectionLogic(String originalStr, String matchOutputStr) {
        this.originalStr = originalStr;
        this.matchOutputStr = matchOutputStr;
    }

    public String process(String position, String ocurrenceCountStr, String ocurrence) {
        // 1. interpretar el ocurrenceCount: [first , last, number] -> a numero
        int ocurrenceCount = ocurrenceCountParser(originalStr, ocurrenceCountStr, ocurrence);

        // 1.1 Validar el ocurrenceCount
        int matchesInFile = StringUtils.countMatches(originalStr, ocurrence);
        if (ocurrenceCount > matchesInFile) {
            throw new DirectiveException("<@AppendFileSection .../> " +
                    "El número de ocurrencia solicitada '" + ocurrenceCount + "' " +
                    "supera el numero de ocurrencias en el archivo original '" + matchesInFile + "'");
        }

        // 2. Obtener la posicion de la ocurrencia dentro del archivo original
        int initIndex = StringUtils.ordinalIndexOf(originalStr, ocurrence, ocurrenceCount);
        int lastIndex = initIndex + ocurrence.length();

        //  3. Interpretar la posicion
        StringBuilder originalSb = new StringBuilder(originalStr);
        if ("before".equalsIgnoreCase(position)) {
            originalSb.insert(initIndex, matchOutputStr);
        } else if ("after".equalsIgnoreCase(position)) {
            originalSb.insert(lastIndex, matchOutputStr);
        }

        return originalSb.toString();
    }

    private int ocurrenceCountParser(String originalFileText, String ocurrenceCountStr, String ocurrence) {
        int ocurrenceInt = 0;

        try {
            ocurrenceInt = Integer.parseInt(ocurrenceCountStr);
        } catch (NumberFormatException ex) {
            if ("first".equals(ocurrenceCountStr)) {
                ocurrenceInt = 1;
            } else if ("last".equals(ocurrenceCountStr)) {
                ocurrenceInt = StringUtils.countMatches(originalFileText, ocurrence);
            }
        }

        if (ocurrenceInt == 0) {
            throw new DirectiveException("Error en la directiva <@AppendFileSection, " +
                    "ocurrenceCount solo permite [first | last | number]");
        }

        return ocurrenceInt;
    }
}
