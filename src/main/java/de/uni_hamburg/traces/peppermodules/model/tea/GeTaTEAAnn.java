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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An object representation of a JSON object from a GeTa *TEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaTEAAnn {
	
	private static final Logger logger = LoggerFactory.getLogger(GeTaTEAAnn.class);
	
	private static final String ID = "Id";
	private static final String TOKL = "TOKL";
	private static final String M = "M";
	private static final String LT = "LT";
	private static final String NT = "NT";
	private static final String AL = "AL";
	private static final String N = "N";
	private static final String V = "V";
	private String id;
	private String tokl;
	public String ne;
	public List<GeTaLT> lts = new ArrayList<>();
	private LinkedHashMap<String,?> ms;

	/**
	 * This is a constructor working as a {@link JsonCreator}, i.e.,
	 * the mapping of {@link GeTaTEAAnn} objects starts here.
	 * 
	 * @param id
	 * @param tokl
	 * @param m
	 */
	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true) // FIXME: Delete once everything is included!
	public GeTaTEAAnn(@JsonProperty(ID) String id, @JsonProperty(TOKL) String tokl, @JsonProperty(M) LinkedHashMap<String, ?> m) {
		this.id = id;
		this.tokl = tokl;
		this.ms = m;
		if (ms != null) {
			for (Entry<String, ?> entry : m.entrySet()) {
				Object mVal = entry.getValue();
				String mKey = entry.getKey();
				if (mKey.equals("ne")) {
					if (mVal instanceof String) {
						this.ne = (String) mVal;
					}
					else {
						throw new PepperModuleException("Model error in annotation with Id \"" + id + "\": Value of \'ne\' must be a list!");
					}
				}
				else if (mKey.equals(LT)) {
					GeTaLT lt = new GeTaLT();
					if (mVal instanceof List) {
						@SuppressWarnings("unchecked")
						List<LinkedHashMap<String, ?>> ltList = (List<LinkedHashMap<String, ?>>) mVal;
						for (LinkedHashMap<String, ?> innerLTMap : ltList) {
							for (Entry<String, ?> ltMapEntry : innerLTMap.entrySet()) {
								String ltMapKey = ltMapEntry.getKey();
								Object ltMapVal = ltMapEntry.getValue();
								if (ltMapKey.equals(NT)) {
									if (ltMapVal instanceof String) {
										lt.setNT((String) ltMapVal);
									}
									else {
										throw new PepperModuleException("Model error in word with Id \"" + id + "\": Value of \'NT\' must be a String!");

									}
								}
								else if (ltMapKey.equals(AL)) {
									GeTaAL al = new GeTaAL();
									if (ltMapVal instanceof List) {
										@SuppressWarnings("unchecked")
										List<LinkedHashMap<String, String>> alList = (List<LinkedHashMap<String, String>>) ltMapVal;
										for (LinkedHashMap<String, String> alMap : alList) {
											for (String alKey : alMap.keySet()) {
												if (alKey.equals(N)) {
													al.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V) });
												}
												else if (alKey.length() > 1 && alKey.startsWith(N)) {
													String suffix = alKey.substring(1, alKey.length());
													al.getNVs().add(new String[] { alMap.get(alKey), alMap.get(V + suffix) });
												}
												else if (!alKey.startsWith(N) && !alKey.startsWith(V)) {
													throw new PepperModuleException("Found an unknown key in annotation: " + alKey + "!");
												}
											}
										}
									}
									else {
										throw new PepperModuleException("Model error in annotations with Id \"" + id + "\": Value of \'AL\' must be a List!");
									}
									lt.getALs().add(al);
								}
							}
						}
					}
					lts.add(lt);
				}
				else {
					throw new PepperModuleException("Found an unknown key in annotation: " + mKey + " of type " + mVal.getClass() + "!");
				}
			}
		}
		else {
			logger.info("Not found any annotations for Id " + id + " (" + tokl + ")!");
		}
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
	 * @return the tokl
	 */
	public final String getTokl() {
		return tokl;
	}

	/**
	 * @param tokl
	 *            the tokl to set
	 */
	public final void setTokl(String tokl) {
		this.tokl = tokl;
	}

	/**
	 * @return the ms
	 */
	public final LinkedHashMap<String, ?> getMs() {
		return ms;
	}

	/*
	 * @copydoc @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append(this.getClass().getName() + "\n");
		b.append(ID + ":" + id + "\n");
		b.append(TOKL + ":" + tokl + "\n");
		b.append(getMs() == null ? "" : "M:\n");
		if (getMs() != null) {
			b.append(ne == null ? "" : " ne:" + ne + "\n");
			b.append(lts.isEmpty() ? "" : " LT:\n");
			for (GeTaLT lt : lts) {
				b.append("  NT: " + lt.getNT() + "\n");
				b.append(lt.getALs().isEmpty() ? "" : "  AL:\n");
				for (GeTaAL al : lt.getALs()) {
					b.append(al.getNVs().isEmpty() ? "" : "   name-value:\n");
					for (String[] nv : al.getNVs()) {
						b.append("    " + nv[0] + ":" + nv[1] + "\n");
					}
				}
			}
		}
		return b.toString();
	}

	/**
	 * @return the lts
	 */
	public final List<GeTaLT> getLTs() {
		return lts;
	}
		
}