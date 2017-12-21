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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.apache.geode.cache.Region;

import org.junit.Test;

public class ExampleTest {
  @Test
  public void testExample() throws Exception {
    Example example = new Example();

    Region<String, String> outgoingRegion = mock(Region.class);
    Region<Integer, String> incomingRegion = mock(Region.class);

    final List<String> words = Arrays.asList(new String[] {"that", "teh"});
    when(outgoingRegion.sizeOnServer()).thenReturn(words.size());
    example.checkWords(incomingRegion, outgoingRegion, words);

    verify(incomingRegion).put(eq(0), eq(words.get(0)));
    verify(incomingRegion).put(eq(1), eq(words.get(1)));
  }
}
