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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.Region;

import org.junit.Test;

public class ExampleTest {
  @Test
  public void testExample() throws Exception {
    Example example = new Example();

    Region<String, String> region = mock(Region.class);
    when(region.put(eq("666-66-6666"), any())).thenThrow(new CacheWriterException());
    when(region.put(eq("8675309"), any())).thenThrow(new CacheWriterException());
    when(region.put(eq("999-000-0000"), any())).thenThrow(new CacheWriterException());

    assertEquals(Arrays.asList(new String[] {"Bart Simpson", "Raymond Babbitt"}),
        example.getValidNames(region));
  }
}
