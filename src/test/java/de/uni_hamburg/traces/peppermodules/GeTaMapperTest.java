/*******************************************************************************
 * Copyright 2016 Universität Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Stephan Druskat - initial API and implementation
 *******************************************************************************/
package de.uni_hamburg.traces.peppermodules;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;

import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SDocument;
import org.eclipse.emf.common.util.URI;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * TODO Description
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaMapperTest {
	
	private GeTaMapper fixture = null;
	
	// FIXME FIX TESTS

	/**
	 * Set up the fixture.
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		GeTaMapper mapper = new GeTaMapper();
		File file = new File(this.getClass().getClassLoader().getResource("testEA.json").getFile());
		String path = file.getAbsolutePath();
		mapper.setResourceURI(URI.createFileURI(path));
		SDocument doc = SaltFactory.createSDocument();
		mapper.setDocument(doc);
		setFixture(mapper);
	}

	/**
	 * Test method for {@link de.uni_hamburg.traces.peppermodules.GeTaMapper#mapSDocument()},
	 * testing if all needed elements exist.
	 */
	@Test
	public void testHasElements() {
		getFixture().mapSDocument();
		assertNotNull(getFixture().getDocument().getDocumentGraph());
		assertEquals(1, getFixture().getDocument().getDocumentGraph().getTextualDSs().size());
		System.err.println(getFixture().getDocument().getDocumentGraph().getNodes());
		assertFalse(getFixture().getDocument().getDocumentGraph().getSpans().isEmpty());
		assertFalse(getFixture().getDocument().getDocumentGraph().getTokens().isEmpty());
	}
	
	@Test
	public void testDocumentHasMetaAnnotations() {
		getFixture().mapSDocument();
		assertNotNull(getFixture().getDocument().getMetaAnnotation("GeTa::SCR"));
		assertNotNull(getFixture().getDocument().getMetaAnnotation("GeTa::TR"));
		assertEquals("SCR", getFixture().getDocument().getMetaAnnotation("GeTa::SCR").getName());
		assertEquals("Ge'ez", getFixture().getDocument().getMetaAnnotation("GeTa::SCR").getValue());
		assertEquals("TR", getFixture().getDocument().getMetaAnnotation("GeTa::TR").getName());
		assertEquals("vocalized", getFixture().getDocument().getMetaAnnotation("GeTa::TR").getValue());
	}

	/**
	 * Test method for {@link de.uni_hamburg.traces.peppermodules.GeTaMapper#mapSCorpus()}.
	 */
	@Test @Ignore
	public void testMapSCorpus() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * @return the fixture
	 */
	private GeTaMapper getFixture() {
		return fixture;
	}

	/**
	 * @param fixture the fixture to set
	 */
	private void setFixture(GeTaMapper fixture) {
		this.fixture = fixture;
	}

}
