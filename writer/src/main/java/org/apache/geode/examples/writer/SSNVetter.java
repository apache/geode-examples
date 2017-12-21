/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.geode_examples.writer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SSNVetter {
  private final Pattern ssnPattern = Pattern.compile("^\\s*([0-8]\\d{2})-?\\d{2}-?\\d{4}\\s*$");

  public boolean isValid(String text) {
    final Matcher m = ssnPattern.matcher(text);
    if (m.matches() && !m.group(1).equals("666")) {
      return true;
    }
    return false;
  }
}
