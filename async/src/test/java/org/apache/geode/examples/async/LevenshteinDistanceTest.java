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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LevenshteinDistanceTest {
  @Test
  public void testCalculate() throws Exception {
    LevenshteinDistance distance = new LevenshteinDistance();
    assertEquals(0, distance.calculate(null, null));
    assertEquals(0, distance.calculate(null, ""));
    assertEquals(0, distance.calculate("", null));
    assertEquals(0, distance.calculate("", ""));
    assertEquals(3, distance.calculate(null, "foo"));
    assertEquals(3, distance.calculate("foo", null));
    assertEquals(3, distance.calculate("", "foo"));
    assertEquals(3, distance.calculate("foo", ""));
    assertEquals(3, distance.calculate("foo", "bar"));
    assertEquals(2, distance.calculate("foo", "ofo"));
    assertEquals(2, distance.calculate("foo", "oof"));
    assertEquals(1, distance.calculate("the", "th"));
    assertEquals(1, distance.calculate("the", "he"));
    assertEquals(2, distance.calculate("the", "teh"));
    assertEquals(2, distance.calculate("project", "porject"));
  }
}
