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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * A bean representing an **LT** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaLT {
	
	private String nt;
	private List<GeTaAL> al;

	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param nt
	 * @param al
	 */
	@JsonCreator
	public GeTaLT(@JsonProperty(GeTaMapper.NT) String nt,
			@JsonProperty(GeTaMapper.AL) List<GeTaAL> al) {
				this.nt = nt;
				this.al = al;
	}

	/**
	 * @return the nt
	 */
	public final String getNt() {
		return nt;
	}

	/**
	 * @return the al
	 */
	public final List<GeTaAL> getAl() {
		return al;
	}
}
