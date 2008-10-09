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
package org.metamorfosis.conf;

import freemarker.template.TemplateDirectiveModel;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.metamorfosis.template.directive.TemplateDirective;

/**
 *
 * @author iberck
 */
public class SpringUtils {

    private static BeanFactory factory = new XmlBeanFactory(
            new ClassPathResource("application-context.xml"));

    private SpringUtils() {
    }

    public static Object getBean(String name) {
        return factory.getBean(name);
    }

    public static Map getBeans(Class clazz) {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors(
                (ListableBeanFactory) factory, clazz);
    }

    public static TemplateDirective[] getFreemarkerDirectives() {
        Map beans = getBeans(TemplateDirectiveModel.class);
        Collection values = beans.values();
        TemplateDirective[] fmDirectives = new TemplateDirective[values.size()];

        int i = 0;
        for (Object v : values) {
            fmDirectives[i++] = (TemplateDirective) v;
        }

        return fmDirectives;
    }
}
