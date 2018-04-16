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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.uni_hamburg.traces.peppermodules.GeTaMapper;

/**
 * An object representation of a JSON object from a GeTa *TEA.ann annotation file.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaDEAAnn {
	
	private static final Logger logger = LoggerFactory.getLogger(GeTaDEAAnn.class);
	private String id;
	private String wb;
	private String we;
	private String nri;
	private String nr;
	private String le;
	private String g;
	private String c;
	private String dp;
	private String na;
	private String cr;
	private String hwb;
	private String hwe;
	private String dc;
	

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
	public GeTaDEAAnn(@JsonProperty(GeTaMapper.Id) String id, 
			@JsonProperty(GeTaMapper.WB) String wb,
			@JsonProperty(GeTaMapper.WE) String we, 
			@JsonProperty(GeTaMapper.NRI) String nri, 
			@JsonProperty(GeTaMapper.NR) String nr, 
			@JsonProperty(GeTaMapper.LE) String le, 
			@JsonProperty(GeTaMapper.G) String g, 
			@JsonProperty(GeTaMapper.C) String c, 
			@JsonProperty(GeTaMapper.DP) String dp, 
			@JsonProperty(GeTaMapper. NA) String na, 
			@JsonProperty(GeTaMapper.CR) String cr, 
			@JsonProperty(GeTaMapper. HWB) String hwb, 
			@JsonProperty(GeTaMapper. HWE) String hwe, 
			@JsonProperty(GeTaMapper.DC) String dc) {
		this.id = id;
		this.wb = wb;
		this.we = we;
		this.nri = nri;
		this.nr = nr;
		this.le = le;
		this.g = g;
		this.c = c;
		this.dp = dp;
		this.na = na;
		this.cr = cr;
		this.hwb = hwb;
		this.hwe = hwe;
		this.dc = dc;
	}


	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}


	/**
	 * @return the wb
	 */
	public final String getWB() {
		return wb;
	}


	/**
	 * @return the we
	 */
	public final String getWE() {
		return we;
	}


	/**
	 * @return the nri
	 */
	public final String getNRI() {
		return nri;
	}


	/**
	 * @return the nr
	 */
	public final String getNR() {
		return nr;
	}


	/**
	 * @return the le
	 */
	public final String getLE() {
		return le;
	}


	/**
	 * @return the g
	 */
	public final String getG() {
		return g;
	}


	/**
	 * @return the c
	 */
	public final String getC() {
		return c;
	}


	/**
	 * @return the dp
	 */
	public final String getDP() {
		return dp;
	}


	/**
	 * @return the na
	 */
	public final String getNA() {
		return na;
	}


	/**
	 * @return the cr
	 */
	public final String getCR() {
		return cr;
	}


	/**
	 * @return the hwb
	 */
	public final String getHWB() {
		return hwb;
	}


	/**
	 * @return the hwe
	 */
	public final String getHWE() {
		return hwe;
	}


	/**
	 * @return the dc
	 */
	public final String getDC() {
		return dc;
	}

}