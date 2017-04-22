package com.midi_automator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Holds the applications context, so that it is accessible from everywhere in
 * the application.
 * 
 * @author aguelle
 *
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

	public static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
		context = ctx;
	}
}
