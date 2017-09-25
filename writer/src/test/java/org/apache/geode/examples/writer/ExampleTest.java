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
package org.apache.geode.examples.writer;

import static org.junit.Assert.assertEquals;

import org.apache.geode.cache.Region;

import org.geode.examples.util.Mocks;
import org.junit.Test;

public class ExampleTest {
  @Test
  public void testExample() throws Exception {
    Example example = new Example();

    Region<String, String> region = Mocks.region("example-region");

    example.accept(region);
    assertEquals(true, example.getNames().contains("Bart Simpson"));
    assertEquals(true, example.getNames().contains("Raymond Babbitt"));
  }
}
