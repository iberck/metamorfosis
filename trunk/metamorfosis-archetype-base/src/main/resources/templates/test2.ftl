<@AppendFileSection filePath="/resources/applicationContext.xml"
                    position="before"
                    ocurrenceCount="last"
                    ocurrence="}" >

${project.name}

<#list classes as clase>
    <#list clase.metaClass.properties as property>
        ${property.tyspe}, ${property.name}
    </#list>
</#list>

</@AppendFileSection>
