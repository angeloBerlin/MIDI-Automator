package com.midi_automator.tests;

import java.io.File;
import java.io.IOException;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.midi_automator.Resources;
import com.midi_automator.model.IModel;
import com.midi_automator.model.Model;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.tests.utils.AssertJGUIAutomations;
import com.midi_automator.view.frames.MainFrame;

public class GUITestCase extends AssertJSwingJUnitTestCase {

	protected FrameFixture window;
	protected String currentPath;

	private static String fileName = null;
	private static String wd = "";
	private static String os = "";
	private static boolean test = false;

	@Rule
	public TestName name = new TestName();

	@Override
	public void onSetUp() {

		MainFrame mainFrame = GuiActionRunner
				.execute(new GuiQuery<MainFrame>() {
					protected MainFrame executeInEDT() {

						ApplicationContext ctx = new ClassPathXmlApplicationContext(
								"Beans.xml");
						Resources resources = (Resources) ctx.getBean(
								"Resources", new Object[] { os, wd });
						IModel model = (Model) ctx.getBean("Model");

						if (fileName == null) {
							fileName = model.getPersistenceFileName();
						}

						MidiAutomator midiAutomator = (MidiAutomator) ctx
								.getBean("Presenter", new Object[] { model,
										resources, fileName, test });
						return midiAutomator.getProgramFrame();
					}
				});
		window = new FrameFixture(mainFrame);
		System.out.println("hallo");
		window.show(); // shows the frame to test
		AssertJGUIAutomations.window = window;
		AssertJGUIAutomations.robot = robot();

		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Before
	public void log() {
		System.out.println();
		System.out.println("Running Test: " + this.getClass().getSimpleName()
				+ " - " + name.getMethodName());
		System.out
				.println("====================================================");
	}
}
