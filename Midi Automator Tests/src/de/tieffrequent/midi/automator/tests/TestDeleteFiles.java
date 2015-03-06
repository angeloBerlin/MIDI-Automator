package de.tieffrequent.midi.automator.tests;

import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Test;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Region;

public class TestDeleteFiles extends SikuliTest {

	@Test
	public void allHelloWorldEntriesShouldBeDeleted() {

		Region match = null;
		Iterator<Match> allMatches = null;

		// find all Hello World entries
		try {

			allMatches = SikuliTest.getSearchRegion().findAll(
					screenshotpath + "Hello_World_entry_snippet.png");
		} catch (FindFailed ea) {
			try {
				allMatches = SikuliTest
						.getSearchRegion()
						.findAll(
								screenshotpath
										+ "Hello_World_entry_snippet_active.png");
			} catch (FindFailed eb) {
				try {
					allMatches = SikuliTest.getSearchRegion().findAll(
							screenshotpath
									+ "Hello_World_entry_snippet_inactive.png");
				} catch (FindFailed ec) {
					System.out.println("No entries in list");
				}
			}
		}

		try {
			// delete entries
			if (allMatches != null) {
				while (allMatches.hasNext()) {
					match = allMatches.next();
					match.highlight(HIGHLIGHT_DURATION);

					match.rightClick();
					match = SikuliTest.getSearchRegion().wait(
							screenshotpath + "delete.png", TIMEOUT);
					match.click();

				}
			}
			// check if list is empty
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "midi_automator.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

			// check if delete is inactive
			match.rightClick();
			match = SikuliTest.getSearchRegion().wait(
					screenshotpath + "delete_inactive.png", TIMEOUT);
			match.highlight(HIGHLIGHT_DURATION);

		} catch (FindFailed e) {
			fail(e.toString());
		}
	}
}
