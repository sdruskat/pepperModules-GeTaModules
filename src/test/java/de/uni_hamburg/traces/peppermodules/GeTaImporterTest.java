package de.uni_hamburg.traces.peppermodules;

import de.uni_hamburg.traces.peppermodules.GeTaImporter; 

import org.corpus_tools.pepper.common.FormatDesc;
import org.corpus_tools.pepper.testFramework.PepperImporterTest;
import org.junit.Before;

/**
 * This is a dummy implementation of a JUnit test for testing the {@link GeTaImporter} class. Feel free to adapt and enhance this test class for real tests to check the work of your importer. If you are not confirm with JUnit, please have
 * a look at <a href="http://www.vogella.com/tutorials/JUnit/article.html"> http://www.vogella.com/tutorials/JUnit/article.html</a>. <br/>
 * Please note, that the test class is derived from {@link PepperImporterTest}. The usage of this class should simplfy your work and allows you to test only your single importer in the Pepper environment.
 * 
 * @author Stephan Druskat
 */
public class GeTaImporterTest extends PepperImporterTest {
	/**
	 * This method is called by the JUnit environment each time before a test case starts. So each time a method annotated with @Test is called. This enables, that each method could run in its own environment being not influenced by before
	 * or after running test cases.
	 */
	@Before
	public void setUp() {
		setFixture(new GeTaImporter());
		FormatDesc formatDef = new FormatDesc();
		formatDef.setFormatName("traces-json");
		formatDef.setFormatVersion("1.0");
	}

}
