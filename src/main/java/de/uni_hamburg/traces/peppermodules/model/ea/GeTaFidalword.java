/*******************************************************************************
 * Copyright 2018 Universität Hamburg
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

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;
import de.uni_hamburg.traces.peppermodules.GeTaUtil;

/**
 * TODO Description
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaFidalword {

	private String id;
	private String fid;
	private String fided;
	private String tr;
	private List<String> sid;
	private List<String> qid;
	private List<String> tid;
	private String ne;
	private String comm;
	private List<GeTaFC> fc;

	private final Map<String, String> annotations = new HashMap<>();

	@JsonCreator
	@JsonIgnoreProperties(ignoreUnknown = true)
	public GeTaFidalword(@JsonProperty(GeTaMapper.Id) String id, 
			@JsonProperty(GeTaMapper.FID) String fid,
			@JsonProperty(GeTaMapper.FIDED) String fided, 
			@JsonProperty(GeTaMapper.TR) String tr,
			@JsonProperty(GeTaMapper.Sid) List<String> sid, 
			@JsonProperty(GeTaMapper.Qid) List<String> qid,
			@JsonProperty(GeTaMapper.Tid) List<String> tid, 
			@JsonProperty(GeTaMapper.NE) String ne,
			@JsonProperty(GeTaMapper.Comm) String comm, 
			@JsonProperty(GeTaMapper.FC) List<GeTaFC> fc) {
		this.id = id;
		this.sid = sid;
		this.qid = qid;
		this.tid = tid;
		this.fc = fc;
		this.fided = fided;
		this.ne = ne;
		/* 
		 * Treat TR extra, as it will have to be applied
		 * to its own span to make multiple segmentation
		 * visualization work.
		 */
		this.tr = tr;
		annotations.put(GeTaMapper.Id, id);
		annotations.put(GeTaMapper.FID, fid);
		annotations.put(GeTaMapper.FIDED, fided);
		// Exclude TR otherwise it'll be duplicated in ANNIS grid
//		annotations.put(GeTaMapper.TR, tr);
		annotations.put(GeTaMapper.Sid, GeTaUtil.join(sid));
		annotations.put(GeTaMapper.Qid, GeTaUtil.join(qid));
		annotations.put(GeTaMapper.Tid, GeTaUtil.join(tid));
		annotations.put(GeTaMapper.NE, ne);
		annotations.put(GeTaMapper.Comm, comm);
		// Remove null values from map
		Iterables.removeIf(annotations.keySet(), Predicates.isNull());
	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @return the fid
	 */
	public final String getFid() {
		return fid;
	}

	/**
	 * @return the fided
	 */
	public final String getFided() {
		return fided;
	}

	/**
	 * @return the tr
	 */
	public final String getTr() {
		return tr;
	}

	/**
	 * @return the sid
	 */
	public final List<String> getSid() {
		return sid;
	}

	/**
	 * @return the qid
	 */
	public final List<String> getQid() {
		return qid;
	}

	/**
	 * @return the tid
	 */
	public final List<String> getTid() {
		return tid;
	}

	/**
	 * @return the ne
	 */
	public final String getNe() {
		return ne;
	}

	/**
	 * @return the comm
	 */
	public final String getComm() {
		return comm;
	}

	/**
	 * @return the fc
	 */
	public final List<GeTaFC> getFc() {
		return fc;
	}

	/**
	 * @return the annotations
	 */
	public final Map<String, String> getAnnotations() {
		return annotations;
	}

}
