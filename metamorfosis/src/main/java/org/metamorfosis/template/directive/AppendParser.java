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

/**
 *
 * @author iberck
 */
public class AppendParser {

    private final static String err = "Error al procesar appendBefore o " +
            "appendLast, sintaxis: [appendBefore,appendLast]=\"{ocurrence:character}\" " +
            "donde ocurrence:[fist,last,number] y character cualquier caracter dentro " +
            "del archivo";

    private static String[] parse(String s) {
        String[] res = new String[2];

        if (s == null || s.length() < 1 || !s.contains(":")) {
            throw new DirectiveException(err);
        }

        String wos = StringUtils.deleteWhitespace(s);
        int i = wos.indexOf(":");
        String ocurrenceStr = wos.substring(0, i);
        String characters = wos.substring(i, wos.length());

        if (ocurrenceStr == null || characters == null || ocurrenceStr.length() < 0 || characters.length() < 0) {
            throw new DirectiveException(err);
        }

        res[0] = ocurrenceStr;
        res[1] = characters;

        return res;
    }

    private static int processOcurrenceNumber(String ocurrenceStr, String fileText) {
        int ocurrenceInt = 0;

        try {
            ocurrenceInt = Integer.parseInt(ocurrenceStr);
        } catch (NumberFormatException ex) {
            if ("first".equalsIgnoreCase(ocurrenceStr)) {
                ocurrenceInt = 1;
            } else if ("last".equalsIgnoreCase(ocurrenceStr)) {
                ocurrenceInt = StringUtils.countMatches(fileText, ocurrenceStr);
            }
        }

        if (ocurrenceInt == 0) {
            throw new DirectiveException(err);
        }

        return ocurrenceInt;
    }

    public static Result parseAppendInstruction(String appendStr, String fileText) {
        Result res = new Result();

        String[] parsed = parse(appendStr);
        int iOcurrence = processOcurrenceNumber(parsed[0], fileText);

        res.setOcurrenceNum(iOcurrence);
        res.setString(parsed[1]);

        return res;
    }

    public static class Result {

        private int ocurrenceNum;
        private String string;

        public int getOcurrenceNum() {
            return ocurrenceNum;
        }

        public void setOcurrenceNum(int ocurrenceNum) {
            this.ocurrenceNum = ocurrenceNum;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}