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
package de.uni_hamburg.traces.peppermodules.model.nea;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;

/**
 * An object representation of a JSON object from a GeTa *NEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaNEA {
	
	private String id;
	private final Map<String, String> annotations = new HashMap<>();
	private List<GeTaRef> ref;
	private List<GeTaAL> feat;
	private String r;

	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param id
	 * @param r
	 * @param t
	 * @param ref
	 * @param feat
	 */
	@JsonCreator
	public GeTaNEA(@JsonProperty(GeTaMapper.Id) String id,
			@JsonProperty(GeTaMapper.R) String r,
			@JsonProperty(GeTaMapper.T) String t,
			@JsonProperty(GeTaMapper.ref) List<GeTaRef> ref,
			@JsonProperty(GeTaMapper.feat) List<GeTaAL> feat) {
		this.id = id;
		this.ref = ref;
		this.feat = feat;
		this.r = r;
		annotations.put(GeTaMapper.T, t);
		annotations.put(GeTaMapper.R, r);
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
	 * @return the ref
	 */
	public final List<GeTaRef> getRef() {
		return ref;
	}

	/**
	 * @return the feat
	 */
	public final List<GeTaAL> getFeat() {
		return feat;
	}

	/**
	 * @return the r
	 */
	public final String getR() {
		return r;
	}

}
