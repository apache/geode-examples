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

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;

import org.geode.examples.util.TestEntryEvent;
import org.junit.Test;

import org.apache.geode.cache.Operation;

public class ExampleCacheListenerTest {
  @Test
  public void testAfterCreate() {
    ExampleCacheListener listener = new ExampleCacheListener(new LinkedList<>());
    listener.afterCreate(
        new TestEntryEvent<String, String>(null, Operation.CREATE, "teh", null, "the"));
    listener.afterCreate(
        new TestEntryEvent<String, String>(null, Operation.CREATE, "wil", null, "will"));
    listener
        .afterCreate(new TestEntryEvent<String, String>(null, Operation.CREATE, "i", null, "I"));
    assertEquals(3, listener.getEvents().size());
    assertEquals(Operation.CREATE, listener.getEvents().element().getOperation());
    assertEquals("teh", listener.getEvents().element().getKey());
    assertEquals("the", listener.getEvents().element().getNewValue());
    listener.getEvents().remove();
    assertEquals(Operation.CREATE, listener.getEvents().element().getOperation());
    assertEquals("wil", listener.getEvents().element().getKey());
    assertEquals("will", listener.getEvents().element().getNewValue());
    listener.getEvents().remove();
    assertEquals(Operation.CREATE, listener.getEvents().element().getOperation());
    assertEquals("i", listener.getEvents().element().getKey());
    assertEquals("I", listener.getEvents().element().getNewValue());
    listener.getEvents().remove();
  }
}
