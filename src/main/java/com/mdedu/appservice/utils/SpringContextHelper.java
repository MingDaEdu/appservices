package com.mdedu.appservice.utils;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextHelper {

	    private ApplicationContext context;
	    public SpringContextHelper(ServletContext servletContext) {
	        /*ServletContext servletContext = 
	                ((WebApplicationContext) application.getContext())
	                .getHttpSession().getServletContext();*/
	        context = WebApplicationContextUtils.
	                getRequiredWebApplicationContext(servletContext);
	    }

	    public Object getBean(final String beanRef) {
	        return context.getBean(beanRef);
	    }    
	    
	    public <T> T getBean(Class<T> clazz){
	    	return context.getBean(clazz);
	    }
	}
