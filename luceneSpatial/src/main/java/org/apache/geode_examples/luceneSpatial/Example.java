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
package org.apache.geode_examples.luceneSpatial;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneQueryException;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;

public class Example {
  public static void main(String[] args) throws InterruptedException, LuceneQueryException {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<String, TrainStop> region =
        cache.<String, TrainStop>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");


    LuceneService luceneService = LuceneServiceProvider.get(cache);
    // Add some entries into the region
    putEntries(luceneService, region);
    findNearbyTrainStops(luceneService);
    cache.close();
  }

  public static void findNearbyTrainStops(LuceneService luceneService)
      throws InterruptedException, LuceneQueryException {
    LuceneQuery<Integer, TrainStop> query =
        luceneService.createLuceneQueryFactory().create("simpleIndex", "example-region",
            index -> SpatialHelper.findWithin(-122.8515139, 45.5099231, 0.25));

    Collection<TrainStop> results = query.findValues();
    System.out.println("Found stops: " + results);
  }

  public static void putEntries(LuceneService luceneService, Map<String, TrainStop> region)
      throws InterruptedException {
    region.put("Elmonica/SW 170th Ave",
        new TrainStop("Elmonica/SW 170th Ave", -122.85146341202486, 45.509962691078009));
    region.put("Willow Creek/SW 185th Ave TC",
        new TrainStop("Willow Creek/SW 185th Ave TC", -122.87021024485213, 45.517251954169652));
    region.put("Merlo Rd/SW 158th Ave",
        new TrainStop("Merlo Rd/SW 158th Ave", -122.84216239020598, 45.505240564251949));

    // Lucene indexing happens asynchronously, so wait for
    // the entries to be in the lucene index.
    luceneService.waitUntilFlushed("simpleIndex", "example-region", 1, TimeUnit.MINUTES);
  }
}
