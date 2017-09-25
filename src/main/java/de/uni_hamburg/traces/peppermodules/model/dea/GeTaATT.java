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

import java.util.ArrayList;
import java.util.List;

/**
 * A bean representing an **Att** section in GeTa *DEA.ann files.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaATT {
	
	private List<String[]> nvs = new ArrayList<>();

	/**
	 * Returns a list of key-value pairs for annotations.
	 * 
	 * @return the nvs
	 */
	public final List<String[]> getNVs() {
		return nvs;
	}

}
