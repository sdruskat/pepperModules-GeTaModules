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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
	private List<String> tid;
	private final Map<String, String> annotations = new HashMap<>();
	private List<GeTaRefWord> ref;
	private List<GeTaAL> feat;

	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true) // FIXME: Delete once everything is included!
	public GeTaNEA(@JsonProperty(GeTaMapper.ID) String id,
			@JsonProperty(GeTaMapper.T) String t,
			@JsonProperty(GeTaMapper.REF) List<GeTaRefWord> ref,
			@JsonProperty(GeTaMapper.feat) List<GeTaAL> feat,
			@JsonProperty(GeTaMapper.WID) String wid,
			@JsonProperty(GeTaMapper.TID) List<String> tid) {
		this.id = id;
		this.tid = tid;
		this.ref = ref;
		this.feat = feat;
		annotations.put(GeTaMapper.T, t);
		annotations.put(GeTaMapper.WID, wid);
		// Remove null values from map
		Iterables.removeIf(annotations.values(), Predicates.isNull());
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @return the tid
	 */
	public final List<String> getTid() {
		return tid;
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
	public final List<GeTaRefWord> getRef() {
		return ref;
	}

	/**
	 * @return the feat
	 */
	public final List<GeTaAL> getFeat() {
		return feat;
	}

}
