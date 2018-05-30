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
package de.uni_hamburg.traces.peppermodules.model.tea;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * An object representation of a JSON object from a GeTa *TEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaTEA {
	
	
	private String id;
	private final Map<String, String> annotations = new HashMap<>();
	private GeTaM m;

	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param id
	 * @param tokl
	 * @param neid
	 * @param m
	 */
	@JsonCreator
	public GeTaTEA(@JsonProperty(GeTaMapper.Id) String id,
			@JsonProperty(GeTaMapper.TOKL) String tokl,
			@JsonProperty(GeTaMapper.NEId) String neid,
			@JsonProperty(GeTaMapper.M) GeTaM m) {
		this.id = id;
		this.m = m;
		annotations.put(GeTaMapper.TOKL, tokl);
		annotations.put(GeTaMapper.NEId, neid);
		// Remove null values from map
		Iterables.removeIf(annotations.keySet(), Predicates.isNull());
	}


	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, String> getAnnotations() {
		return annotations;
	}

	/**
	 * @return the m
	 */
	public final GeTaM getM() {
		return m;
	}

			
}