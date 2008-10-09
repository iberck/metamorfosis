package org.metamorfosis.main;

import org.metamorfosis.template.match.AtomicTemplatesMatcher;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * @author iberck
 *
 */
public class App {

    public static void main(String[] args) {
        BeanFactory factory = new XmlBeanFactory(
                new ClassPathResource("metamorfosisBeans.xml"));
        AtomicTemplatesMatcher atomicMatcher = (AtomicTemplatesMatcher) factory.getBean("atomicMatcher");
        atomicMatcher.matchAndWrite();
    }
}
