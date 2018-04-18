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
import org.corpus_tools.pepper.modules.PepperModuleProperties;
import org.corpus_tools.pepper.modules.exceptions.PepperModuleException;
import org.corpus_tools.salt.SaltFactory;
import org.corpus_tools.salt.common.SDocumentGraph;
import org.corpus_tools.salt.common.SSpan;
import org.corpus_tools.salt.common.STextualDS;
import org.corpus_tools.salt.common.SToken;
import org.corpus_tools.salt.core.SLayer;
import org.corpus_tools.salt.exceptions.SaltException;
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

import de.uni_hamburg.traces.peppermodules.model.dea.GeTaDEA;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaFidalword;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaLL;
import de.uni_hamburg.traces.peppermodules.model.metaea.GeTaMetaEA;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaEA;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaEd;
import de.uni_hamburg.traces.peppermodules.model.ea.GeTaFC;
import de.uni_hamburg.traces.peppermodules.model.nea.GeTaNEA;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaM;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaTEA;
import de.uni_hamburg.traces.peppermodules.properties.GeTaImporterProperties;

/**
 * The mapper class doing the actual mapping work.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaMapper extends PepperMapperImpl implements PepperMapper {

	private static final Logger logger = LoggerFactory.getLogger(GeTaMapper.class);
	private GeTaImporterProperties properties = null;
	
	/*
	 * ██╗  ██╗███████╗██╗   ██╗███████╗
	 * ██║ ██╔╝██╔════╝╚██╗ ██╔╝██╔════╝
	 * █████╔╝ █████╗   ╚████╔╝ ███████╗
	 * ██╔═██╗ ██╔══╝    ╚██╔╝  ╚════██║
	 * ██║  ██╗███████╗   ██║   ███████║
	 * ╚═╝  ╚═╝╚══════╝   ╚═╝   ╚══════╝
	 */
	// ███ Graphical unit annotations ███
	/** Transcription vocalisation */
	public static final String TR = "TR";
	/** Original script type */
	public static final String SCR = "SCR";
	/** Graphical unit words */
	public static final String FIDALWORDS = "FIDALWORDS";
	
	// █ FIDALWORD (= GeTaWord) annotations █
	/** Graphical unit id */
	public static final String ID = "ID";
	/** Ge'ez word */
	public static final String FID = "FID";
	/** Ge'ez word with editorial markers */
	public static final String FIDED = "FIDED";
	/** Custom key for HTML rendering of FIDED values */
	public static final String FIDEDh = "FIDEDh";
	// Latin script transcription
	// Duplicate key TR is already accounted for above under Graphical unit annotations
	/** List of ids for division objects */
	public static final String Sid = "Sid";
	/** List of ids for quotation objects */
	public static final String Qid = "Qid";
	/** List of ids for token objects */
	public static final String Tid = "Tid";
	/** Named entity id which this word is a part of */
	public static final String NE = "NE";
	/** Comment */
	public static final String Comm = "Comm";
	/** Array of fidal letter objects */
	public static final String FC = "FC";
	
	/* 
	 * █ FIDALWORD > FC █
	 * Fidal letter annotations
	 */
	/** Fidal letter */
	public static final String FIDLET = "FIDLET";
	/** Fidal letter with editorial markers */
	public static final String FIDLETED = "FIDLETED";
	/** Latin transcription of fidal letter */
	public static final String TRFID = "TRFID";
	/** Line break number */
	public static final String pLB = "pLB";
	/** Page break number */
	public static final String pPB = "pPB";
	/** Editorial annotations */
	public static final String Ed = "Ed";
	/** Array of transliteration letter objects */
	public static final String LL = "LL";
		
	/* 
	 * █ FIDALWORD > FC > LL █
	 * Latin letter annotations
	 */
	/** Latin letter transliteration of fidal letter */
	public static final String LAT = "LAT";
	/** Token id for the latin letter */
	// Duplicate key Tid is already accounted for above under FIDALWORD annotations
	
	// ███ Token annotations ███
	/** Token Id */
	public static final String Id = "Id";
	/** Token label */
	public static final String TOKL = "TOKL";
	/** Id of the named entity to which a token belongs */
	public static final String NEId = "NEId";
	/** Morphological annotation object */
	public static final String M = "M";
	
	/*
	 * █ Token annotations > M █
	 * Morphological token annotations
	 */
	/** Whether the token can be part of a named entity */
	public static final String ne = "ne";
	/** Array of tag objects */
	public static final String LT = "LT";
	
	/*
	 * █ Token annotations > M > LT █
	 * Morphological token annotation tags
	 */
	/** Tag name */
	public static final String NT = "NT";
	/** Array of attribute objects */
	public static final String AL = "AL";

	/*
	 * █ Token annotations > M > LT > AL █
	 * Morphological token annotation tag values
	 */
	/** Gender PATTERN for nominals and numbers */
	public static final String N1 = "N1";
	/** Value of the gender PATTERN */
	public static final String V1 = "V1";
	/** Gender SYNTAX for nominals and numbers */
	public static final String N2 = "N2";
	/** Value of the gender SYNTAX */
	public static final String V2 = "V2";
	/** Gender NATURE for nominals and numbers */
	public static final String N3 = "N3";
	/** Value of the gender NATURE */
	public static final String V3 = "V3";
	/** Attribute name for all but the three above */
	public static final String N = "N";
	/** Attribute value for all but the three above */
	public static final String V = "V";
	
	// ███ Division annotations ███
	// Division Id
	// Duplicate key Id is already accounted for above under token annotations
	/** Id of the word starting the division */
	public static final String WB = "WB";
	/** Id of the word ending the division */
	public static final String WE = "WE";
	/** Internal number */
	public static final String NRI = "NRI";
	/** User-defined number */
	public static final String NR = "NR";
	/** Division level (1-4) */
	public static final String LE = "LE";
	/** Division genre */
	public static final String G = "G";
	/** Division comment */
	public static final String C = "C";
	/** Id of parent division */
	public static final String DP ="DP";
	/** Division name */
	public static final String  NA = "NA";
	/** Creator */
	public static final String CR = "CR";
	/** Id header begin */
	public static final String  HWB = "HWB";
	/** Id header end */
	public static final String  HWE = "HWE";
	/** List of ids of children divisions */
	public static final String DC = "DC";
	
	// ███ Named entity annotations ███
	// NE Id
	// Duplicate key Id is already accounted for above under token annotations
	/** NE id in Beta-Masaheft authority lists */
	public static final String R = "R";
	/** Type of NE */
	public static final String  T = "T";
	/** List of objects pointing to which tokens and graphical units belong to this NE */
	public static final String ref = "ref";
	/** List of other NE features */
	public static final String feat = "feat";
	
	// █ Named entity annotations > ref █
	/** Word id in a RefWord object */
	public static final String WID = "WID";
	/** List of Ids of tokens in the graphical unit with a WID occurring in the NE */
	public static final String TID = "TID";
		
	// ███ Metadata annotations ███
	// Document Id in CLAVIS of Beta-masaheft server 
	// Duplicate key Id is already accounted for above under token annotations
	/** Annotator name */
	public static final String ANNOT = "ANNOT";
	/** Tool name incl. version and author */
	public static final String SOFT = "SOFT";
	/** Document name */
	public static final String NAME = "NAME";
	/** Document language */
	public static final String  LANG = "LANG";
	/** Document date */
	public static final String DATE = "DATE";
	/** Zotero link to edition */
	public static final String EDITION = "EDITION";
	/** List of highest level division included in the document */
	public static final String PARTS = "PARTS";
	// TR already included in FIDALWORDS annotations
	// SCR already included in FIDALWORDS annotations
	// Comment
	// Comm already included elsewhere
	
	// ███ Quotation annotations ███try<String, List<GeTaWord>> sidAndTracesWord : sid2WordsMap.entrySet()) {
	// Quotation id
	// Duplicate key ID is already accounted for above under graphical unit annotations
	/** Reference to the work */
	public static final String REF = "REF";
	/** Id of quotation beginning */
	public static final String QWB = "QWB";
	/** Id of quotation end */
	public static final String QWE = "QWE";
	
	// ###### END KEYS ########
	
	/*
	 * Metadata
	 */
	// GeTa prefix
	private static final String TRACES = "GeTa";
	// Metadata object for document
	private GeTaMetaEA metaea;
	
	/*
	 * FILES
	 */
	// Main file
	private static final String JSON_FILE_SUFFIX = "EA";
	// Token file
	private static final String TEA_FILE_SUFFIX = "TEA";
	// Divisions file
	private static final String DEA_FILE_SUFFIX = "DEA";
	// Named Entities file
	private static final String NEA_FILE_SUFFIX = "NEA";
	// Quotations file
	private static final String QEA_FILE_SUFFIX = "QEA";
	// Metadata file
	private static final String MetaEA_FILE_SUFFIX = "MetaEA";
	private static final String ANN_FILE_ENDING = "ann";
	private static final String JSON_FILE_ENDING = "json";
	
	/*
	 * OBJECTS
	 */
	private final Map<String, GeTaFidalword> fidalwordMap = new HashMap<>();
	private final Map<String, GeTaTEA> teaMap = new HashMap<>();
	private final Map<String, GeTaDEA> deaMap = new HashMap<>();
	private final Map<String, GeTaNEA> neaMap = new HashMap<>();
	private JsonParser jsonParser = null;
	private boolean mapTEA = true;
	private boolean mapDEA = true;
	private boolean mapNEA = true;
	private boolean mapQEA = true;
	private boolean mapMetaEA = true;

	private static final String GETA_NAMESPACE = "GeTa"; 
	private static final String GETA_META_NAMESPACE = GETA_NAMESPACE + "_META";

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
		if (getProperties() instanceof GeTaImporterProperties) {
			this.properties = (GeTaImporterProperties) getProperties();
		}
		boolean mapMetaToAnnos = properties.mapMetadataToAnnotations();
