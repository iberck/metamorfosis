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
package org.metamorfosis.model.project;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Esta clase modela el classpath interno del framework y sirve para introducir
 * recursos de un proyecto externo al classpath interno con el propósito de
 * obtener hacer uso de ellos en tiempo de ejecución.
 *
 * @author iberck
 */
public class InternalClassPath {

    private static final Log log = LogFactory.getLog(InternalClassPath.class);
    private static final Class<?>[] parameters = new Class[]{URL.class};

    /**
     * Agrega una url al classloader interno
     * @see RuntimeClassPath
     * @param url
     * @throws ProjectException No se puede agregar el recurso al classpath interno
     */
    protected static void addURL(URL url) throws ProjectException {
        log.debug("Agregando el recurso '" + url + "' al classpath interno");

        try {
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            Class<?> clazz = URLClassLoader.class;

            Method method = clazz.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(classLoader, new Object[]{url});
        } catch (Exception ex) {
            throw new ProjectException("No se puede agregar recurso " +
                    "'" + url + "' al classpath interno", ex);
        }
    }

    /**
     * Agrega un recurso de un proyecto externo al classpath interno
     * @param resource Archivo donde donde se encuentra el recurso o los recursos.
     *
     * Por ejemplo, para agregar las clases del proyecto c:/Proyect1 al classpath interno,
     * debería agregar la carpeta classes de la siguiente forma:
     * RuntimeClassPath.addFile(new File("c:/Proyect1/classes"));
     * lo cual permitiría crear objetos del proyecto externo en tiempo de ejecución.
     * Nota: No se pueden agregar archivos .class de forma individual, debe ser
     * toda la carpeta classes del proyecto externo.
     *
     * Otro ejemplo, para agregar el archivo c:/Proyect1/conf/app-context.xml al
     * classpath interno, deberá hacer algo como esto:
     * RuntimeClassPath.addFile(new File("c:/Proyect1/conf/app-context.xml"));
     * y a partir de ese momento lo tendrá a disposición.
     *
     * @throws ProjectException: En caso que no exista el recurso o
     * no se puede agregar al classpath interno.
     */
    protected static void addResource(File resource) throws ProjectException {
        if (!resource.exists()) {
            throw new ProjectException("No se pudo agregar el recurso " + "al " +
                    "classpath interno ya que no existe");
        }

        try {
            addURL(resource.toURI().toURL());
        } catch (MalformedURLException ex) {
            throw new ProjectException("No se pudo agregar el recurso al classpath interno", ex);
        }
    }

    /**
     * @see addResource(File resource)
     * @param resource
     * @throws ProjectException
     */
    protected static void addResource(String resource) throws ProjectException {
        addResource(new File(resource));
    }

    protected static void addJProject(JProject project) {
        log.debug("Introduciendo el proyecto '" + project.getPath() + "' al classpath interno");
        InternalClassPath.addResource(project.getPath() + File.separator + project.getClassesPath());
    }
}