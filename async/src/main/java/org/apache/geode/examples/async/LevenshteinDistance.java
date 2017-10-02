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
package org.apache.geode.examples.async;

public class LevenshteinDistance {
  public int calculate(String s, String t) {
    if (s == null || s.isEmpty())
      return (t == null ? 0 : t.length());
    if (t == null || t.isEmpty())
      return (s == null ? 0 : s.length());

    final String sprime = s.substring(0, s.length() - 1);
    final String tprime = t.substring(0, t.length() - 1);
    final int cost = ((s.charAt(s.length() - 1) == t.charAt(t.length() - 1)) ? 0 : 1);
    return Math.min(Math.min(calculate(sprime, t) + 1, calculate(s, tprime) + 1),
        calculate(sprime, tprime) + cost);
  }
}