//	Set<SToken> sidTokens = new HashSet<>();
//	// Compile list of tokens governed by words in this sid
//	for (GeTaWord word : sidAndTracesWord.getValue()) {
//		for (String wordTid : word.getTids()) {
//			for (SToken wordToken : tid2TokMap.get(wordTid)) {
//				sidTokens.add(wordToken);
//			}
//		}
//	}
//	SSpan sidSpan = graph.createSpan(new ArrayList<SToken>(sidTokens));
//	annoLayer.addNode(sidSpan);
//	if (mapDEA) {
//		annotateAnnoSpanWithDEAAnnos(sidSpan, sidAndTracesWord.getKey());
//	}
//}
		// Create SDocumentGraph and set source text
		SDocumentGraph graph = SaltFactory.createSDocumentGraph();
		getDocument().setDocumentGraph(graph);
		STextualDS text = SaltFactory.createSTextualDS();
		text.setText("");
		graph.addNode(text);

		// Create layers
		SLayer annoLayer = createLayer(graph, "anno");
//		SLayer basicAnnoLayer = createLayer(graph, "basic-anno");
//		SLayer edLayer = createLayer(graph, "ed-anno");
		SLayer tRLayer = createLayer(graph, "TR");
		SLayer fIDEDLayer = createLayer(graph, "FIDED");

		// Create a parseable String from file
		URI resource = getResourceURI();
		String eaPath = resource.toFileString();
		String teaPath = eaPath.split(JSON_FILE_SUFFIX + ".json")[0].concat(TEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String deaPath = eaPath.split(JSON_FILE_SUFFIX + ".json")[0].concat(DEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String neaPath = eaPath.split(JSON_FILE_SUFFIX + ".json")[0].concat(NEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		// TODO: Implement later
//		String qeaPath = eaPath.split(JSON_FILE_SUFFIX + ".json")[0].concat(QEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String metaeaPath = eaPath.split(JSON_FILE_SUFFIX + ".json")[0].concat(MetaEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		File eaFile = new File(eaPath);
		
		// Check what files to map
		File teaFile = new File(teaPath);
		mapTEA = checkFileExists(teaFile, teaPath);
		
		File deaFile = new File(deaPath);
		mapDEA = checkFileExists(deaFile, deaPath);
		
		File neaFile = new File(neaPath);
		mapNEA = checkFileExists(neaFile, neaPath);

		File metaeaFile = new File(metaeaPath);
		mapMetaEA = checkFileExists(metaeaFile, metaeaPath);

		// TODO: Implement later
//		File qeaFile = new File(qeaPath);
//		mapQEA = checkFileExists(qeaFile, qeaPath);
//		
		
		// Initiate the mapping process for .json and .ann files
		try {
			ObjectMapper eaMapper = new ObjectMapper();
			ObjectMapper teaMapper = new ObjectMapper();
			teaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			ObjectMapper dEAMapper = new ObjectMapper();
			teaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			ObjectMapper neaMapper = new ObjectMapper();
			neaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			ObjectMapper metaeaMapper = new ObjectMapper();
			metaeaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			// TODO: Implement later
//			ObjectMapper qeaMapper = new ObjectMapper();
//			qeaMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // FIXME Remove once all is in
			GeTaEA ea;
			List<GeTaTEA> tea = null;
			List<GeTaDEA> dea = null;
			List<GeTaNEA> nea = null;
			metaea = null;
			// TODO: Implement later
//			List<GeTaQEA> qea = null;
			try {
				// Map the contents of the main file.
				ea = eaMapper.readValue(eaFile, new TypeReference<GeTaEA>() {
				});
				if (mapTEA)
					tea = teaMapper.readValue(teaFile, new TypeReference<List<GeTaTEA>>() {
					});
				if (mapDEA)
					dea = dEAMapper.readValue(deaFile, new TypeReference<List<GeTaDEA>>() {
					});
				 if (mapNEA)
				 nea = neaMapper.readValue(neaFile, new TypeReference<List<GeTaNEA>>() {
				 });
				  if (mapMetaEA)
				  metaea = metaeaMapper.readValue(metaeaFile, new TypeReference<GeTaMetaEA>() {
				  });
				// TODO: Implement later
//				 if (mapQEA)
//				 qea = qeaMapper.readValue(qeaFile, new TypeReference<List<GeTaQEA>>() {
//				 });
			}
			catch (JsonMappingException | JsonParseException e) {
				logger.error("Error while parsing JSON.", e);
				return DOCUMENT_STATUS.FAILED;
			}
			
			// Construct indices of objects
			
			for (GeTaFidalword w : ea.getFidalwords()) {
				fidalwordMap.put(w.getId(), w);
			}
			// FIXME: teaMAP not needed in this direction of mapping?
			if (mapTEA) {
				for (GeTaTEA t : tea) {
					teaMap.put(t.getId(), t);
				}
			}
			if (mapDEA) {
				for (GeTaDEA d : dea) {
					deaMap.put(d.getId(), d);
				}
			}
			if (mapNEA) {
				for (GeTaNEA n : nea) {
					neaMap.put(n.getId(), n);
				}
			}
			// TODO: Implement later
//			if (mapQEA) {
//				for (GeTaQEAAnn qEAann : qEAanns) {
//					qEAannMap.put(qEAann.getId(), qEAann);
//				}
//			}

			/*
			 * ############################ 
			 * Map the JSON objects to Salt
			 * ############################
			 */
			// Map document metadata 
			/* 
			 * Avoid dupllication of keys and resulting SaltExceptions by
			 * suffixing namespace for metadata annotations with "_META".
			 */
			getDocument().createMetaAnnotation(GETA_NAMESPACE, SCR, ea.getSCR());
			getDocument().createMetaAnnotation(GETA_NAMESPACE, TR, ea.getTR());
			for (Entry<String, Object> meta : metaea.getAnnotations().entrySet()) {
				getDocument().createMetaAnnotation(GETA_NAMESPACE + "_META", meta.getKey(), meta.getValue());
			}
			List<String> parts = metaea.getParts();
			if (parts != null) {
				StringBuilder sbParts = new StringBuilder();
				for (String p : metaea.getParts()) {
					sbParts.append(p).append(", ");
				}
				String strParts = sbParts.toString().trim();
				getDocument().createMetaAnnotation(GETA_META_NAMESPACE, PARTS,
						strParts.substring(0, strParts.length() - 1));
			}
			
//			SSpan objectBasicAnnoSpan = null, tRSpan = null, fIDEDSpan = null;
//			Map<String, List<SToken>> tid2TokMap = new HashMap<>();
//			// GeTaWord = entity in FIDALWORDS array
//			Map<String, List<GeTaFidalword>> sid2WordsMap = new HashMap<>();
			
			// A map mapping GeTa Token Ids to lists of STokens
			Map<String, ArrayList<SToken>> tidTokensMap = new HashMap<>();
			// A map mapping GeTa Division Ids to lists of STokens
			Map<String, ArrayList<SToken>> sidTokensMap = new HashMap<>();
			// A map mapping GeTa Division Ids to lists of STokens
			Map<String, ArrayList<SToken>> neTokensMap = new HashMap<>();

			// Iterate through all GeTaWords and map accordingly
			for (GeTaFidalword fidalword : ea.getFidalwords()) {
				List<SToken> fidalwordTokens = new ArrayList<>();
				// FC = Fidal letter
				for (GeTaFC fc : fidalword.getFc()) {
					List<SSpan> fcSpans = new ArrayList<>();
					List<SToken> fcTokens = new ArrayList<>();
					// LL = SToken
					for (GeTaLL ll : fc.getLl()) {
						text.setText(text.getText().concat(ll.getLat()));
						SToken tok = graph.createToken(text, text.getText().length() - ll.getLat().length(), text.getText().length());
						fcTokens.add(tok);
						fidalwordTokens.add(tok);
						// Add the token to the map from Tids to STokens
						ArrayList<SToken> llTidTokenList = null;
						String tid = ll.getTid();
						if ((llTidTokenList = tidTokensMap.get(tid)) != null) {
							llTidTokenList.add(tok);
						}
						else {
							tidTokensMap.put(ll.getTid(), new ArrayList<>(Arrays.asList(new SToken[] {tok})));
						}
						// Add the token to the map from Sids to STokens
						ArrayList<SToken> llSidTokenList = null;
						List<String> sids = fidalword.getSid();
						for (String sid : sids) {
							if ((llSidTokenList = sidTokensMap.get(sid)) != null) {
								llSidTokenList.add(tok);
							}
							else {
								sidTokensMap.put(sid, new ArrayList<>(Arrays.asList(new SToken[] {tok})));
							}
						}
						// Add the token to the map from NE Ids to STokens
						String neId = fidalword.getNe();
						if (neId != null && !neId.isEmpty()) {
							List<SToken> neTokens = neTokensMap.get(neId);
							if (neTokens == null) {
								neTokensMap.put(neId, new ArrayList<>(Arrays.asList(new SToken[]{tok})));
							}
							else {
								neTokens.add(tok);
							}
						}
					}
					// Add FC-level annotations to FC
					SSpan singleFcSpan = graph.createSpan(fcTokens);
					fcSpans.add(singleFcSpan);
					annotateSpan(fc.getAnnotations(), singleFcSpan);
					// Add Ed-level annotations to FC
					GeTaEd ed = fc.getEd();
					if (ed != null) {
						List<GeTaLT> lts = ed.getLt();
						if (lts != null) {
							/* 
							 * Add LT-level annotations to the current FC span
							 */
							for (GeTaLT lt : lts) {
								String nt = lt.getNt();
								List<GeTaAL> als = lt.getAl();
								if (als != null || nt != null) {
									SSpan ltSpan = graph.createSpan(fcTokens);
									if (nt != null && !nt.isEmpty()) {
										ltSpan.createAnnotation(GETA_NAMESPACE, NT, lt.getNt());
									}
									if (als != null) {
										annotateSpanWithALs(als, ltSpan);
									}
								}
							}
						}
					}
				}
				// Add Fidalword-level annotations to Fidalword
				SSpan fidalwordSpan = graph.createSpan(fidalwordTokens);
				annotateSpan(fidalword.getAnnotations(), fidalwordSpan);
				// Fix HTML in FIDED
				String fided = fidalword.getFided();
				fidalwordSpan.createAnnotation(GETA_NAMESPACE, FIDEDh, fided);
				fidalwordSpan.getAnnotation(GETA_NAMESPACE, FIDED).setValue(Jsoup.parse(fided).text());
				text.setText(text.getText().concat(" "));
			}
			
			/*
			 * Connect the Fidal words with their linguistic annotations. The
			 * connection is made via the Tids.
			 */
			for (GeTaTEA t : tea) {
				ArrayList<SToken> teaTokens = tidTokensMap.get(t.getId());
				if (teaTokens != null) {
					SSpan teaSpan = graph.createSpan(teaTokens);
					// Map TEA-level annotations to TEA span
					annotateSpan(t.getAnnotations(), teaSpan);
					GeTaM m = t.getM();
					if (m != null) {
						Boolean mNe = m.getNe();
						if (mNe != null) {
							teaSpan.createAnnotation(GETA_NAMESPACE, ne, mNe);
						}
						List<GeTaLT> lts = m.getLt();
						if (lts != null) {
							for (GeTaLT lt : lts) {
								teaSpan.createAnnotation(GETA_NAMESPACE, NT, lt.getNt());
								List<GeTaAL> als = lt.getAl();
								annotateSpanWithALs(als, teaSpan);
							}
						}
					}
				}
				
			}
			// OLD BELOW
//			for (Entry<String, List<SToken>> tracesToken : tidTokensMap.entrySet()) {
//				SSpan tracesTokenSpan = graph.createSpan(tracesToken.getValue());
//				annoLayer.addNode(tracesTokenSpan);
//				if (mapTEA) {
//					annotateAnnoSpanWithTEAAnnos(tracesTokenSpan, tracesToken.getKey());
//				}
//			}
			
			/*
			 * Connect the Fidel words with their division annotations. The
			 * connection is made via the Sids.
			 */
			for (GeTaDEA d : dea) {
				SSpan deaSpan = graph.createSpan(sidTokensMap.get(d.getId()));
				annotateSpan(d.getAnnotations(), deaSpan);
			}
			
			
//			// OLD BELOW
//			for (Entry<String, List<GeTaFidalword>> sidAndTracesWord : sid2WordsMap.entrySet()) {
//				Set<SToken> sidTokens = new HashSet<>();
//				// Compile list of tokens governed by words in this sid
//				for (GeTaFidalword word : sidAndTracesWord.getValue()) {
//					for (String wordTid : word.getTid()) {
//						for (SToken wordToken : tid2TokMap.get(wordTid)) {
//							sidTokens.add(wordToken);
//						}
//					}
//				}
//				SSpan sidSpan = graph.createSpan(new ArrayList<SToken>(sidTokens));
//				annoLayer.addNode(sidSpan);
//				if (mapDEA) {
//					annotateAnnoSpanWithDEAAnnos(sidSpan, sidAndTracesWord.getKey());
//				}
//			}
			
			/*
			 * Connect Fidal words with named entities
			 */
			
			
		} catch (IOException e) {
			throw new PepperModuleException("Error parsing the JSON file " + eaFile.getName() + "!", e);
		}
		return (DOCUMENT_STATUS.COMPLETED);
	}

	/**
	 * TODO: Description
	 *
	 * @param als
	 * @param teaSpan
	 */
	private void annotateSpanWithALs(List<GeTaAL> als, SSpan teaSpan) {
		if (als != null) {
			for (GeTaAL al : als) {
				annotateSpan(al.getAnnotations(), teaSpan);
			}
		}
	}

	/**
	 * TODO: Description
	 *
	 * @param entrySet
	 * @param span
	 */
	private void annotateSpan(Map<String, ?> annotationMap, SSpan span) {
		for (Entry<String, ?> a : annotationMap.entrySet()) {
			String key = a.getKey();
			if (key == null || key.isEmpty()) {
				continue;
			}
			Object value = a.getValue();
			if (a.getValue() == null) {
				continue;
			}
			else {
				if (a.getValue() instanceof String) {
					String aStr = (String) a.getValue();
					if (aStr.isEmpty()) {
						continue;
					}
				}
			}
			span.createAnnotation(GETA_NAMESPACE, key, value);
		}
	}

	/**
	 * TODO: Description
	 * @param filePath 
	 *
	 * @param fileToCheck
	 */
	private boolean checkFileExists(File fileToCheck, String filePath) {
		if (!fileToCheck.exists()) {
			throw new PepperModuleException("Could not find the " + fileToCheck.getName() + " file at " + filePath + "!");
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileToCheck))){
			String rl = br.readLine();
			if (rl == null) {
				logger.error("No TEA.ann file found or file is empty!");
				return false;
			}
			else if (rl.isEmpty()) {
				logger.error("TEA.ann file is empty!");
				return false;
			}
		}
		catch (IOException e2) {
			logger.error("Error occurred reading the file {}.", fileToCheck.getName(), e2);
		}
		return true;
	}

	/**
	 * TODO: Description
	 *
	 * @param sidSpan
	 * @param sid
	 */
	private void annotateAnnoSpanWithDEAAnnos(SSpan sidSpan, String sid) {
		GeTaDEA dea = deaMap.get(sid);
		if (dea == null) {
			logger.info("Could not find annotations for " + Sid + " " + sid + " near " + jsonParser.getCurrentLocation() + "!");
		}
		else {
			for (Entry<String, String> entry : dea.getAnnotations().entrySet()) {
				sidSpan.createAnnotation(GETA_NAMESPACE, entry.getKey(), entry.getValue());
			}
		}
	}

