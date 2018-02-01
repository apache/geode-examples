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

import org.apache.geode.cache.CacheWriter;
import org.apache.geode.cache.CacheWriterException;
import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.RegionEvent;

public class ExampleCacheWriter implements CacheWriter<String, String> {
  final SSNVetter vetter = new SSNVetter();

  @Override
  public void beforeUpdate(EntryEvent<String, String> event) throws CacheWriterException {
    if (!vetter.isValid(event.getKey())) {
      throw new CacheWriterException("Invalid SSN");
    }
  }

  @Override
  public void beforeCreate(EntryEvent<String, String> event) throws CacheWriterException {
    if (!vetter.isValid(event.getKey())) {
      throw new CacheWriterException("Invalid SSN");
    }
  }

  @Override
  public void beforeDestroy(EntryEvent<String, String> event) throws CacheWriterException {
    // N/A
  }

  @Override
  public void beforeRegionDestroy(RegionEvent<String, String> event) throws CacheWriterException {
    // N/A
  }

  @Override
  public void beforeRegionClear(RegionEvent<String, String> event) throws CacheWriterException {
    // N/A
  }

  @Override
  public void close() {
    // N/A
  }
}
