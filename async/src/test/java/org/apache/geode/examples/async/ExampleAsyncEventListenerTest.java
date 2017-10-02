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
import java.util.List;

import org.geode.examples.util.TestAsyncEvent;
import org.junit.Test;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.asyncqueue.AsyncEvent;

public class ExampleAsyncEventListenerTest {
  @Test
  public void testAfterCreate() {
    ExampleAsyncEventListener listener = new ExampleAsyncEventListener();
    List<AsyncEvent> events = new LinkedList<AsyncEvent>();
    events.add(new TestAsyncEvent<Integer, String>(null, Operation.CREATE, 1, "teh"));
    events.add(new TestAsyncEvent<Integer, String>(null, Operation.CREATE, 2, "wil"));
    events.add(new TestAsyncEvent<Integer, String>(null, Operation.CREATE, 3, "i"));
    assertEquals(true, listener.processEvents(events));
  }

  @Test
  public void testSpellCheck() {
    ExampleAsyncEventListener listener = new ExampleAsyncEventListener();
    assertEquals("that", listener.spellCheck("that"));
    assertEquals("the", listener.spellCheck("teh"));
    assertEquals("will", listener.spellCheck("wil"));
    assertEquals("I", listener.spellCheck("i"));
  }
}
