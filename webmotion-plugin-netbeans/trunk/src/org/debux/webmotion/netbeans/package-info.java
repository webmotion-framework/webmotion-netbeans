@TemplateRegistrations({
    @TemplateRegistration(folder = "WebMotion", displayName = "#mapping_template_name",
            content = "template/mapping.wm.template", scriptEngine = "freemarker"),
    
    @TemplateRegistration(folder = "WebMotion", displayName = "#controller_template_name",
            content = "template/controller.java.template", scriptEngine = "freemarker"),
    
    @TemplateRegistration(folder = "WebMotion", displayName = "#filter_template_name",
            content = "template/filter.java.template", scriptEngine = "freemarker")
})
package org.debux.webmotion.netbeans;

import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;
