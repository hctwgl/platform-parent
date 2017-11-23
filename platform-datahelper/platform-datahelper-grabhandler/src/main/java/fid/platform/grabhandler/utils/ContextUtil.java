package fid.platform.grabhandler.utils;

import org.springframework.context.ApplicationContext;

public class ContextUtil {
	private static ApplicationContext context;
    public static ApplicationContext getContext() {
        return context;
    }
    public static void setContext(ApplicationContext aContext) {
        context = aContext;
    }
}

