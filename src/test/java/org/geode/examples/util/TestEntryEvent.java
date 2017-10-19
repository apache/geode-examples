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
package org.geode.examples.util;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.Operation;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.SerializedCacheValue;
import org.apache.geode.cache.TransactionId;
import org.apache.geode.distributed.DistributedMember;

public class TestEntryEvent<K, V> implements EntryEvent<K, V> {
  final Region region;
  final Operation operation;
  final K key;
  final V oldValue;
  final V newValue;

  public TestEntryEvent(Region region, Operation operation, K key, V oldValue, V newValue) {
    this.region = region;
    this.operation = operation;
    this.key = key;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getOldValue() {
    return oldValue;
  }

  @Override
  public SerializedCacheValue getSerializedOldValue() {
    return null;
  }

  @Override
  public V getNewValue() {
    return newValue;
  }

  @Override
  public SerializedCacheValue getSerializedNewValue() {
    return null;
  }

  @Override
  public TransactionId getTransactionId() {
    return null;
  }

  @Override
  public boolean hasClientOrigin() {
    return false;
  }

  @Override
  public boolean isOldValueAvailable() {
    return true;
  }

  @Override
  public Region getRegion() {
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
  public boolean isCallbackArgumentAvailable() {
    return false;
  }

  @Override
  public boolean isOriginRemote() {
    return false;
  }

  @Override
  public DistributedMember getDistributedMember() {
    return null;
  }
}
