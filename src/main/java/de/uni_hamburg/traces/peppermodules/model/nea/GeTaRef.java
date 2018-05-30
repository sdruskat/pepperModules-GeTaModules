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
package de.uni_hamburg.traces.peppermodules.model.nea;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * An object representation of a JSON object from a GeTa *NEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaRef {
	
	private String wid;
	private List<String> tid;

	/**
	 * Constructor creating a JSON representation.
	 * 
	 * @param wid
	 * @param tid
	 */
	public GeTaRef(@JsonProperty(GeTaMapper.WId) String wid,
			@JsonProperty(GeTaMapper.TID) List<String> tid) {
		this.wid = wid;
		this.tid = tid;
	}

	/**
	 * @return the wid
	 */
	public final String getWid() {
		return wid;
	}

	/**
	 * @return the tid
	 */
	public final List<String> getTid() {
		return tid;
	}

}