//	/**
//	 * Maps the annotations from the GeTa .ann annotations file to the
//	 * {@link SSpan}s that are to be annotated.
//	 *
//	 * @param annoSpan
//	 * @param tid
//	 */
//	private void annotateAnnoSpanWithTEAAnnos(SSpan annoSpan, String tid) {
//		GeTaTEA tea = teaMap.get(tid);
//		if (tea == null) {
//			logger.info("Could not find annotations for " + Tid + " " + tid + " near " + jsonParser.getCurrentLocation() + "!");
//		}
//		else {
//			annotateSpan(tea.getAnnotations(), annoSpan);
//			if (tea.getM() != null) {
//			for (GeTaLT lt : tea.getM().getLt()) {
//				annoSpan.createAnnotation(GETA_NAMESPACE, NT, lt.getNt());
//				if (lt.getAl() != null) {
//				for (GeTaAL al : lt.getAl()) {
//					for (Entry<String, String> a : al.getAnnotations().entrySet()) {
//						if (a.getKey() != null && !a.getKey().isEmpty()) {
//							annoSpan.createAnnotation(GETA_NAMESPACE, a.getKey(), a.getValue());
//						}
////						if (nv[1] != null && !nv[1].isEmpty()) {
////							try {
////								/* 
////								 * Special case: split up "lex" annotations which contain
////								 * Tid and actual value separated by a double dash "--".
////								 */
////								if (nv[0].equals("lex")) {
////									annoSpan.createAnnotation(GETA_NAMESPACE, "lem", nv[1].split("--")[1]);
////								}
////								else {
////									/*
////									 *  Create the annotation, and replace whitespaces, which
////									 *  are illegal as annotation keys, with dashes.
////									 */
////									annoSpan.createAnnotation(TRACES, nv[0].replaceAll(" ", "-"), nv[1]);
////								}
////							}
////							catch (SaltInsertionException e) {
////								PepperModuleException ex = new PepperModuleException("Duplicate annotation name in " + getDocument().getName() + "!\n" + "Current annotation: " + TRACES + "::" + nv[0] + "=" + nv[1] + "\n" + "Existing annotation: " + annoSpan.getAnnotation(TRACES, nv[0]) + "\n" + "Token ID: " + tid, e);
////								logger.error("Conversion error: ", ex);
////								throw ex;
////							}
////						}
////						else {
////							logger.trace("Found an empty annotation (Tid: " + tid + "): \"" + nv[0] + "\"! Ignoring it.");
////						}
//					}
//				}}
//			}}
//		}
//	}

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
