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
package de.uni_hamburg.traces.peppermodules.model.tea;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * A bean representing an **AL** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaAL {

	private final Map<String, String> annotations = new HashMap<>();


	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param n1
	 * @param v1
	 * @param n2
	 * @param v2
	 * @param n3
	 * @param v3
	 * @param n
	 * @param v
	 */
	@JsonCreator
	public GeTaAL(@JsonProperty(GeTaMapper.N1) String n1, @JsonProperty(GeTaMapper.V1) String v1,
			@JsonProperty(GeTaMapper.N2) String n2, @JsonProperty(GeTaMapper.V2) String v2,
			@JsonProperty(GeTaMapper.N3) String n3, @JsonProperty(GeTaMapper.V3) String v3,
			@JsonProperty(GeTaMapper.N) String n, @JsonProperty(GeTaMapper.V) String v) {
		annotations.put(n1, v1);
		annotations.put(n2, v2);
		annotations.put(n3, v3);
		annotations.put(n, v);
		// Remove null values from map
		Iterables.removeIf(annotations.keySet(), Predicates.isNull());
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, String> getAnnotations() {
		return annotations;
	}

}
