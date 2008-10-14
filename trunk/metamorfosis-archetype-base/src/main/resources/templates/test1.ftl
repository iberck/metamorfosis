<@JFileSection>
package org.metamorfosis.test;

public class Clase1 {
    ${project.srcPath}
    <#list classes as class>
        <#list class.metaClass.properties as property>
            ${property.name}
        </#list>    
    </#list>
}

</@JFileSection>
