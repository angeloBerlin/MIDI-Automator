package com.midi_automator.tests.IntegrationTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.midi_automator.AppConfig;

public class IntegrationTest {

	@Rule
	public TestName name = new TestName();

	protected AnnotationConfigApplicationContext ctx;

	@Before
	public void log() {
		System.out.println();
		System.out.println("Running Test: " + this.getClass().getSimpleName()
				+ " - " + name.getMethodName());
		System.out
				.println("====================================================");
	}

	@Before
	public void startSpringContext() {
		ctx = new AnnotationConfigApplicationContext(AppConfig.class);
	}

	@After
	public void closeSpringContext() {
		ctx.close();
	}
}
