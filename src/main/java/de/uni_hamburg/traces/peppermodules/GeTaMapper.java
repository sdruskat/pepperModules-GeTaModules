/**
 * Copyright 2016ff. Stephan Druskat
 * All exploitation rights belong exclusively to Universität Hamburg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Stephan Druskat - initial API and implementation
 */
package de.uni_hamburg.traces.peppermodules;

import java.io.BufferedReader;    
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import org.corpus_tools.salt.exceptions.SaltInsertionException;
import org.eclipse.emf.common.util.URI;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import de.uni_hamburg.traces.peppermodules.model.nea.GeTaRef;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaAL;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaLT;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaM;
import de.uni_hamburg.traces.peppermodules.model.tea.GeTaTEA;

/**
 * The mapper class doing the actual mapping work.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaMapper extends PepperMapperImpl implements PepperMapper {

	private static final Logger logger = LoggerFactory.getLogger(GeTaMapper.class);

	// ###### KEYS ########
	
	/** Transcription vocalisation */
	public static final String TR = "TR";
	/** Original script type */
	public static final String SCR = "SCR";
	/** Graphical unit words */
	public static final String FIDALWORDS = "FIDALWORDS";

	/** Graphical unit id */
	public static final String ID = "ID";
	/** Ge'ez word */
	public static final String FID = "FID";
	/** Ge'ez word with editorial markers */
	public static final String FIDED = "FIDED";
	/** Custom key for HTML rendering of FIDED values */
	public static final String FIDEDh = "FIDEDh";
	// Latin script transcription
	// Duplicate key TR is already accounted for above under Graphical unit
	// annotations
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

	/** Latin letter transliteration of fidal letter */
	public static final String LAT = "LAT";
	/** Token id for the latin letter */
	// Duplicate key Tid is already accounted for above under FIDALWORD
	// annotations

	/** Token Id */
	public static final String Id = "Id";
	/** Token label */
	public static final String TOKL = "TOKL";
	/** Id of the named entity to which a token belongs */
	public static final String NEId = "NEId";
	/** Morphological annotation object */
	public static final String M = "M";

	/** Whether the token can be part of a named entity */
	public static final String ne = "ne";
	/** Array of tag objects */
	public static final String LT = "LT";

	/** Tag name */
	public static final String NT = "NT";
	/** Array of attribute objects */
	public static final String AL = "AL";

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
	public static final String DP = "DP";
	/** Division name */
	public static final String NA = "NA";
	/** Creator */
	public static final String CR = "CR";
	/** Id header begin */
	public static final String HWB = "HWB";
	/** Id header end */
	public static final String HWE = "HWE";
	/** List of ids of children divisions */
	public static final String DC = "DC";

	/** NE id in Beta-Masaheft authority lists */
	public static final String R = "R";
	/** Type of NE */
	public static final String T = "T";

	/**
	 * List of objects pointing to which tokens and graphical units belong to
	 * this NE
	 */
	public static final String ref = "ref";
	/** List of other NE features */
	public static final String feat = "feat";

	/** Word id in a RefWord object */
	public static final String WId = "WId";
	/**
	 * List of Ids of tokens in the graphical unit with a WID occurring in the
	 * NE
	 */
	public static final String TID = "TID";

	// Document Id in CLAVIS of Beta-masaheft server
	// Duplicate key Id is already accounted for above under token annotations
	/** Annotator name */
	public static final String ANNOT = "ANNOT";
	/** Tool name incl. version and author */
	public static final String SOFT = "SOFT";
	/** Document name */
	public static final String NAME = "NAME";
	/** Document language */
	public static final String LANG = "LANG";
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

	// TODO Quotation annotations
	// Quotation id
	// Duplicate key ID is already accounted for above under graphical unit
	// annotations
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
	// TODO Quotations file
	// private static final String QEA_FILE_SUFFIX = "QEA";
	// Metadata file
	private static final String MetaEA_FILE_SUFFIX = "MetaEA";
	private static final String ANN_FILE_ENDING = "ann";
	private static final String JSON_FILE_ENDING = ".json";

	/*
	 * OBJECTS
	 */
	private boolean mapTEA = true;
	private boolean mapDEA = true;
	private boolean mapNEA = true;
	// TODO To implement
	// private boolean mapQEA = true;
	private boolean mapMetaEA = true;

	// NAMESPACES
	private static final String GETA_NAMESPACE = "GeTa";
	private static final String GETA_META_NAMESPACE = GETA_NAMESPACE + "_META";
	private static final String GETA_NAMESPACE_TEA = GETA_NAMESPACE + "_TEA";
	private static final String GETA_NAMESPACE_DEA = GETA_NAMESPACE + "_DEA";
	private static final String GETA_NAMESPACE_NEA = GETA_NAMESPACE + "_NEA";
	// TODO Rename to include "Ed"
	private static final String GETA_NAMESPACE_LT_ALS = GETA_META_NAMESPACE + "_LT_ALS";
	private static final String GETA_NAMESPACE_TEA_LT_ALS = GETA_NAMESPACE_TEA + "_LT_ALS";
	private static final String GETA_NAMESPACE_NEA_FEAT_ALS = GETA_NAMESPACE_NEA + "_FEAT_ALS";
	// TODO To implement
	// private static final String GETA_NAMESPACE_QEA = GETA_NAMESPACE + "_QEA";
	
	// Special annotation values
	// "lex" N:V attribute
	private static final String lex = "lex";
	// HTMLified lex annotation
	private static final String lexh = "lexh";
	private static final String vocalized = "vocalized";
	private static final String unvocalized = "unvocalized";
	private static final String GEEZ = "Ge'ez";
	private static final String SOUTH_ARABIAN = "SouthArabian";
	// Token-based Named Entity annotation
	private static final String NET = "NET";



	/*
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperMapperImpl#mapSCorpus()
	 */
	@Override
	public DOCUMENT_STATUS mapSCorpus() {
		return super.mapSCorpus(); // (DOCUMENT_STATUS.COMPLETED);
	}

	/*
	 * @copydoc @see org.corpus_tools.pepper.impl.PepperMapperImpl#mapS()
	 */
	@Override
	public DOCUMENT_STATUS mapSDocument() {
		SDocumentGraph graph = SaltFactory.createSDocumentGraph();
		getDocument().setDocumentGraph(graph);
		STextualDS text = SaltFactory.createSTextualDS();
		text.setText("");
		graph.addNode(text);

		// Create a parseable String from file
		URI resource = getResourceURI();
		String eaPath = resource.toFileString();
		String teaPath = eaPath.split(JSON_FILE_SUFFIX + JSON_FILE_ENDING)[0].concat(TEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String deaPath = eaPath.split(JSON_FILE_SUFFIX + JSON_FILE_ENDING)[0].concat(DEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String neaPath = eaPath.split(JSON_FILE_SUFFIX + JSON_FILE_ENDING)[0].concat(NEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		// TODO: Implement later
		// String qeaPath = eaPath.split(JSON_FILE_SUFFIX +
		// ".json")[0].concat(QEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
		String metaeaPath = eaPath.split(JSON_FILE_SUFFIX + JSON_FILE_ENDING)[0]
				.concat(MetaEA_FILE_SUFFIX + "." + ANN_FILE_ENDING);
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

		// TODO: Implement
		// File qeaFile = new File(qeaPath);
		// mapQEA = checkFileExists(qeaFile, qeaPath);
		//

		// Initiate the mapping process for .json and .ann files
		try {
			ObjectMapper eaMapper = new ObjectMapper();
			ObjectMapper teaMapper = new ObjectMapper();
			ObjectMapper dEAMapper = new ObjectMapper();
			ObjectMapper neaMapper = new ObjectMapper();
			ObjectMapper metaeaMapper = new ObjectMapper();
			// TODO: Implement
			// ObjectMapper qeaMapper = new ObjectMapper();
			GeTaEA ea;
			List<GeTaTEA> tea = null;
			List<GeTaDEA> dea = null;
			List<GeTaNEA> nea = null;
			metaea = null;
			// TODO: Implement
			// List<GeTaQEA> qea = null;
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
				// TODO: Implement
				// if (mapQEA)
				// qea = qeaMapper.readValue(qeaFile, new
				// TypeReference<List<GeTaQEA>>() {
				// });
			}
			catch (JsonMappingException | JsonParseException e) {
				logger.error("Error while parsing JSON.", e);
				return DOCUMENT_STATUS.FAILED;
			}

			/*
			 * ### Map the JSON objects to Salt ###
			 */
			// Map document metadata
			/*
			 * Avoid dupllication of keys and resulting SaltExceptions by
			 * suffixing namespace for metadata annotations with "_META".
			 */
			getDocument().createMetaAnnotation(GETA_NAMESPACE, SCR, ea.getSCR() == 0 ? GEEZ : SOUTH_ARABIAN);
			getDocument().createMetaAnnotation(GETA_NAMESPACE, TR, ea.getTR() == 0 ? vocalized : unvocalized);
			if (mapMetaEA) {
				for (Entry<String, Object> meta : metaea.getAnnotations().entrySet()) {
					getDocument().createMetaAnnotation(GETA_META_NAMESPACE, meta.getKey(), meta.getValue());
				}
				// Map SCR and TR customly
				getDocument().createMetaAnnotation(GETA_META_NAMESPACE, SCR, ea.getSCR() == 0 ? GEEZ : SOUTH_ARABIAN);
				getDocument().createMetaAnnotation(GETA_META_NAMESPACE, TR, ea.getTR() == 0 ? vocalized : unvocalized);
				// ID contains a URL
				if (metaea.getId() != null) {
					String rawValue = metaea.getId();
					boolean isURL = GeTaUtil.isValidJavaNetURL(rawValue);
					if (isURL) {
						getDocument().createMetaAnnotation(GETA_NAMESPACE + "_META", ID, "<a href =\"" + rawValue + "\">" + rawValue + "</a>");
					}
					else {
						getDocument().createMetaAnnotation(GETA_NAMESPACE + "_META", ID, rawValue);
					}
				}
				List<String> parts = metaea.getParts();
				if (parts != null) {
					String partsString = StringUtil.join(metaea.getParts(), ",");
					getDocument().createMetaAnnotation(GETA_META_NAMESPACE, PARTS, partsString);
				}
			}

			// A map mapping fidalword ids to spans spanning that fidalword
			Map<String, SSpan> fidalwordSpanIndex = new HashMap<>();
			// A map mapping GeTa Token Ids from Graphical Units (words) to lists of STokens
			Map<String, ArrayList<SToken>> tidTokensMap = new HashMap<>();
			// A map mapping GeTa Division Ids to lists of STokens
			Map<String, ArrayList<SToken>> sidTokensMap = new HashMap<>();
			// A map mapping GeTa word-based Named Entity Ids to lists of STokens
			Map<String, ArrayList<SToken>> wordNETokensMap = new HashMap<>();
			
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
						SToken tok = graph.createToken(text, text.getText().length() - ll.getLat().length(),
								text.getText().length());
						fcTokens.add(tok);
						fidalwordTokens.add(tok);
						// Add the token to the map from Tids to STokens
						ArrayList<SToken> llTidTokenList = null;
						String tid = ll.getTid();
						if ((llTidTokenList = tidTokensMap.get(tid)) != null) {
							llTidTokenList.add(tok);
							tidTokensMap.put(ll.getTid(), llTidTokenList);
						}
						else {
							tidTokensMap.put(ll.getTid(), new ArrayList<>(Arrays.asList(new SToken[] { tok })));
						}
						// Add the token to the map from Sids to STokens
						ArrayList<SToken> llSidTokenList = null;
						List<String> sids = fidalword.getSid();
						for (String sid : sids) {
							if ((llSidTokenList = sidTokensMap.get(sid)) != null) {
								llSidTokenList.add(tok);
							}
							else {
								sidTokensMap.put(sid, new ArrayList<>(Arrays.asList(new SToken[] { tok })));
							}
						}
						// Add the token to the map from NE Ids to STokens
						String neId = fidalword.getNe();
						if (neId != null && !neId.isEmpty()) {
							List<SToken> neTokens = wordNETokensMap.get(neId);
							if (neTokens == null) {
								wordNETokensMap.put(neId, new ArrayList<>(Arrays.asList(new SToken[] { tok })));
							}
							else {
								neTokens.add(tok);
							}
						}
					}
					// Add FC-level annotations to FC
					SSpan singleFcSpan = graph.createSpan(fcTokens);
					fcSpans.add(singleFcSpan);
					annotateSpan(fc.getAnnotations(), singleFcSpan, GETA_NAMESPACE);
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
										for (GeTaAL al : als) {
											annotateSpan(al.getAnnotations(), ltSpan, GETA_NAMESPACE_LT_ALS);
										}
									}
								}
							}
						}
					}
				}
				// Add Fidalword-level annotations to Fidalword
				SSpan fidalwordSpan = graph.createSpan(fidalwordTokens);
				annotateSpan(fidalword.getAnnotations(), fidalwordSpan, GETA_NAMESPACE);
				fidalwordSpanIndex.put(fidalword.getId(), fidalwordSpan);
				/* 
				 * Need an extra span just for TR annotations to make
				 * multiple segmentation visualization work
				 */
				SSpan trSpan = graph.createSpan(fidalwordTokens);
				trSpan.createAnnotation(GETA_NAMESPACE, TR, fidalword.getTr());
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
			if (mapTEA) {
				for (GeTaTEA t : tea) {
					String id = t.getId();
					ArrayList<SToken> teaTokens = tidTokensMap.get(id);
					if (teaTokens != null) {
						SSpan teaSpan = graph.createSpan(teaTokens);
						// Map TEA-level annotations to TEA span
						annotateSpan(t.getAnnotations(), teaSpan, GETA_NAMESPACE_TEA);
						GeTaM m = t.getM();
						if (m != null) {
							Boolean mNe = m.getNe();
							if (mNe != null) {
								teaSpan.createAnnotation(GETA_NAMESPACE_TEA, ne, mNe);
							}
							List<GeTaLT> lts = m.getLt();
							if (lts != null) {
								for (GeTaLT lt : lts) {
									teaSpan.createAnnotation(GETA_NAMESPACE_TEA, NT, lt.getNt());
									List<GeTaAL> als = lt.getAl();
									annotateSpanWithALs(als, teaSpan, GETA_NAMESPACE_TEA_LT_ALS);
								}
							}
						}
						/* 
						 * If the attribute name is "lex", it is likely to contain
						 * a Dillmann URL.
						 */
						// R annotations contain URLs to the Beta-Masaheft lexicon
						SAnnotation lexAnnotation = teaSpan.getAnnotation(GETA_NAMESPACE_TEA_LT_ALS, lex); 
						if (lexAnnotation != null) {
							String rawValue = lexAnnotation.getValue_STEXT();
							String[] splitLemmaURL = rawValue.split("\\s+");
							if (splitLemmaURL.length == 2) {
								/* 
								 * Possibly contains Lemma and URL, e.g.
								 * "መኰንን    http://betamasaheft.eu/..."
								 */
								String potentialURL = splitLemmaURL[1];
								boolean isURL = GeTaUtil.isValidJavaNetURL(potentialURL);
								if (isURL) {
									lexAnnotation.setValue(splitLemmaURL[0]);
									teaSpan.createAnnotation(GETA_NAMESPACE_TEA_LT_ALS, lexh, "<a href=\"" + potentialURL + "\">" + splitLemmaURL[0] + "</a>");
								}
							}
							else {
								// Leave as is
							}
						}
					}

				}
			}

			/*
			 * Connect the Fidel words with their division annotations. The
			 * connection is made via the Sids.
			 */
			if (mapDEA) {
				for (GeTaDEA d : dea) {
					SSpan deaSpan = graph.createSpan(sidTokensMap.get(d.getId()));
					annotateSpan(d.getAnnotations(), deaSpan, GETA_NAMESPACE_DEA);
				}
			}


			/*
			 * Connect Fidal words with named entities
			 */
			if (mapNEA) {
				for (GeTaNEA ne : nea) {
					// Build spans to receive annotations
					List<SSpan> refTokenSpans = new ArrayList<>();
					List<SSpan> refWordSpans = new ArrayList<>();
					for (GeTaRef neRef : ne.getRef()) {
						/* 
						 * Create spans for Named Entity only over those
						 * tokens that are explicitly linked in the NE
						 */
						List<String> tokenIds = neRef.getTid();
						List<SToken> tokenSTokenList = new ArrayList<>();
						for (String tid : tokenIds) {
							tokenSTokenList.addAll(tidTokensMap.get(tid));
						}
						SSpan tokenNESpan = graph.createSpan(tokenSTokenList);
						refTokenSpans.add(tokenNESpan);
						// Add the span for the respective fidalword to a list
						SSpan refWordSpan = fidalwordSpanIndex.get(neRef.getWid());
						if (refWordSpan != null) {
							refWordSpans.add(refWordSpan);
						}
					}
					// Annotate
					// Test R value for URL
					String rawValue = ne.getR();
					boolean isURL = GeTaUtil.isValidJavaNetURL(rawValue);
					for (SSpan span : refTokenSpans) {
						annotateSpan(ne.getAnnotations(), span, GETA_NAMESPACE_NEA);
						// R annotations contain URLs to the Beta-Masaheft
						// lexicon
						SAnnotation rAnnotation = span.getAnnotation(GETA_NAMESPACE_NEA, R);
						if (rAnnotation != null) {
							if (isURL) {
								rAnnotation.setValue("<a href=\"" + rawValue + "\">" + rawValue + "</a>");
							}
						}
						List<GeTaAL> neFeat = ne.getFeat();
						if (neFeat != null) {
							for (GeTaAL al : neFeat) {
								annotateSpan(al.getAnnotations(), span, GETA_NAMESPACE_NEA_FEAT_ALS);
							}
						}
					}
					if (rawValue != null && !rawValue.isEmpty()) {
						for (SSpan span : refWordSpans) {
							// Annotate the word span with NET annotation containing
							// raw R value
							try {
								span.createAnnotation(GETA_NAMESPACE_NEA, NET, rawValue);
							}
							catch (SaltInsertionException e) {
								logger.warn("Duplicate annotation caught, not adding: {}.", NET + ":" + rawValue	);
							}
						}
					}
				}
			}

		}
		catch (IOException e) {
			throw new PepperModuleException("Error parsing the JSON file " + eaFile.getName() + "!", e);
		}
		return (DOCUMENT_STATUS.COMPLETED);
	}

	/**
	 * Calls {@link #annotateSpan(Map, SSpan, String)} on all
	 * AL objects for a span.
	 *
	 * @param als The list of {@link GeTaAL} objects containing annotations for the span
	 * @param span The span to be annotated
	 * @param namespace The Salt annotation namespace to use for the annotations 
	 */
	private void annotateSpanWithALs(List<GeTaAL> als, SSpan span, String namespace) {
		if (als != null) {
			for (GeTaAL al : als) {
				annotateSpan(al.getAnnotations(), span, namespace);
			}
		}
	}

	/**
	 * Annotates a span with the passed annotation map.  
	 * The map maps annotation keys to annotation values.  
	 * This method performs checks on the annotation map,
	 * i.e., whether the key/value is `null` or emtpy.
	 * 
	 * Only if both key and value of the respective entry
	 * of the annotations map which is iterated is neither `null`
	 * nor empty is the annotation created.
	 *
	 * @param annotationMap The key:value map of annotations to apply to the span
	 * @param span The span to be annotated
	 * @param namespace The Salt annotation namespace to be used for the annotations
	 */
	private void annotateSpan(Map<String, ?> annotationMap, SSpan span, String namespace) {
		for (Entry<String, ?> a : annotationMap.entrySet()) {
			String key = a.getKey();
			if (key == null || key.isEmpty()) {
				continue;
			}
			else {
				key = key.replaceAll("\\s", "-");
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
			span.createAnnotation(namespace, key, value);
		}
	}

	/**
	 * Checks if a file exists and is not empty.
	 *
	 * @param fileToCheck The file to check
	 * @param filePath The path of the file to check
	 * @return `true` iff the file exists and is not empty, else `false`.
	 */
	private boolean checkFileExists(File fileToCheck, String filePath) {
		String name = fileToCheck.getName();
		if (!fileToCheck.exists()) {
			logger.error("No {} file found or file is empty!", name);
			return false;
		}
		try (BufferedReader br = new BufferedReader(new FileReader(fileToCheck))) {
			String rl = br.readLine();
			if (rl == null) {
				logger.error("No {} file found or file is empty!", name);
				return false;
			}
			else if (rl.isEmpty()) {
				logger.error("{} file is empty!", name);
				return false;
			}
		}
		catch (IOException e2) {
			logger.error("Error occurred reading the file {}.", name, e2);
		}
		return true;
	}

}
