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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;

/**
 * An object representation of a JSON object from a GeTa *TEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaDEAAnn {
	
	private static final Logger logger = LoggerFactory.getLogger(GeTaDEAAnn.class);
	
	private static final String ID = "Id";
	private static final String WSTART = "Wstart";
	private static final String WSTOP = "Wstop";
	private static final String LE = "Le";
	private static final String ATT = "Att";
	private static final String N = "N";
	private static final String V = "V";
	private String id;
	private String wstart;
	private String wstop;
	private String le;
	private List<GeTaATT> attList = new ArrayList<>();


	/**
	 * This is a constructor working as a {@link JsonCreator}, i.e.,
	 * the mapping of {@link GeTaTEAAnn} objects starts here.
	 * 
	 * @param id
	 * @param tokl
	 * @param m
	 */
	@JsonCreator
	public GeTaDEAAnn(@JsonProperty(ID) String id, @JsonProperty(WSTART) String wstart, @JsonProperty(WSTOP) String wstop, @JsonProperty(LE) String le, @JsonProperty(ATT) ArrayList atts) {
		this.id = id;
		this.wstart = wstart;
		this.wstop = wstop;
		this.le = le;
//		this.attList = atts;
		
		if (atts != null && atts instanceof List) {
			List<LinkedHashMap<String, String>> attMapList = (List<LinkedHashMap<String, String>>) atts;
			for (LinkedHashMap<String, String> attMap : attMapList) {
				GeTaATT att = new GeTaATT();
				for (String attKey : attMap.keySet()) {
					if (attKey.equals(N)) {
						att.getNVs().add(new String[] { attMap.get(attKey), attMap.get(V) });
					}
					else if (attKey.length() > 1 && attKey.startsWith(N)) {
						String suffix = attKey.substring(1, attKey.length());
						att.getNVs().add(new String[] { attMap.get(attKey), attMap.get(V + suffix) });
					}
					else if (!attKey.startsWith(N) && !attKey.startsWith(V)) {
						throw new PepperModuleException("Found an unknown key in annotation: " + attKey + "!");
					}
				}
				attList.add(att);
			}
		}
		else {
			throw new PepperModuleException("Model error in annotations with Id \"" + id + "\": Value of \'Att\' must be a List!");
		}
		
		
		
		
//		if (atts != null && atts instanceof List) {
//			for (String[] att : atts) {
//				
//			}
//			GeTaATT att = new GeTaATT();
//			if (ltMapVal instanceof List) {
//				@SuppressWarnings("unchecked")
//				List<LinkedHashMap<String, String>> alList = (List<LinkedHashMap<String, String>>) ltMapVal;
//				for (LinkedHashMap<String, String> alMap : alList) {
//					for (String alKey : alMap.keySet()) {
//						if (alKey.equals(N)) {
//							att.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V) });
//						}
//						else if (alKey.length() > 1 && alKey.startsWith(N)) {
//							String suffix = alKey.substring(1, alKey.length());
//							att.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V + suffix) });
//						}
//						else if (!alKey.startsWith(N) && !alKey.startsWith(V)) {
//							throw new PepperModuleException("Found an unknown key in annotation: " + alKey + "!");
//						}
//					}
//				}
//			}
//			else {
//				throw new PepperModuleException("Model error in annotations with Id \"" + id + "\": Value of \'AL\' must be a List!");
//			}
//			lt.getALs().add(att);
//		}
//		
//		if (atts != null) {
//			for (Entry<String, ?> entry : m.entrySet()) {
//				Object mVal = entry.getValue();
//				String mKey = entry.getKey();
//				if (mKey.equals("ne")) {
//					if (mVal instanceof String) {
//						this.ne = (String) mVal;
//					}
//					else {
//						throw new PepperModuleException("Model error in annotation with Id \"" + id + "\": Value of \'ne\' must be a list!");
//					}
//				}
//				else if (mKey.equals(LT)) {
//					GeTaLT lt = new GeTaLT();
//					if (mVal instanceof List) {
//						@SuppressWarnings("unchecked")
//						List<LinkedHashMap<String, ?>> ltList = (List<LinkedHashMap<String, ?>>) mVal;
//						for (LinkedHashMap<String, ?> innerLTMap : ltList) {
//							for (Entry<String, ?> ltMapEntry : innerLTMap.entrySet()) {
//								String ltMapKey = ltMapEntry.getKey();
//								Object ltMapVal = ltMapEntry.getValue();
//								if (ltMapKey.equals(NT)) {
//									if (ltMapVal instanceof String) {
//										lt.setNT((String) ltMapVal);
//									}
//									else {
//										throw new PepperModuleException("Model error in word with Id \"" + id + "\": Value of \'NT\' must be a String!");
//
//									}
//								}
//								else if (ltMapKey.equals(AL)) {
//									GeTaAL al = new GeTaAL();
//									if (ltMapVal instanceof List) {
//										@SuppressWarnings("unchecked")
//										List<LinkedHashMap<String, String>> alList = (List<LinkedHashMap<String, String>>) ltMapVal;
//										for (LinkedHashMap<String, String> alMap : alList) {
//											for (String alKey : alMap.keySet()) {
//												if (alKey.equals(N)) {
//													al.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V) });
//												}
//												else if (alKey.length() > 1 && alKey.startsWith(N)) {
//													String suffix = alKey.substring(1, alKey.length());
//													al.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V + suffix) });
//												}
//												else if (!alKey.startsWith(N) && !alKey.startsWith(V)) {
//													throw new PepperModuleException("Found an unknown key in annotation: " + alKey + "!");
//												}
//											}
//										}
//									}
//									else {
//										throw new PepperModuleException("Model error in annotations with Id \"" + id + "\": Value of \'AL\' must be a List!");
//									}
//									lt.getALs().add(al);
//								}
//							}
//						}
//					}
//					lts.add(lt);
//				}
//				else {
//					throw new PepperModuleException("Found an unknown key in annotation: " + mKey + " of type " + mVal.getClass() + "!");
//				}
//			}
//		}
//		else {
//			logger.warn("Not found any annotations for Id " + id + " (" + tokl + ")!");
//		}
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the wstart
	 */
	public String getWstart() {
		return wstart;
	}

	/**
	 * @param wstart the wstart to set
	 */
	public final void setWstart(String wstart) {
		this.wstart = wstart;
	}

	/**
	 * @return the wstop
	 */
	public String getWstop() {
		return wstop;
	}

	/**
	 * @param wstop the wstop to set
	 */
	public final void setWstop(String wstop) {
		this.wstop = wstop;
	}

	/**
	 * @return the le
	 */
	public String getLe() {
		return le;
	}

	/**
	 * @param le the le to set
	 */
	public final void setLe(String le) {
		this.le = le;
	}
		
	/**
	 * @return the attList
	 */
	public List<GeTaATT> getAttList() {
		return attList;
	}
	
	/**
	 * @param attList the attList to set
	 */
	public final void setAttList(List<GeTaATT> attList) {
		this.attList = attList;
	}

	/*
	 * @copydoc @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getClass().getName() + "\n");
		b.append(ID + ":" + id + "\n");
		b.append(WSTART + ":" + wstart + "\n");
		b.append(WSTOP + ":" + wstop + "\n");
		b.append(LE + ":" + le + "\n");
		for (GeTaATT att : getAttList()) {
			b.append(att.getNVs().isEmpty() ? "" : "   name-value:\n");
			for (String[] nv : att.getNVs()) {
				b.append("    " + nv[0] + ":" + nv[1] + "\n");
			}
		}
		return b.toString();
	}

}