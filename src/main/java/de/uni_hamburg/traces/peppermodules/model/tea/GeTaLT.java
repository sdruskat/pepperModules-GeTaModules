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

import java.util.ArrayList;
import java.util.List;

/**
 * A bean representing an **LT** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaLT {
	
	private String nt;
	private List<GeTaAL> als = new ArrayList<>();
	/**
	 * @return the nt
	 */
	public final String getNT() {
		return nt;
	}
	/**
	 * @param nt the nt to set
	 */
	public final void setNT(String nt) {
		this.nt = nt;
	}
	/**
	 * @return the als
	 */
	public final List<GeTaAL> getALs() {
		return als;
	}

}
