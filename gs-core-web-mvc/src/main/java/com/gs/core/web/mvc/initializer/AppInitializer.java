package com.gs.core.web.mvc.initializer;

import com.gs.core.web.mvc.TomcatServer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * author: linjuntan
 * date: 2018/2/22
 */
public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(TomcatServer.scanPack);
        servletContext.addListener(new ContextLoaderListener(context));

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8", true);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("spring", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");

        FilterRegistration.Dynamic encodingFilter =  servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");

        if (TomcatServer.openTokenFilter) {
            FilterRegistration.Dynamic loginTokenFilter =  servletContext.addFilter("loginTokenFilter", characterEncodingFilter);
            loginTokenFilter.addMappingForUrlPatterns(null, false, "/*");
        }

        if (TomcatServer.customFilters != null && TomcatServer.customFilters.size() > 0) {
            TomcatServer.customFilters.forEach(e -> servletContext.addFilter(e, org.springframework.web.filter.DelegatingFilterProxy.class)
                    .addMappingForUrlPatterns(null, false, "/*"));
        }
    }
}
