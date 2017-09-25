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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;

/**
 * An object representation of a GeTa .json file, which contains
 * *GeTa word tokens* (Fidel script).
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaJson {
	
	private static final String SCR = "SCR";
	private static final String TR = "TR";
	private static final String FIDALWORDS = "FIDALWORDS";
	private static final String AT = "AT";
	private static final String ID = "Id";
	private static final String TID = "Tid";
	private static final String SID = "Sid";
	private static final String FC = "FC";
	private static final String FC_LL = "LL";
	private static final String FC_NR = "Nr";
	private static final String FC_ENDS = "endS";
	private static final String FC_END = "end";
	private static final String FC_TRFID = "TRFID";
	private static final String FC_FIDLETED = "FIDLETED";
	private static final String FC_FIDLET = "FIDLET";
	private static final String FC_ED = "Ed";
	private static final String LL_T = "T";
	private static final String LL_NR = "Nr";
	private static final String LL_L = "L";
	private static final String LL_TID = "Tid";
	private static final String LL_LAT = "LAT";
	private static final String COMM = "Comm";
	private static final String COL = "Col";
	private static final String FID = "FID";
	private static final String FIDED = "FIDED";

	private Integer scr;
	private Integer tr;
	private List<LinkedHashMap<String, ?>> wordObjects;
	private List<GeTaWord> words = new ArrayList<>();
	
	

	/**
	 * This is a constructor working as a {@link JsonCreator}, i.e.,
	 * the mapping of {@link GeTaJson} objects starts here.
	 * 
	 * @param scr
	 * @param tr
	 * @param wordObjects
	 */
	@JsonCreator
	public GeTaJson(@JsonProperty(SCR) Integer scr, @JsonProperty(TR) Integer tr, @JsonProperty(FIDALWORDS) List<LinkedHashMap<String, ?>> wordObjects) {
		this.scr = scr;
		this.tr = tr;
		this.wordObjects = wordObjects;
		for (LinkedHashMap<String, ?> w : this.wordObjects) {
			GeTaWord word = new GeTaWord();
			for (Entry<String, ?> entry : w.entrySet()) {
				String key = entry.getKey();
				Object val = entry.getValue();
				if (key.equals(ID)) {
					if (val instanceof String) {
						word.setID((String) val);
					}
					else {
						throw new PepperModuleException("Found an Id that is not a String: " + val + "!");
					}
				}
				else if (key.equals(AT)) {
					if (val instanceof Boolean) {
						word.setAT((boolean) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'AT\' must be of type Boolean!");
					}
				}
				else if (key.equals(TID)) {
					if (val instanceof List) {
						@SuppressWarnings("unchecked")
						List<String> tidList = (List<String>) val;
						for (String tid : tidList) {
							word.getTids().add(tid);
						}
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Tid\' must be a list of Strings!");
					}
				}
				else if (key.equals(SID)) {
					if (val instanceof List) {
						@SuppressWarnings("unchecked")
						ArrayList<LinkedHashMap<String, String>> sidMapList = (ArrayList<LinkedHashMap<String, String>>) val;
						for (LinkedHashMap<String, String> sidMap : sidMapList) {
							for (Entry<String, String> sidMapEntry : sidMap.entrySet()) {
								word.getSids().add(sidMapEntry.getValue());
							}
						}
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Sid\' must be a list of ???!");
					}
				}
				else if (key.equals(FC)) {
					if (val instanceof List) {
						@SuppressWarnings("unchecked")
						List<LinkedHashMap<String, ?>> valList = (List<LinkedHashMap<String, ?>>) val;
						for (LinkedHashMap<String, ?> value : valList) {
							GeTaFC fc = new GeTaFC();
							for (Entry<String, ?> fcValEntry : value.entrySet()) {
								String fcValKey = fcValEntry.getKey();
								Object fcVal = fcValEntry.getValue();
								if (fcValKey.equals(FC_LL)) {
									if (fcVal instanceof List) {
										@SuppressWarnings("unchecked")
										List<LinkedHashMap<String, String>> llList = (List<LinkedHashMap<String, String>>) fcVal;
										for (LinkedHashMap<String, String> llMap : llList) {
											GeTaLL ll = new GeTaLL();
											for (Entry<String, String> llEntry : llMap.entrySet()) {
												String llKey = llEntry.getKey();
												String llVal = llEntry.getValue();
												if (llKey.equals(LL_T)) {
													ll.setT(llVal);
												}
												else if (llKey.equals(LL_NR)) {
													ll.setNR(llVal);
												}
												else if (llKey.equals(LL_L)) {
													ll.setL(llVal);
												}
												else if (llKey.equals(LL_TID)) {
													ll.setTID(llVal);
												}
												else if (llKey.equals(LL_LAT)) {
													ll.setLAT(llVal);
												}
												else {
													throw new PepperModuleException("Found an unknown key in LL: " + llKey + " of type " + val.getClass() + "!");
												}
											}
											fc.getLLs().add(ll);
										}
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'LL\' must be a list!");
									}
								}
								else if (fcValKey.equals(FC_NR)) {
									if (fcVal instanceof String) {
										fc.setNR((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Nr\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_ENDS)) {
									if (fcVal instanceof String) {
										fc.setENDS((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'endS\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_END)) {
									if (fcVal instanceof String) {
										fc.setEND((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'end\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_TRFID)) {
									if (fcVal instanceof String) {
										fc.setTRFID((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'end\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_FIDLETED)) {
									if (fcVal instanceof String) {
										fc.setFIDLETED((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'end\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_FIDLET)) {
									if (fcVal instanceof String) {
										fc.setFIDLET((String) fcVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'end\' must be a String!");
									}
								}
								else if (fcValKey.equals(FC_ED)) {
									GeTaEd ed = new GeTaEd();
									if (fcVal instanceof LinkedHashMap) {
										@SuppressWarnings("unchecked")
										LinkedHashMap<String, ?> edMap = (LinkedHashMap<String, ?>) fcVal;
										for (Entry<String, ?> edEntry : edMap.entrySet()) {
											String edKey = edEntry.getKey();
											Object edVal = edEntry.getValue();
											if (edKey.equals("aut")) {
												if (edVal instanceof String) {
													ed.setAUT((String) edVal);
												}
											}
											else if (edKey.equals("LT")) {
												GeTaLT lt = new GeTaLT();
												if (edVal instanceof List) {
													@SuppressWarnings("unchecked")
													List<LinkedHashMap<String, ?>> ltList = (List<LinkedHashMap<String, ?>>) edVal;
													for (LinkedHashMap<String, ?> innerLTMap : ltList) {
														for (Entry<String, ?> ltMapEntry : innerLTMap.entrySet()) {
															String ltMapKey = ltMapEntry.getKey();
															Object ltMapVal = ltMapEntry.getValue();
															if (ltMapKey.equals("NT")) {
																if (ltMapVal instanceof String) {
																	lt.setNT((String) ltMapVal);
																}
																else {
																	throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'NT\' must be a String!");

																}
															}
															else if (ltMapKey.equals("AL")) {
																GeTaAL al = new GeTaAL();
																if (ltMapVal instanceof List) {
																	@SuppressWarnings("unchecked")
																	List<LinkedHashMap<String, String>> alList = (List<LinkedHashMap<String, String>>) ltMapVal;
																	for (LinkedHashMap<String, String> alMap : alList) {
																		for (String alKey : alMap.keySet()) {
																			if (alKey.equals("N")) {
																				al.getNVs().add(new String[] { alMap.get(alKey), alMap.get("V") });
																			}
																			else if (!alKey.startsWith("N") && !alKey.startsWith("V")) {
																				throw new PepperModuleException("Found an unknown key in annotation: " + alKey + "!");
																			}
																		}
																	}
																}
																else {
																	throw new PepperModuleException("Model error in annotations with Id \"" + word.getID() + "\": Value of \'AL\' must be a List!");
																}
																lt.getALs().add(al);
															}
														}
													}
												}
												ed.getLTs().add(lt);
											}
										}
									}
									else {
										throw new PepperModuleException("Found an unknown key in FC: " + fcValKey + " of type " + val.getClass() + "!");
									}
									fc.getEDs().add(ed);
								}
							}
							word.getFCs().add(fc);
						}
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'FC\' must be a list!");
					}
				}
				else if (key.equals(COMM)) {
					if (val instanceof String) {
						word.setCOMM((String) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Comm\' must be of type String!");
					}
				}
				else if (key.equals(COL)) {
					if (val instanceof String) {
						word.setCOL((String) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Col\' must be of type String!");
					}
				}
				else if (key.equals(FID)) {
					if (val instanceof String) {
						word.setFID((String) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Col\' must be of type String!");
					}
				}
				else if (key.equals(FIDED)) {
					if (val instanceof String) {
						word.setFIDED((String) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Col\' must be of type String!");
					}
				}
				else if (key.equals(TR)) {
					if (val instanceof String) {
						word.setTR((String) val);
					}
					else {
						throw new PepperModuleException("Model error in word with Id \"" + word.getID() + "\": Value of \'Col\' must be of type String!");
					}
				}
				else {
					throw new PepperModuleException("Found an unknown key in Word: " + key + " of type " + val.getClass() + "!");
				}
			}
			words.add(word);
		}
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
	 * @return the words
	 */
	public final List<GeTaWord> getWords() {
		return words;
	}

}
