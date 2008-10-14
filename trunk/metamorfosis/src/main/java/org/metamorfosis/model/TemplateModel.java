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
package org.metamorfosis.model;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 *
 * @author iberck
 */
public class TemplateModel {

    private static final Log log = LogFactory.getLog(TemplateModel.class);
    private TemplateDef templateDef;
    private List<MetaClass> metaPojos;
    private MetaProperty metaProperty;

    @Required
    public void setTemplateDef(TemplateDef templateDef) {
        this.templateDef = templateDef;
    }

    public TemplateDef getTemplateDef() {
        return templateDef;
    }

    public List<MetaClass> getMetaPojos() {
        return metaPojos;
    }

    public void setMetaPojos(List<MetaClass> metaPojos) {
        this.metaPojos = metaPojos;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setMetaProperty(MetaProperty metaProperty) {
        this.metaProperty = metaProperty;
    }

    @Override
    public String toString() {
        StringBuilder modelStr = new StringBuilder();
        TemplateDef tDef = getTemplateDef();
        modelStr.append("Template: " + tDef.getName() + " ");

        modelStr.append("Model[{");
        MetaProperty property = getMetaProperty();
        if (property != null) {
            modelStr.append("property:" + property.getValue() + "}, {");
        }

        List<MetaClass> mps = getMetaPojos();
        if (mps != null) {
            for (MetaClass mp : mps) {
                modelStr.append("metaclass:" + mp.toString());
            }
        }
        modelStr.append("}]");

        return modelStr.toString();
    }
}
