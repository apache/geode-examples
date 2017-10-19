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
package org.apache.geode.examples.listener;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;

import org.geode.examples.util.TestEntryEvent;
import org.junit.Test;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Operation;

public class ExampleCacheListenerTest {
  @Test
  public void testAfterCreate() {
    Queue<EntryEvent<Integer, String>> events = new LinkedList<>();
    events.add(new TestEntryEvent<>(null, Operation.CREATE, 1, "one", "uno"));
    events.add(new TestEntryEvent<>(null, Operation.CREATE, 2, "two", "dos"));
    events.add(new TestEntryEvent<>(null, Operation.CREATE, 3, "three", "tres"));

    ExampleCacheListener cacheListener =
        new ExampleCacheListener(new LinkedList<EntryEvent<Integer, String>>());
    for (EntryEvent<Integer, String> event : events) {
      cacheListener.afterCreate(event);
    }

    assertEquals(events, cacheListener.getEvents());
  }
}
