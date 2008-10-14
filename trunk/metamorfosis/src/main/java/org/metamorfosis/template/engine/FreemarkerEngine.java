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
package org.metamorfosis.template.engine;

import org.metamorfosis.model.GroupTemplatesDef;
import org.metamorfosis.model.GroupTemplatesMatch;
import org.metamorfosis.model.TemplateDef;
import org.metamorfosis.template.match.MatchException;
import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.metamorfosis.model.MetaClass;
import org.metamorfosis.model.MetaProperty;
import org.metamorfosis.model.SingleTemplateMatch;
import org.metamorfosis.model.project.ExternalProject;
import org.metamorfosis.template.directive.TemplateDirective;
import org.metamorfosis.template.wrapper.EngineWrappersFactory;
import org.metamorfosis.template.wrapper.MetaPropertyWrapper;
import org.metamorfosis.template.wrapper.freemarker.FreemarkerWrappersFactory;
import org.metamorfosis.conf.SpringUtils;
import org.metamorfosis.conf.ProjectHolder;

/**
 *
 * @author iberck
 */
public class FreemarkerEngine implements TemplateEngine {

    private static final Log log = LogFactory.getLog(FreemarkerEngine.class);
    private BeansWrapper bw_instance;
    private Map projectWrapped;
    private Map directivesWrapped;
    private Configuration cfg;
    private EngineWrappersFactory engineWrappersFactory;


    {
        bw_instance = SimpleObjectWrapper.getDefaultInstance();
        //bw_instance.setMethodsShadowItems(false);
        bw_instance.setUseCache(true);
    }

    public FreemarkerEngine() {
        engineWrappersFactory = new FreemarkerWrappersFactory();
    }

    @Override
    public EngineWrappersFactory getEngineWrappersFactory() {
        return engineWrappersFactory;
    }

    @Override
    public void match(SingleTemplateMatch templateModel) {
        try {
            // create model root
            Map root = new HashMap();

            // put directives
            root.putAll(directivesWrapped);

            // ${project}
            root.putAll(projectWrapped);

            // ${metapojos}
            List<MetaClass> metaPojos = templateModel.getMetaPojos();
            if (metaPojos != null) {
                Object metaPojosWrapped = getEngineWrappersFactory().getMetaPojosWrapper().wrap(metaPojos);
                root.putAll((Map) metaPojosWrapped);
            }

            // ${metaproperty}
            MetaProperty metaProperty = templateModel.getMetaProperty();
            if (metaProperty != null) {
                MetaPropertyWrapper metaPropertyWrapper = getEngineWrappersFactory().getMetaPropertyWrapper();
                Object metaPropertyWrapped = metaPropertyWrapper.wrap(metaProperty);
                root.putAll((Map) metaPropertyWrapped);
            }

            // process template
            TemplateDef templateDef = templateModel.getTemplateDef();
            InputStream is = templateDef.getLocation().getInputStream();
            InputStreamReader reader = new InputStreamReader(is);
            Template template = new Template(templateDef.getName(), reader, cfg);

            StringWriter sw = new StringWriter();
            Environment env = template.createProcessingEnvironment(root, sw);
            env.process(); // process the template
            sw.close();
        } catch (TemplateException ex) {
            String templateName = templateModel.getTemplateDef().getName();
            throw new MatchException("No se pudo hacer match del template '" +
                    templateName + "' por un error en la definición del template", ex);
        } catch (IOException ex) {
            String templateName = templateModel.getTemplateDef().getName();
            throw new MatchException("No se pudo hacer match del template '" +
                    templateName + "' por un error de i/o", ex);
        }
    }

    @Override
    public void setUpEnvironment(ExternalProject project) {
        log.info("Setting up freemarker environment");

        project.setProjectWrapperFactory(
                getEngineWrappersFactory().getProjectWrapperFactory());
        projectWrapped = (Map) project.getProjectWrapper().wrap(project);

        ProjectHolder projectHolder = (ProjectHolder) SpringUtils.getBean("projectHolder");
        projectHolder.setProject(project);

        // setup engine
        cfg = new Configuration();
        //cfg.setDirectoryForTemplateLoading(tmp);
        cfg.setObjectWrapper(bw_instance);

        setUpDirectives();
    }

    @Override
    public void setUpDirectives() {
        log.info("Setting up freemarker directives");

        directivesWrapped = new HashMap();

        TemplateDirective[] fmDirectives = SpringUtils.getFreemarkerDirectives();
        for (TemplateDirective directive : fmDirectives) {
            directivesWrapped.put(directive.getDirectiveName(), directive);
        }
    }

    @Override
    public void match(GroupTemplatesMatch groupTemplatesMatch) {
        // create model
        Map root = new HashMap();

        // put directives
        root.putAll(directivesWrapped);

        // ${project}
        root.putAll(projectWrapped);

        // ${metapojos}
        List<MetaClass> metaPojos = groupTemplatesMatch.getMetaPojos();
        if (metaPojos != null) {
            Object metaPojosWrapped = getEngineWrappersFactory().getMetaPojosWrapper().wrap(metaPojos);
            root.putAll((Map) metaPojosWrapped);
        }

        // ${metaproperty}
        MetaProperty metaProperty = groupTemplatesMatch.getMetaProperty();
        if (metaProperty != null) {
            MetaPropertyWrapper metaPropertyWrapper = getEngineWrappersFactory().getMetaPropertyWrapper();
            Object metaPropertyWrapped = metaPropertyWrapper.wrap(metaProperty);
            root.putAll((Map) metaPropertyWrapped);
        }

        // process templates
        GroupTemplatesDef groupTemplatesDef = groupTemplatesMatch.getGroupTemplatesDef();
        List<TemplateDef> templatesDef = groupTemplatesDef.getTemplatesDef();
        for (TemplateDef templateDef : templatesDef) {
            try {
                InputStream is = templateDef.getLocation().getInputStream();
                InputStreamReader reader = new InputStreamReader(is);
                Template template = new Template(templateDef.getName(), reader, cfg);

                StringWriter sw = new StringWriter();
                Environment env = template.createProcessingEnvironment(root, sw);
                env.process(); // process the template
                sw.close();
            } catch (TemplateException ex) {
                String templateName = templateDef.getName();
                throw new MatchException("No se pudo procesar el grupo de templates '" +
                        groupTemplatesDef.getGroupName() + "' ya que al hacer match del template '" +
                        templateName + "' ocurrio error en la definición", ex);
            } catch (IOException ex) {
                String templateName = templateDef.getName();
                throw new MatchException("No se pudo procesar el grupo de templates '" +
                        groupTemplatesDef.getGroupName() + "' ya que al hacer match del template '" +
                        templateName + "' ocurrio error de i/o", ex);
            }
        }
    }
}
