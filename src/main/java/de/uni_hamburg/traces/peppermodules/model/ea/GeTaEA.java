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

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * An object representation of a GeTa .json file, which contains
 * *GeTa word tokens* (Fidel script).
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaEA {
	
	private Integer scr;
	private Integer tr;
	private List<GeTaFidalword> fidalwords;

	/**
	 * This is a constructor working as a {@link JsonCreator}, i.e.,
	 * the mapping of {@link GeTaEA} objects starts here.
	 * 
	 * @param scr
	 * @param tr
	 * @param wordObjects
	 */
	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaEA(@JsonProperty(GeTaMapper.TR) Integer tr, 
			@JsonProperty(GeTaMapper.SCR) Integer scr, 
			@JsonProperty(GeTaMapper.FIDALWORDS) List<GeTaFidalword> fidalwords) {
		this.scr = scr;
		this.tr = tr;
		this.fidalwords = fidalwords;
	}

	/**
	 * @return the scr
	 */
	public final Integer getSCR() {
		return scr;
	}

	/**
	 * @return the tr
	 */
	public final Integer getTR() {
		return tr;
	}

	/**
	 * @return the fidalwords
	 */
	public final List<GeTaFidalword> getFidalwords() {
		return fidalwords;
	}

}
