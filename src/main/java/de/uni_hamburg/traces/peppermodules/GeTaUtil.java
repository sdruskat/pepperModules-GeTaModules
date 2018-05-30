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

import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * A static util class for the GeTaModules.
 *
 * @author Stephan Druskat <mail@sdruskat.net>
 *
 */
public class GeTaUtil {

	/**
	 * Joins a list of strings.
	 *
	 * @param list The list of string to join
	 * @return The joined string
	 */
	public static String join(List<String> list) {
		return StringUtils.join(list, ",");
	}

	/**
	 * Tests whether a string represents a valid {@link java.net.URL}.
	 *
	 * @param rawValue THe raw string to check for validity
	 * @return Whether the string represents a valid {@link java.net.URL}
	 */
	public static boolean isValidJavaNetURL(String rawValue) {
		if (rawValue != null) {
			try {
				new URL(rawValue);
				return true;
			}
			catch (Exception e) {
				// R value is not a valid URL, so leave as is.
				return false;
			}
		}
		else {
			return false;
		}
	}

}
