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

import org.corpus_tools.pepper.impl.PepperImporterImpl;
import org.corpus_tools.pepper.modules.PepperImporter;
import org.corpus_tools.pepper.modules.PepperMapper;
import org.corpus_tools.salt.graph.Identifier;
import org.eclipse.emf.common.util.URI;
import org.osgi.service.component.annotations.Component;

/**
 * The main importer class which is responsible for setting up
 * metadata and creating the actual {@link PepperMapper}.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
@Component(name = "GeTaImporterComponent", factory = "PepperImporterComponentFactory")
public class GeTaImporter extends PepperImporterImpl implements PepperImporter{
	
	public GeTaImporter() {
		super();
		setName("GeTaImporter");
		setSupplierContact(URI.createURI("mail@sdruskat.net"));
		setSupplierHomepage(URI.createURI("http://sdruskat.net/traces-importer"));
		setDesc("An importer for the custom JSON-based format used by the GeTa research project (https://www.traces.uni-hamburg.de/).");
		addSupportedFormat("traces-json", "1.0", null);
		getDocumentEndings().add("json");
	}
	
	/* 
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperModuleImpl#createPepperMapper(org.corpus_tools.salt.graph.Identifier)
	 */
	@Override
	public PepperMapper createPepperMapper(Identifier Identifier) {
		GeTaMapper mapper = new GeTaMapper();
		mapper.setResourceURI(getIdentifier2ResourceTable().get(Identifier));
		return (mapper);
	}

	/* 
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperImporterImpl#isImportable(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public Double isImportable(URI corpusPath) {
		// TODO some code to analyze the given corpus-structure
		return (null);
	}

}
