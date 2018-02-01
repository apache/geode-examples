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

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.wan.EventSequenceID;

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

  public class TestAsyncEvent<K, V> implements AsyncEvent<K, V> {
    private final Region region;
    private final Operation operation;
    private final K key;
    private final V value;

    public TestAsyncEvent(Region region, Operation operation, K key, V value) {
      this.region = region;
      this.operation = operation;
      this.key = key;
      this.value = value;
    }

    @Override
    public boolean getPossibleDuplicate() {
      return false;
    }

    @Override
    public EventSequenceID getEventSequenceID() {
      return null;
    }

    @Override
    public Region<K, V> getRegion() {
      return region;
    }

    @Override
    public Operation getOperation() {
      return operation;
    }

    @Override
    public Object getCallbackArgument() {
      return null;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getDeserializedValue() {
      return value;
    }

    @Override
    public byte[] getSerializedValue() {
      return new byte[0];
    }
  }
}
