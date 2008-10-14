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

import org.metamorfosis.template.engine.*;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.metamorfosis.model.SingleTemplateMatch;
import org.metamorfosis.model.project.ExternalProject;
import org.metamorfosis.conf.SpringUtils;
import org.metamorfosis.model.GroupTemplatesMatch;

/**
 * 
 * @author iberck
 */
public class AtomicTemplatesMatcher {

    private static final Log log = LogFactory.getLog(AtomicTemplatesMatcher.class);
    private TemplateEngine engine;
    private List<SingleTemplateMatch> singleTemplatesMatch;
    private List<GroupTemplatesMatch> groupTemplatesMatch;
    private ExternalProject project;
    private TemplatesWriterPool templatesWriterPool;

    public AtomicTemplatesMatcher() {
        templatesWriterPool = (TemplatesWriterPool) SpringUtils.getBean("templatesWriterPool");
    }

    public TemplateEngine getEngine() {
        return engine;
    }

    @Required
    public void setEngine(TemplateEngine engine) {
        this.engine = engine;
    }

    public List<SingleTemplateMatch> getSingleTemplatesMatch() {
        return singleTemplatesMatch;
    }

    public void setSingleTemplatesMatch(List<SingleTemplateMatch> singleTemplatesMatch) {
        this.singleTemplatesMatch = singleTemplatesMatch;
    }

    public List<GroupTemplatesMatch> getGroupTemplatesMatch() {
        return groupTemplatesMatch;
    }

    public void setGroupTemplatesMatch(List<GroupTemplatesMatch> groupTemplatesMatch) {
        this.groupTemplatesMatch = groupTemplatesMatch;
    }

    public ExternalProject getProject() {
        return project;
    }

    @Required
    public void setProject(ExternalProject project) {
        this.project = project;
    }

    private void match() {
        log.info("Realizando el match entre plantillas y modelos");

        if (singleTemplatesMatch == null && groupTemplatesMatch == null) {
            throw new IllegalArgumentException("No se definido ningún templatesMatch ni groupsMatch");
        }

        // procesar plantillas simples
        if (singleTemplatesMatch != null) {
            log.info("Single template match");
            for (SingleTemplateMatch tModel : singleTemplatesMatch) {
                log.info("Realizando el match de: " + tModel);
                engine.match(tModel);
            }
        }

        // procesar grupos de plantillas
        if (groupTemplatesMatch != null) {
            log.info("Group template match");
            for (GroupTemplatesMatch gtm : groupTemplatesMatch) {
                engine.match(gtm);
            }
        }
    }

    private void writeAtomically() {
        try {
            log.info("Iniciando transacción");
            templatesWriterPool.writeTemplates(project);
            log.info("transacción ejecutada exitosamente");
        } catch (Exception ex) {
            log.error("Error al escribir los archivos generados", ex);
            templatesWriterPool.rollbackTemplates(project);
        }
    }

    public void matchAndWrite() {
        // inicializar el engine
        engine.setUpEnvironment(project);

        // hacer el match de los templates y crear templates procesados
        match();

        // escribir los templates procesados de forma atómica
        writeAtomically();
    }
}
