/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamorfosis.template.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author iberck
 */
public class FreemarkerEngineTest extends TestCase {

    private static final Log log = LogFactory.getLog(FreemarkerEngineTest.class);

    public void testCreateTemplateFromReader() throws IOException {
        Configuration cfg = new Configuration();
        ClassPathResource resource = new ClassPathResource("templates/internalTemplate.ftl");
        InputStream is = resource.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);

        Template template = new Template("nombreTemplate", reader, cfg);
        assertEquals("nombreTemplate", template.getName());
        log.debug("template: " + template);
    }
}
