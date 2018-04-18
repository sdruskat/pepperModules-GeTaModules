/*******************************************************************************
 * Copyright 2017 Universität Hamburg
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
package de.uni_hamburg.traces.peppermodules.model.dea;

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
 * An object representation of a JSON object from a GeTa *DEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaDEA {
	
	final Map<String, String> annotations = new HashMap<>();
	private String id;
	private List<String> dc;
	

	/**
	 * This is a constructor working as a {@link JsonCreator}, i.e.,
	 * the mapping of {@link GeTaTEA} objects starts here.
	 * 
	 * @param id
	 * @param tokl
	 * @param m
	 */
	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true) // FIXME: Delete once everything is included!
	public GeTaDEA(@JsonProperty(GeTaMapper.Id) String id, 
			@JsonProperty(GeTaMapper.WB) String wb,
			@JsonProperty(GeTaMapper.WE) String we, 
			@JsonProperty(GeTaMapper.NRI) String nri, 
			@JsonProperty(GeTaMapper.NR) String nr, 
			@JsonProperty(GeTaMapper.LE) String le, 
			@JsonProperty(GeTaMapper.G) String g, 
			@JsonProperty(GeTaMapper.C) String c, 
			@JsonProperty(GeTaMapper.DP) String dp, 
			@JsonProperty(GeTaMapper. NA) String na, 
			@JsonProperty(GeTaMapper.CR) String cr, 
			@JsonProperty(GeTaMapper. HWB) String hwb, 
			@JsonProperty(GeTaMapper. HWE) String hwe, 
			@JsonProperty(GeTaMapper.DC) List<String> dc) {
		this.id = id;
		this.dc = dc;
		annotations.put(GeTaMapper.Id, id);
		annotations.put(GeTaMapper.WB, wb);
		annotations.put(GeTaMapper.WE, we);
		annotations.put(GeTaMapper.NRI, nri);
		annotations.put(GeTaMapper.NR, nr);
		annotations.put(GeTaMapper.LE, le);
		annotations.put(GeTaMapper.G, g);
		annotations.put(GeTaMapper.C, c);
		annotations.put(GeTaMapper.DP, dp);
		annotations.put(GeTaMapper.NA, na);
		annotations.put(GeTaMapper.CR, cr);
		annotations.put(GeTaMapper.HWB, hwb);
		annotations.put(GeTaMapper.HWE, hwe);
		// Remove null values from map
		Iterables.removeIf(annotations.values(), Predicates.isNull());
	}


	/**
	 * @return the annotations
	 */
	public final Map<String, String> getAnnotations() {
		return annotations;
	}


	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}


	/**
	 * @return the dc
	 */
	public final List<String> getDc() {
		return dc;
	}

}