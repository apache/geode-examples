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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;

public class ExampleCacheWriterTest {
  @Test(expected = CacheWriterException.class)
  public void testBeforeCreateFailsForBadSSN() throws Exception {
    ExampleCacheWriter writer = new ExampleCacheWriter();

    EntryEvent<String, String> event = mock(EntryEvent.class);
    when(event.getKey()).thenReturn("666-66-6666");
    writer.beforeCreate(event);
  }

  @Test
  public void testBeforeCreatePassesWithGoodSSN() throws Exception {
    ExampleCacheWriter writer = new ExampleCacheWriter();

    EntryEvent<String, String> event = mock(EntryEvent.class);
    when(event.getKey()).thenReturn("555-66-6666");
    writer.beforeCreate(event);
  }
}
