package com.copel.icl.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.copel.monitor.Monitor;
import com.copel.monitor.servlet.MonitorServlet;

@Configuration
public class MonitorConfig {
	
	@Autowired
	private Monitor monitor;
	
	@Bean
	public ServletRegistrationBean exampleServletBean() {
	    ServletRegistrationBean bean = new ServletRegistrationBean(
	      new MonitorServlet(monitor), "/monitor");
	    bean.setLoadOnStartup(1);
	    bean.addInitParameter("intervalo", "30000" );
	    return bean;
	}
	
	@Bean
	public Monitor initMonitor() {
		return new MonitorImpl();
	}

}
