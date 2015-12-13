package com.midi_automator.tests;

import static com.midi_automator.tests.utils.GUIAutomations.getInfoLabelText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.sikuli.script.Screen;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.midi_automator.AppConfig;
import com.midi_automator.presenter.Messages;
import com.midi_automator.presenter.MidiAutomator;
import com.midi_automator.tests.utils.GUIAutomations;
import com.midi_automator.tests.utils.MockUpUtils;
import com.midi_automator.view.frames.MainFrame;

public class GUITestCase extends AssertJSwingJUnitTestCase {

	@Rule
	public TestName name = new TestName();

	protected FrameFixture window;
	protected String currentPath;
	protected Screen screen;
	protected AnnotationConfigApplicationContext ctx;

	@Before
	public void log() {
		System.out.println();
		System.out.println("Running Test: " + this.getClass().getSimpleName()
				+ " - " + name.getMethodName());
		System.out
				.println("====================================================");
	}

	/**
	 * Starts the application
	 */
	protected void startApplication() {

		// TODO: New Screen() has to be called once outside the EDT. Maybe we
		// can get rid of that as soon as the application is "EDT" proof.
		// https://weblogs.java.net/blog/alexfromsun/archive/2006/02/debugging_swing.html
		screen = new Screen(0);

		MainFrame mainFrame = GuiActionRunner
				.execute(new GuiQuery<MainFrame>() {
					protected MainFrame executeInEDT() {

						ctx = new AnnotationConfigApplicationContext(
								AppConfig.class);

						MidiAutomator presenter = (MidiAutomator) ctx
								.getBean(MidiAutomator.class);

						return presenter.openMainFrame();
					}
				});

		window = new FrameFixture(robot(), mainFrame);

		GUIAutomations.window = window;
		GUIAutomations.robot = robot();

		try {
			currentPath = new File(".").getCanonicalPath();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Checks if the entry was opened
	 * 
	 * @param entryName
	 *            The name of the entry
	 */
	protected void checkIfEntryWasOpened(String entryName) {
		checkInfoText(String.format(Messages.MSG_OPENING_ENTRY, entryName));
	}

	/**
	 * Checks for a specific text in the info
	 * 
	 * @param text
	 *            The text to check for
	 */
	protected void checkInfoText(String text) {
		String infoMsg = text.replace("\"", "&quot;").replace("Ä", "&#196;")
				.replace("Ö", "&#214;").replace("Ü", "&#220;");
		assertThat(getInfoLabelText(), containsString(infoMsg));
	}

	@After
	public void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSetUp() {

	}

	@Override
	protected void onTearDown() {
		MockUpUtils.recoverMidoBackup();
		MockUpUtils.recoverPropertiesBackup();
	}

	/**
	 * Checks that the info text is empty
	 */
	protected void checkEmptyInfoText() {
		assertEquals("<html> <head> </head> <body> </body> </html> ",
				getInfoLabelText());
	}
}
