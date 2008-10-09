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

/**
 *
 * @author iberck
 */
public class StringUtils {

    /**
     * Gets string between initFlag and endFlag
     * @param s
     * @param initFlag
     * @param endFlag
     * @return
     * @throws java.io.IOException
     */
    public static String getStringBetween(String s, String init, String end) {
        return s.substring(s.indexOf(init) + init.length(), s.indexOf(end));
    }

    /**
     * Replaces String between two flags
     * @param s The string source
     * @param toReplace The string  to replace
     * @param init Init flag to search
     * @param end End flag to search
     * @return
     * @throws java.io.IOException
     */
    public static String replaceStringBetween(String s, String toReplace, String init, String end) {
        StringBuilder sb = new StringBuilder(s);
        sb.replace(s.indexOf(init) + init.length(), s.indexOf(end), toReplace);

        return sb.toString();
    }

    /**
     * Searches a block delimited by initFlag and endFlag and replaces it from
     * s2 to s1.
     * @param s1 Source String
     * @param s2 Target String
     * @param initFlag InitFlag to search
     * @param endFlag EndFlag to search
     * @return Matched string
     */
    public static String matchStrings(String s1, String s2, String initFlag, String endFlag) {
        // new gen code
        String gCode2 = getStringBetween(s2, initFlag, endFlag);
        return replaceStringBetween(s1, gCode2, initFlag, endFlag);
    }

    public static String appendBeforeLast(String s, String strToInsert, String strLast) {
        StringBuilder sb = new StringBuilder(s);
        sb.insert(s.lastIndexOf(strLast), strToInsert);
        return sb.toString();
    }
}
