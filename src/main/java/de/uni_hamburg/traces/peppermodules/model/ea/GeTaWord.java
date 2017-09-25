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

import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;

/**
 * A bean representing an word section in GeTa .json files.
 * This section contains the annotations for the word
 * token and is parent to all other annotation levels.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaWord {

	private boolean AT;
	private String ID;
	private String COMM;
	private String COL;
	private String FID;
	private String FIDED;
	private String TR;
	private List<String> tids = new ArrayList<>();
	private List<String> sids = new ArrayList<>();
	private List<GeTaFC> fcs = new ArrayList<>();

	/**
	 * @return the aT
	 */
	public boolean getAT() {
		return AT;
	}

	/**
	 * @param aT
	 *            the aT to set
	 */
	public void setAT(boolean aT) {
		AT = aT;
	}

	/**
	 * @return the iD
	 */
	public String getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(String iD) {
		ID = iD;
	}

	/**
	 * @return the tids
	 */
	public final List<String> getTids() {
		return tids;
	}

	/**
	 * @return the fcs
	 */
	public final List<GeTaFC> getFCs() {
		return fcs;
	}

	/**
	 * @return the cOMM
	 */
	public final String getCOMM() {
		return COMM;
	}

	/**
	 * @param cOMM
	 *            the cOMM to set
	 */
	public final void setCOMM(String cOMM) {
		COMM = cOMM;
	}

	/**
	 * @return the cOL
	 */
	public final String getCOL() {
		return COL;
	}

	/**
	 * @param cOL
	 *            the cOL to set
	 */
	public final void setCOL(String cOL) {
		COL = cOL;
	}

	/**
	 * @return the fID
	 */
	public final String getFID() {
		return FID;
	}

	/**
	 * @param fID
	 *            the fID to set
	 */
	public final void setFID(String fID) {
		FID = fID;
	}

	/**
	 * @return the fIDED
	 */
	public final String getFIDED() {
		return FIDED;
	}

	/**
	 * @param fIDED
	 *            the fIDED to set
	 */
	public final void setFIDED(String fIDED) {
		FIDED = fIDED;
	}

	/**
	 * @return the tR
	 */
	public final String getTR() {
		return TR;
	}

	/**
	 * @param tR
	 *            the tR to set
	 */
	public final void setTR(String tR) {
		TR = tR;
	}

	/**
	 * @return the sids
	 */
	public List<String> getSids() {
		return sids;
	}
	
	/**
	 * @param sids the sids to set
	 */
	public final void setSids(List<String> sids) {
		this.sids = sids;
	}
	
	/*
	 * @copydoc @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getName() + "\n");
		sb.append(ID == null ? "" : "Id:" + ID + "\n");
		sb.append(FID == null ? "" : "FID:" + FID + "\n");
		sb.append(FIDED == null ? "" : "FIDED:" + FIDED + "\n");
		sb.append(TR == null ? "" : "TR:" + TR + "\n");
		sb.append(fcs.isEmpty() ? "" : "FC:\n");
		for (GeTaFC fc : this.getFCs()) {
			sb.append(fc.getFIDLET() == null ? "" : " FIDLET:" + fc.getFIDLET() + "\n");
			sb.append(fc.getFIDLETED() == null ? "" : " FIDLETED:" + fc.getFIDLETED() + "\n");
			sb.append(fc.getTRFID() == null ? "" : " TRFID:" + fc.getTRFID() + "\n");
			sb.append(fc.getLLs().isEmpty() ? "" : " LL:\n");
			for (GeTaLL ll : fc.getLLs()) {
				sb.append(ll.getTID() == null ? "" : "  Tid:" + ll.getTID() + "\n");
				sb.append(ll.getLAT() == null ? "" : "  LAT:" + ll.getLAT() + "\n");
			}
			sb.append(fc.getEDs().isEmpty() ? "" : " ED:\n");
			for (GeTaEd ed : fc.getEDs()) {
				sb.append(ed.getAUT() == null ? "" : "  aut:" + ed.getAUT() + "\n");
				sb.append(ed.getC() == null ? "" : "  c:" + ed.getC() + "\n");
				sb.append(ed.getG() == null ? "" : "  g:" + ed.getG() + "\n");
				sb.append(ed.getNE() == null ? "" : "  ne:" + ed.getNE() + "\n");
				sb.append(ed.getP() == null ? "" : "  p:" + ed.getP() + "\n");
				sb.append(ed.getLTs().isEmpty() ? "" : "  LT:\n");
				for (GeTaLT lt : ed.getLTs()) {
					sb.append(lt.getNT() == null ? "" : "   NT:" + lt.getNT() + "\n");
					sb.append(lt.getALs().isEmpty() ? "" : "   AL:\n");
					for (GeTaAL al : lt.getALs()) {
						sb.append(al.getNVs() == null ? "" : "    " + al.getNVs() + "\n");
					}
				}
			}
		}
		sb.append("Tid:" + getTids());
		return sb.toString();
	}

}
