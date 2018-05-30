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
package de.uni_hamburg.traces.peppermodules.model.metaea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * An object representation of a GeTa *MetaEA.ann file, which contains
 * metadata for a GeTa dataset.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaMetaEA {
	
	private final Map<String, Object> annotations = new HashMap<>();
	private String id;
	private List<String> parts;
	
	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param id
	 * @param annot
	 * @param soft
	 * @param name
	 * @param lang
	 * @param date
	 * @param edition
	 * @param parts
	 * @param tr
	 * @param scr
	 * @param comm
	 */
	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaMetaEA(@JsonProperty(GeTaMapper.ID) String id,
			@JsonProperty(GeTaMapper.ANNOT) String annot,
			@JsonProperty(GeTaMapper.SOFT) String soft,
			@JsonProperty(GeTaMapper.NAME) String name,
			@JsonProperty(GeTaMapper.LANG) String lang,
			@JsonProperty(GeTaMapper.DATE) String date,
			@JsonProperty(GeTaMapper.EDITION) String edition,
			@JsonProperty(GeTaMapper.PARTS) List<String> parts,
			@JsonProperty(GeTaMapper.TR) Integer tr,
			@JsonProperty(GeTaMapper.SCR) Integer scr,
			@JsonProperty(GeTaMapper.Comm) String comm) {
		this.id = id;
		this.parts = parts;
		annotations.put(GeTaMapper.ANNOT, annot);
		annotations.put(GeTaMapper.SOFT, soft);
		annotations.put(GeTaMapper.NAME, name);
		annotations.put(GeTaMapper.LANG, lang);
		annotations.put(GeTaMapper.DATE, date);
		annotations.put(GeTaMapper.EDITION, edition);
		annotations.put(GeTaMapper.Comm, comm);
		// Remove null values from map
		Iterables.removeIf(annotations.keySet(), Predicates.isNull());
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, Object> getAnnotations() {
		return annotations;
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @return the parts
	 */
	public final List<String> getParts() {
		return parts;
	}

}
