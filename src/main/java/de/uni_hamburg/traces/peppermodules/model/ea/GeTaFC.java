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
package de.uni_hamburg.traces.peppermodules.model.ea;

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
 * A bean representing an **FC** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaFC {
	
	private final Map<String, Object> annotations = new HashMap<>();
	private List<GeTaLL> ll;
	private GeTaEd ed;

	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaFC(@JsonProperty(GeTaMapper.FIDLET) String fidlet,
			@JsonProperty(GeTaMapper.FIDLETED) String fidleted,
			@JsonProperty(GeTaMapper.TRFID) String trfid,
			@JsonProperty(GeTaMapper.pLB) Integer plb,
			@JsonProperty(GeTaMapper.pPB) Integer ppb,
			@JsonProperty(GeTaMapper.Ed) GeTaEd ed,
			@JsonProperty(GeTaMapper.LL) List<GeTaLL> ll) {
				this.ll = ll;
				this.ed = ed;
				annotations.put(GeTaMapper.FIDLET, fidlet);
				annotations.put(GeTaMapper.FIDLETED, fidleted);
				annotations.put(GeTaMapper.TRFID, trfid);
				annotations.put(GeTaMapper.pLB, plb);
				annotations.put(GeTaMapper.pPB, ppb);
				// Remove null values from map
				Iterables.removeIf(annotations.keySet(), Predicates.isNull());
	}

	/**
	 * @return the ll
	 */
	public final List<GeTaLL> getLl() {
		return ll;
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, Object> getAnnotations() {
		return annotations;
	}

	/**
	 * @return the ed
	 */
	public final GeTaEd getEd() {
		return ed;
	}
	
	/**
	 * Whether this FC instance has Ed annotations.
	 *
	 * @return
	 */
	public final boolean hasEd() {
		return ed != null;
	}


}
