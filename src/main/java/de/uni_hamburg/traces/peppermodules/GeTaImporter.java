/**
 * Copyright 2016ff. Stephan Druskat
 * All exploitation rights belong exclusively to Universität Hamburg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Stephan Druskat - initial API and implementation
 */
package de.uni_hamburg.traces.peppermodules;

import java.io.File; 
import java.io.IOException;
import org.corpus_tools.pepper.impl.PepperImporterImpl;
import org.corpus_tools.pepper.modules.PepperImporter;
import org.corpus_tools.pepper.modules.PepperMapper;
import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;
import org.corpus_tools.salt.SALT_TYPE;
import org.corpus_tools.salt.common.SCorpus;
import org.corpus_tools.salt.common.SDocument;
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
	
	/**
	 * No-args constructor setting some basic values.
	 */
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
	public PepperMapper createPepperMapper(Identifier identifier) {
		GeTaMapper mapper = new GeTaMapper();
		mapper.setResourceURI(getIdentifier2ResourceTable().get(identifier));
		return (mapper);
	}

	/* 
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperImporterImpl#importCorpusStructureRec(org.eclipse.emf.common.util.URI, org.corpus_tools.salt.common.SCorpus)
	 */
	/**
	 * Overrides org.corpus_tools.pepper.impl.PepperImporterImpl#importCorpusStructureRec(org.eclipse.emf.common.util.URI, org.corpus_tools.salt.common.SCorpus)
	 * to add specific files to ignore, i.e., all GeTa annotation files
	 * that aren't the base annotation file. 
	 */
	@Override
	protected Boolean importCorpusStructureRec(URI currURI, SCorpus parent) {
		Boolean retVal = false;

		// set name for corpus graph
		if ((this.getCorpusGraph().getName() == null) || (this.getCorpusGraph().getName().isEmpty())) {
			this.getCorpusGraph().setName(currURI.lastSegment());
		}

		boolean ignoreAnnotationFile = currURI.toString().endsWith("TEA.json")
				|| currURI.toString().endsWith("DEA.json")
				|| currURI.toString().endsWith("NEA.json")
				|| currURI.toString().endsWith("QEA.json")
				|| currURI.toString().endsWith("MetaEA.json");
		
		if ((currURI.lastSegment() != null) && (!this.getIgnoreEndings().contains(currURI.lastSegment())) && !ignoreAnnotationFile) {// if
			SALT_TYPE type = this.setTypeOfResource(currURI);
			if (type != null) {
				// do not ignore resource create new id
				File currFile = new File(currURI.toFileString());

				if (SALT_TYPE.SCORPUS.equals(type)) {
					// resource is a SCorpus create corpus
					SCorpus sCorpus = getCorpusGraph().createCorpus(parent, currURI.lastSegment());
					this.getIdentifier2ResourceTable().put(sCorpus.getIdentifier(), currURI);
					if (currFile.isDirectory()) {
						File[] files = currFile.listFiles();
						if (files != null) {
							for (File file : files) {
								try {
									// if retval is true or returned value is
									// true
									// set retVal to true
									Boolean containsDocuments = importCorpusStructureRec(
											URI.createFileURI(file.getCanonicalPath()), sCorpus);
									retVal = (retVal || containsDocuments);
								} catch (IOException e) {
									throw new PepperModuleException(
											"Cannot import corpus structure, because cannot create a URI out of file '"
													+ file + "'. ",
											e);
								}
							}
						}
					}
				} // resource is a SCorpus
				else if (SALT_TYPE.SDOCUMENT.equals(type)) {
					retVal = true;
					// resource is a SDocument
					if (parent == null) {
						// if there is no corpus given, create one with name of
						// document
						parent = getCorpusGraph().createCorpus(null,
								currURI.lastSegment().replace("." + currURI.fileExtension(), ""));

						this.getIdentifier2ResourceTable().put(parent.getIdentifier(), currURI);
					}
					File docFile = new File(currURI.toFileString());
					SDocument sDocument = null;
					if (docFile.isDirectory()) {
						sDocument = getCorpusGraph().createDocument(parent, currURI.lastSegment());
					} else {
						// if uri is a file, cut off file ending
						sDocument = getCorpusGraph().createDocument(parent,
								currURI.lastSegment().replace("." + currURI.fileExtension(), ""));
					}
					// link documentId with resource
					this.getIdentifier2ResourceTable().put(sDocument.getIdentifier(), currURI);
				} // resource is a SDocument
			} // do not ignore resource
		} // if file is not part of ignore list
		return (retVal);
	}

}
