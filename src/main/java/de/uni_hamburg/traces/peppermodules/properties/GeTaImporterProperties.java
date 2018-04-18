/*******************************************************************************
 * Copyright 2018 Universität Hamburg
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
package de.uni_hamburg.traces.peppermodules.properties;

import org.corpus_tools.pepper.modules.PepperModuleProperties;
import org.corpus_tools.pepper.modules.PepperModuleProperty;

/**
 * TODO Description
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaImporterProperties extends PepperModuleProperties {
	
	private static final long serialVersionUID = -3063309507969354351L;
	
	/**
	 * Whether the metadata should be mapped to annotations instead of meta annotations.
	 */
	public static final String PROP_METAANNOS_IN_ANNOS = "mapMetaToAnnos";
	
	/**
	 * Constructor adding all properties to the instance. 
	 */
	public GeTaImporterProperties() {
		this.addProperty(new PepperModuleProperty<Boolean>(PROP_METAANNOS_IN_ANNOS, Boolean.class, "Whether the metadata should be mapped to annotations instead of meta annotations.", Boolean.TRUE, false));

	}
	
	@SuppressWarnings("javadoc")
	public boolean mapMetadataToAnnotations() {
		return ((Boolean) getProperty(PROP_METAANNOS_IN_ANNOS).getValue());
	}

}
