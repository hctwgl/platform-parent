package fid.platform.grabhandler.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fid.platform.grabhandler.utils.ContextUtil;

public class MyContentListener extends ContextLoaderListener {
	
	private static Logger logger = Logger.getLogger(MyContentListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		ContextUtil.setContext(ctx);
		
	}
}
