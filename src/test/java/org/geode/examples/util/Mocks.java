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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.mockito.invocation.InvocationOnMock;

public class Mocks {
  private Mocks() { }
  
  @SuppressWarnings("unchecked")
  public static <K, V> Region<K, V> region(String name) throws Exception {

    QueryService queryService = mock(QueryService.class);

    ClientCache regionService = mock(ClientCache.class);
    when(regionService.getQueryService()).thenReturn(queryService);

    Map<K, V> data = new HashMap<>();
    Region<K, V> region = mock(Region.class);

    when(region.getName()).thenReturn(name);
    when(region.put(any(), any())).then(inv -> data.put(getKey(inv), getValue(inv)));
    when(region.get(any())).then(inv -> data.get(getKey(inv)));
    when(region.keySet()).thenReturn(data.keySet());
    when(region.values()).thenReturn(data.values());
    when(region.size()).thenReturn(data.size());
    when(region.keySetOnServer()).thenReturn(data.keySet());
    when(region.containsKey(any())).then(inv -> data.containsKey(getKey(inv)));
    when(region.containsKeyOnServer(any())).then(inv -> data.containsKey(getKey(inv)));
    when(region.getRegionService()).thenReturn(regionService);
    
    doAnswer(inv -> {
      data.putAll((Map<? extends K, ? extends V>) inv.getArguments()[0]);
      return inv.getArguments();
    }).when(region).putAll(any());

    return region;
  }

  public static <K, V> Region<K, V> addQuery(Region<K, V> region, String queryString, Collection results) throws Exception {
    ClientCache regionService = (ClientCache) region.getRegionService();
    QueryService queryService = regionService.getQueryService();
    SelectResults<V> selectResults = (SelectResults<V>) mock(SelectResults.class);
    when(selectResults.iterator()).thenReturn(results.iterator());

    Query query = mock(Query.class);
    when(query.execute()).thenReturn(selectResults);

    when(selectResults.size()).thenReturn(results.size());

    when(queryService.newQuery(queryString)).thenReturn(query);

    return region;
  }
  @SuppressWarnings("unchecked")
  private static <K> K getKey(InvocationOnMock inv) {
    return (K) inv.getArguments()[0];
  }

  @SuppressWarnings("unchecked")
  private static <V> V getValue(InvocationOnMock inv) {
    return (V) inv.getArguments()[1];
  }
}
