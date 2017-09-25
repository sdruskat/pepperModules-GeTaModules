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
package de.uni_hamburg.traces.peppermodules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.corpus_tools.pepper.common.DOCUMENT_STATUS;
import org.corpus_tools.pepper.impl.PepperMapperImpl;
import org.corpus_tools.pepper.modules.PepperMapper;
import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;
import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SDocumentGraph;
import org.corpus_tools.salt.common.SSpan;
import org.corpus_tools.salt.common.STextualDS;
import org.corpus_tools.salt.common.SToken;
import org.corpus_tools.salt.core.SAnnotation;
import org.corpus_tools.salt.core.SLayer;
import org.corpus_tools.salt.exceptions.SaltInsertionException;
import org.eclipse.emf.common.util.URI;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni_hamburg.traces.peppermodules.model.dea.GeTaATT;
import de.uni_hamburg.traces.peppermodules.model.dea.GeTaDEAAnn;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaEd;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaFC;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaJson;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaLL;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaWord;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaTEAAnn;

/**
 * The mapper class doing the actual mapping work.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaMapper extends PepperMapperImpl implements PepperMapper {

	private static final Logger logger = LoggerFactory.getLogger(GeTaMapper.class);
	private static final String SCR = "SCR";
	private static final String TR = "TR";
	private static final String FID = "FID";
	private static final String FIDED = "FIDED";
	private static final String FIDEDh = "FIDEDh";
	private static final String TRFID = "TRFID";
	private static final String FIDLETED = "FIDLETED";
	private static final String FIDLET = "FIDLET";
	private static final String TID = "Tid";
	private static final String SID = "Sid";
	private static final String TRACES = "GeTa";
	private static final String TRACESED = "GeTa_Ed";
	private static final String TRACESSID = "GeTa_S";
	private static final String AL = "AL";
	private static final String p = "p";
	private static final String aut = "aut";
	private static final String c = "c";
	private static final String g = "g";
	private static final String ne = "ne";
	private static final String NT = "NT";
	private static final String LE = "Le";
	private static final String COMMENT = "Comm";
	private static final String TOKL = "TOKL";
	private static final String JSON_FILE_SUFFIX = "EA";
	private static final String TEA_FILE_SUFFIX = "TEA";
	private static final String DEA_FILE_SUFFIX = "DEA";
	private static final String ANN_FILE_ENDING = "ann";
	private final Map<String, GeTaTEAAnn> tEAannMap = new HashMap<>();
	private final Map<String, GeTaDEAAnn> dEAannMap = new HashMap<>();
	private JsonParser jsonParser = null;
	private boolean mapTEA = true;
	private boolean mapDEA = true;
	private static final String TRACES_NAMESPACE = "GeTa"; 

	/*
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperMapperImpl#mapSCorpus()
	 */
	@Override
	public DOCUMENT_STATUS mapSCorpus() {
		return super.mapSCorpus(); // (DOCUMENT_STATUS.COMPLETED);
	}

	/*
	 * @copydoc @see
	 * org.corpus_tools.pepper.impl.PepperMapperImpl#mapS()
	 */
	@Override
	public DOCUMENT_STATUS mapSDocument() {
		// Create SDocumentGraph and set source text
		SDocumentGraph graph = SaltFactory.createSDocumentGraph();
		getDocument().setDocumentGraph(graph);
		STextualDS text = SaltFactory.createSTextualDS();
		text.setText("");
		graph.addNode(text);

		// Create layers
		SLayer annoLayer = createLayer(graph, "anno");
		SLayer basicAnnoLayer = createLayer(graph, "basic-anno");
		SLayer edLayer = createLayer(graph, "ed-anno");
		SLayer tRLayer = createLayer(graph, "TR");
		SLayer fIDEDLayer = createLayer(graph, "FIDED");

		// Create a parseable String from file
		URI resource = getResourceURI();
		String path = resource.toFileString();
		String tEAPath = path.split(JSON_FILE_SUFFIX + ".json")[0].concat(TEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String dEAPath = path.split(JSON_FILE_SUFFIX + ".json")[0].concat(DEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		File jsonFile = new File(path);
		File tEAannFile = new File(tEAPath);
		if (!tEAannFile.exists()) {
			throw new PepperModuleException("Could not find the TEA.ann file at " + tEAPath + "!");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(tEAannFile))){
			String rl = br.readLine();
			if (rl == null) {
				logger.error("No TEA.ann file found or file is empty!");
				mapTEA = false;
			}
			else if (rl.isEmpty()) {
				logger.error("TEA.ann file is empty!");
				mapTEA = false;
			}
		}
		catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}     
		File dEAannFile = new File(dEAPath);
		if (!dEAannFile.exists()) {
			throw new PepperModuleException("Could not find the DEA.ann file at " + dEAPath + "!");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(dEAannFile))){
			String rl = br.readLine();
			if (rl == null) {
				logger.error("No DEA.ann file found or file is empty!");
				mapDEA = false;
			}
			else if (rl.isEmpty()) {
				logger.error("DEA.ann file is empty!");
				mapDEA = false;
			}
		}
		catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}     
		
		// Initiate the mapping process for .json and .ann files
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			ObjectMapper tEAMapper = new ObjectMapper();
			tEAMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			ObjectMapper dEAMapper = new ObjectMapper();
			GeTaJson json;
			List<GeTaTEAAnn> tEAanns = null;
			List<GeTaDEAAnn> dEAanns = null;
			try {
				json = jsonMapper.readValue(jsonFile, new TypeReference<GeTaJson>() {
				});
				if (mapTEA)
					tEAanns = tEAMapper.readValue(tEAannFile, new TypeReference<List<GeTaTEAAnn>>() {
					});
				if (mapDEA)
					dEAanns = dEAMapper.readValue(dEAannFile, new TypeReference<List<GeTaDEAAnn>>() {
					});
			}
			catch (JsonMappingException | JsonParseException e) {
				logger.error("Error while parsing JSON.", e);
				return DOCUMENT_STATUS.FAILED;
			}
			if (mapTEA) {
				for (GeTaTEAAnn tEAann : tEAanns) {
					tEAannMap.put(tEAann.getId(), tEAann);
				}
			}
			if (mapDEA) {
				for (GeTaDEAAnn dEAann : dEAanns) {
					dEAannMap.put(dEAann.getId(), dEAann);
				}
			}

			/*
			 * ############################ 
			 * Map the JSON objects to Salt
			 * ############################
			 */
			// Map document metadata
			getDocument().createMetaAnnotation(TRACES_NAMESPACE, SCR, json.getSCR());
			getDocument().createMetaAnnotation(TRACES_NAMESPACE, TR, json.getTR());

			List<SSpan> annoSpans = new ArrayList<>();
			SSpan objectAnnoSpan = null, objectBasicAnnoSpan = null, tRSpan = null, fIDEDSpan = null;
			Map<String, List<SToken>> tid2TokMap = new HashMap<>();
			Map<String, List<GeTaWord>> sid2WordsMap = new HashMap<>();

			// Iterate through all GeTaWords and map accordingly
			for (GeTaWord word : json.getWords()) {
				for (String sid : word.getSids()) {
					if (sid2WordsMap.get(sid) == null) {
						sid2WordsMap.put(sid, new ArrayList<GeTaWord>(Arrays.asList(word)));
					}
					else {
						sid2WordsMap.get(sid).add(word);
					}
				}
				for (GeTaFC fc : word.getFCs()) {
					List<SToken> tokList = new ArrayList<>();
					for (GeTaLL ll : fc.getLLs()) {
						text.setText(text.getText().concat(ll.getLAT()));
						SToken tok = graph.createToken(text, text.getText().length() - ll.getLAT().length(), text.getText().length());
						if (tid2TokMap.get(ll.getTID()) == null) {
							tid2TokMap.put(ll.getTID(), new ArrayList<>(Arrays.asList(tok)));
						}
						else {
							tid2TokMap.get(ll.getTID()).add(tok);
						}
						tokList.add(tok);
					}
					SSpan annoSpan = graph.createSpan(tokList);
					annoSpans.add(annoSpan);
					SSpan basicAnnoSpan = graph.createSpan(tokList);
					SSpan edAnnoSpan = graph.createSpan(tokList);
					annoSpan.createAnnotation(TRACES_NAMESPACE, FIDLETED, fc.getFIDLETED());
					annoSpan.createAnnotation(TRACES_NAMESPACE, TRFID, fc.getTRFID());
					annoLayer.addNode(annoSpan);
					basicAnnoSpan.createAnnotation(TRACES_NAMESPACE, FIDLET, fc.getFIDLET());
					basicAnnoLayer.addNode(basicAnnoSpan);
					edLayer.addNode(edAnnoSpan);
					for (GeTaEd ed : fc.getEDs()) {
						edAnnoSpan.createAnnotation(TRACESED, aut, ed.getAUT());
						edAnnoSpan.createAnnotation(TRACESED, c, ed.getC());
						edAnnoSpan.createAnnotation(TRACESED, g, ed.getG());
						edAnnoSpan.createAnnotation(TRACESED, ne, ed.getNE());
						edAnnoSpan.createAnnotation(TRACESED, p, ed.getP());
						for (GeTaLT lt : ed.getLTs()) {
							SAnnotation nTanno;
							if ((nTanno = edAnnoSpan.getAnnotation(TRACESED + "::" + NT)) == null) {
								edAnnoSpan.createAnnotation(TRACESED, NT, lt.getNT());
							}
							else {
								String oldValue = nTanno.getValue_STEXT();
								nTanno.setValue(oldValue + ", " + lt.getNT());
							}
							for (GeTaAL al : lt.getALs()) {
								for (String[] nv : al.getNVs()) {
									try {
										edAnnoSpan.createAnnotation(TRACESED + "_" + AL, nv[0], nv[1]);
									}
									catch (SaltInsertionException e) {
										throw new PepperModuleException("Error in document " + getDocument().getName() + " for token " + word.getID() + ": Cannot insert annotation \"" + nv[0] + "\" because it already exists!", e);
									}
								}
							}
						}
					}
				}
				if (!annoSpans.isEmpty()) {
					List<SToken> annoSpanTokens = new ArrayList<>();
					for (SSpan span : annoSpans) {
						annoSpanTokens.addAll(graph.getOverlappedTokens(span));
					}
					objectAnnoSpan = graph.createSpan(annoSpanTokens);
					tRSpan = graph.createSpan(annoSpanTokens);
					tRSpan.createAnnotation(TRACES, TR, word.getTR());
					tRLayer.addNode(tRSpan);
					fIDEDSpan = graph.createSpan(annoSpanTokens);
					fIDEDSpan.createAnnotation(TRACES, FIDED, Jsoup.parse(word.getFIDED()).text());
					fIDEDLayer.addNode(fIDEDSpan);
					objectBasicAnnoSpan = graph.createSpan(annoSpanTokens);
					objectBasicAnnoSpan.createAnnotation(TRACES, FID, word.getFID());
					objectBasicAnnoSpan.createAnnotation(TRACES, FIDEDh, word.getFIDED());
					objectBasicAnnoSpan.createAnnotation(TRACES, COMMENT, word.getCOMM());
					basicAnnoLayer.addNode(objectBasicAnnoSpan);
					annoLayer.addNode(objectAnnoSpan);

				}
				annoSpans.clear();
				text.setText(text.getText().concat(" "));
			}
			/*
			 * Connect the Fidel words with their linguistic annotations. The
			 * connection is made via the Tids.
			 */
			for (Entry<String, List<SToken>> tracesToken : tid2TokMap.entrySet()) {
				SSpan tracesTokenSpan = graph.createSpan(tracesToken.getValue());
				annoLayer.addNode(tracesTokenSpan);
				if (mapTEA) {
					annotateAnnoSpanWithTEAAnnos(tracesTokenSpan, tracesToken.getKey());
				}
			}
			/*
			 * Connect the Fidel words with their section annotations. The
			 * connection is made via the Sids.
			 */
			for (Entry<String, List<GeTaWord>> sidAndTracesWord : sid2WordsMap.entrySet()) {
				Set<SToken> sidTokens = new HashSet<>();
				// Compile list of tokens governed by words in this sid
				for (GeTaWord word : sidAndTracesWord.getValue()) {
					for (String wordTid : word.getTids()) {
						for (SToken wordToken : tid2TokMap.get(wordTid)) {
							sidTokens.add(wordToken);
						}
					}
				}
				SSpan sidSpan = graph.createSpan(new ArrayList<SToken>(sidTokens));
				annoLayer.addNode(sidSpan);
				if (mapDEA) {
					annotateAnnoSpanWithDEAAnnos(sidSpan, sidAndTracesWord.getKey());
				}
			}
		} catch (IOException e) {
			throw new PepperModuleException("Error parsing the JSON file " + jsonFile.getName() + "!", e);
		}
		return (DOCUMENT_STATUS.COMPLETED);
	}

	/**
	 * TODO: Description
	 *
	 * @param sidSpan
	 * @param sid
	 */
	private void annotateAnnoSpanWithDEAAnnos(SSpan sidSpan, String sid) {
		GeTaDEAAnn dea = dEAannMap.get(sid);
		if (dea == null) {
			logger.info("Could not find annotations for " + SID + " " + sid + " near " + jsonParser.getCurrentLocation() + "!");
		}
		else {
//			sidSpan.createAnnotation(TRACESSID, LE, dea.getLe());
			// Alternative annotation format: Le: name (number) - creator - style: style [comment]
			Map<String, String> annoMap = new HashMap<>();
			for (GeTaATT att : dea.getAttList()) {
				for (String[] nv : att.getNVs()) {
					if (nv[1] != null && !nv[1].isEmpty()) {
						try {
							/*
							 * Create the annotation, and replace whitespaces,
							 * which are illegal as annotation keys, with
							 * dashes.
							 */
//							sidSpan.createAnnotation(TRACESSID, nv[0].replaceAll(" ", "-"), nv[1]);
							annoMap.put(nv[0].replaceAll(" ", "-"), nv[1]);
						} catch (SaltInsertionException e) {
							PepperModuleException ex = new PepperModuleException("Duplicate annotation name in " + getDocument().getName()
									+ "!\n" + "Current annotation: " + TRACESSID + "::" + nv[0] + "=" + nv[1] + "\n"
									+ "Existing annotation: " + sidSpan.getAnnotation(TRACESSID, nv[0]) + "\n" + "Sid: "
									+ sid, e);
							logger.error("Conversion error: ", ex);
							throw ex;
						}
					} else {
						logger.info("Found an empty annotation (Sid: " + sid + "): \"" + nv[0] + "\"! Ignoring it.");
					}
				}
			}
			sidSpan.createAnnotation(TRACESSID, "DEA", dea.getLe() + ": " + annoMap.get("name") + " (" + annoMap.get("number") + ") - " + 
			annoMap.get("creator") + " - style: " + (annoMap.get("style") == null ? "-" : annoMap.get("style")) + " [" + (annoMap.get("comm") == null ? "-" : annoMap.get("comm")) + "]");
			
		}
	}

	/**
	 * Maps the annotations from the GeTa .ann annotations file to the
	 * {@link SSpan}s that are to be annotated.
	 *
	 * @param annoSpan
	 * @param tid
	 */
	private void annotateAnnoSpanWithTEAAnnos(SSpan annoSpan, String tid) {
		GeTaTEAAnn tea = tEAannMap.get(tid);
		if (tea == null) {
			logger.info("Could not find annotations for " + TID + " " + tid + " near " + jsonParser.getCurrentLocation() + "!");
		}
		else {
			annoSpan.createAnnotation(TRACES, TOKL, tea.getTokl());
			annoSpan.createAnnotation(TRACES_NAMESPACE, ne, tea.ne);
			for (GeTaLT lt : tea.getLTs()) {
				annoSpan.createAnnotation(TRACES_NAMESPACE, NT, lt.getNT());
				for (GeTaAL al : lt.getALs()) {
					for (String[] nv : al.getNVs()) {
						if (nv[1] != null && !nv[1].isEmpty()) {
							try {
								/* 
								 * Special case: split up "lex" annotations which contain
								 * Tid and actual value separated by a double dash "--".
								 */
								if (nv[0].equals("lex")) {
									annoSpan.createAnnotation(TRACES_NAMESPACE, "lem", nv[1].split("--")[1]);
								}
								else {
									/*
									 *  Create the annotation, and replace whitespaces, which
									 *  are illegal as annotation keys, with dashes.
									 */
									annoSpan.createAnnotation(TRACES, nv[0].replaceAll(" ", "-"), nv[1]);
								}
							}
							catch (SaltInsertionException e) {
								PepperModuleException ex = new PepperModuleException("Duplicate annotation name in " + getDocument().getName() + "!\n" + "Current annotation: " + TRACES + "::" + nv[0] + "=" + nv[1] + "\n" + "Existing annotation: " + annoSpan.getAnnotation(TRACES, nv[0]) + "\n" + "Token ID: " + tid, e);
								logger.error("Conversion error: ", ex);
								throw ex;
							}
						}
						else {
							logger.info("Found an empty annotation (Tid: " + tid + "): \"" + nv[0] + "\"! Ignoring it.");
						}
					}
				}
			}
		}
	}

	/**
	 * Creates an {@link SLayer} with the given name
	 * and adds it to the {@link SDocumentGraph}.
	 *
	 * @param graph
	 * @param string
	 */
	private SLayer createLayer(SDocumentGraph graph, String name) {
		SLayer layer = SaltFactory.createSLayer();
		layer.setName(name);
		graph.addLayer(layer);
		return layer;
	}

}
