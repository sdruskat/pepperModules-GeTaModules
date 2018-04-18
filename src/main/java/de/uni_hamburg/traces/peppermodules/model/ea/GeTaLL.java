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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * A bean representing an **LL** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaLL {
	
	private String lat;
	private String tid;

	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaLL(@JsonProperty(GeTaMapper.LAT) String lat,
			@JsonProperty(GeTaMapper.Tid) String tid) {
				this.lat = lat;
				this.tid = tid;
	}

	/**
	 * @return the lat
	 */
	public final String getLat() {
		return lat;
	}

	/**
	 * @return the tid
	 */
	public final String getTid() {
		return tid;
	}
}
