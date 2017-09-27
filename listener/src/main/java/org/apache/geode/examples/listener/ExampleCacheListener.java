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

import java.util.Queue;

import org.apache.geode.cache.CacheListener;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

public class ExampleCacheListener implements CacheListener<Integer, String> {
  Queue<EntryEvent<Integer, String>> events;

  public ExampleCacheListener(Queue<EntryEvent<Integer, String>> events) {
    this.events = events;
  }

  @Override
  public void afterCreate(EntryEvent<Integer, String> event) {
    events.add(event);
  }

  @Override
  public void afterUpdate(EntryEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterInvalidate(EntryEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterDestroy(EntryEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterRegionInvalidate(RegionEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterRegionDestroy(RegionEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterRegionClear(RegionEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterRegionCreate(RegionEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void afterRegionLive(RegionEvent<Integer, String> event) {
    // NOP
  }

  @Override
  public void close() {
    // NOP
  }
}
