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

/**
 * A bean representing an **LL** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaLL {
	
	private String t, nr, l, tid, lat;

	/**
	 * @return the t
	 */
	public final String getT() {
		return t;
	}

	/**
	 * @param t the t to set
	 */
	public final void setT(String t) {
		this.t = t;
	}

	/**
	 * @return the nr
	 */
	public final String getNR() {
		return nr;
	}

	/**
	 * @param nr the nr to set
	 */
	public final void setNR(String nr) {
		this.nr = nr;
	}

	/**
	 * @return the l
	 */
	public final String getL() {
		return l;
	}

	/**
	 * @param l the l to set
	 */
	public final void setL(String l) {
		this.l = l;
	}

	/**
	 * @return the tid
	 */
	public final String getTID() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public final void setTID(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the lat
	 */
	public final String getLAT() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public final void setLAT(String lat) {
		this.lat = lat;
	}

}
