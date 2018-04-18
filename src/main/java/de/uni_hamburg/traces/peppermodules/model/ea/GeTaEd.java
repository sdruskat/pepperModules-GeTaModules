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
package de.uni_hamburg.traces.peppermodules.model.ea;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;

/**
 * TODO Description
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaEd {
	
	private List<GeTaLT> lt;

	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaEd(@JsonProperty(GeTaMapper.LT) List<GeTaLT> lt) {
		this.lt = lt;
	}

	/**
	 * @return the lt
	 */
	public final List<GeTaLT> getLt() {
		return lt;
	}

}
