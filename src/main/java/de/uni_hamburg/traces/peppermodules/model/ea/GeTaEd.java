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

import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;

/**
 * A bean representing an **Ed** section in GeTa data.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaEd {
	
	private String p, aut, c, g, ne;
	private List<GeTaLT> lts = new ArrayList<>();

	/**
	 * @return the p
	 */
	public final String getP() {
		return p;
	}

	/**
	 * @param p
	 *            the p to set
	 */
	public final void setP(String p) {
		this.p = p;
	}

	/**
	 * @return the aut
	 */
	public final String getAUT() {
		return aut;
	}

	/**
	 * @param aut
	 *            the aut to set
	 */
	public final void setAUT(String aut) {
		this.aut = aut;
	}

	/**
	 * @return the c
	 */
	public final String getC() {
		return c;
	}

	/**
	 * @param c
	 *            the c to set
	 */
	public final void setC(String c) {
		this.c = c;
	}

	/**
	 * @return the g
	 */
	public final String getG() {
		return g;
	}

	/**
	 * @param g
	 *            the g to set
	 */
	public final void setG(String g) {
		this.g = g;
	}

	/**
	 * @return the ne
	 */
	public final String getNE() {
		return ne;
	}

	/**
	 * @param ne
	 *            the ne to set
	 */
	public final void setNE(String ne) {
		this.ne = ne;
	}

	/**
	 * @return the lts
	 */
	public final List<GeTaLT> getLTs() {
		return lts;
	}

}
