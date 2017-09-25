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

import java.util.ArrayList;
import java.util.List;

/**
 * A bean representing an **FC** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaFC {

	private final List<GeTaLL> lls = new ArrayList<>();
	private final List<GeTaEd> eds = new ArrayList<>();
	private String nr, end, ends, trfid, fidleted, fidlet;

	/**
	 * @return the nr
	 */
	public final String getNR() {
		return nr;
	}

	/**
	 * @param nr
	 *            the nr to set
	 */
	public final void setNR(String nr) {
		this.nr = nr;
	}

	/**
	 * @return the end
	 */
	public final String getEND() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public final void setEND(String end) {
		this.end = end;
	}

	/**
	 * @return the ends
	 */
	public final String getENDS() {
		return ends;
	}

	/**
	 * @param ends
	 *            the ends to set
	 */
	public final void setENDS(String ends) {
		this.ends = ends;
	}

	/**
	 * @return the lls
	 */
	public final List<GeTaLL> getLLs() {
		return lls;
	}

	/**
	 * @return the trfid
	 */
	public final String getTRFID() {
		return trfid;
	}

	/**
	 * @param trfid
	 *            the trfid to set
	 */
	public final void setTRFID(String trfid) {
		this.trfid = trfid;
	}

	/**
	 * @return the fidleted
	 */
	public final String getFIDLETED() {
		return fidleted;
	}

	/**
	 * @param fidleted
	 *            the fidleted to set
	 */
	public final void setFIDLETED(String fidleted) {
		this.fidleted = fidleted;
	}

	/**
	 * @return the fidlet
	 */
	public final String getFIDLET() {
		return fidlet;
	}

	/**
	 * @param fidlet
	 *            the fidlet to set
	 */
	public final void setFIDLET(String fidlet) {
		this.fidlet = fidlet;
	}

	/**
	 * @return the eds
	 */
	public final List<GeTaEd> getEDs() {
		return eds;
	}

}
