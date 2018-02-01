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
package org.apache.geode_examples.async;

/**
 * The Levenshtein distance is a measure of the difference between two strings of characters. It can
 * be useful in determining when two strings are very much alike, e.g., a transposed character.
 * While not as powerful as other techniques, one use is simple spell-checking.
 */
public class LevenshteinDistance {
  public int calculate(String first, String second) {
    if (first == null || first.isEmpty())
      return (second == null ? 0 : second.length());
    if (second == null || second.isEmpty())
      return (first == null ? 0 : first.length());

    final String firstPrime = first.substring(0, first.length() - 1);
    final String secondPrime = second.substring(0, second.length() - 1);
    final int cost =
        ((first.charAt(first.length() - 1) == second.charAt(second.length() - 1)) ? 0 : 1);
    return Math.min(Math.min(calculate(firstPrime, second) + 1, calculate(first, secondPrime) + 1),
        calculate(firstPrime, secondPrime) + cost);
  }
}
