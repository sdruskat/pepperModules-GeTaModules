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
package de.uni_hamburg.traces.peppermodules;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * TODO Description
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaUtil {

	/**
	 * TODO: Description
	 *
	 * @param sid
	 * @return
	 */
	public static String join(List<String> list) {
		return StringUtils.join(list, ",");
	}

}
