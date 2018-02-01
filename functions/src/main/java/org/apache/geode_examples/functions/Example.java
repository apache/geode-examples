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
package org.apache.geode_examples.functions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

public class Example {
  private int maximum;

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    Execution execution = FunctionService.onRegion(region);
    new Example().getPrimes(region, execution);
    cache.close();
  }

  public Example() {
    this(100);
  }

  public Example(int maximum) {
    this.maximum = maximum;
  }

  public Set<Integer> getPrimes(Region<Integer, String> region, Execution execution) {
    Set<Integer> primes = new HashSet<>();

    for (Integer key : (Iterable<Integer>) () -> IntStream.rangeClosed(1, maximum).iterator()) {
      region.put(key, key.toString());
    }

    ResultCollector<Integer, List> results = execution.execute(PrimeNumber.ID);
    primes.addAll(results.getResult());
    System.out.println("The primes in the range from 1 to " + maximum + " are:\n" + primes);
    return primes;
  }
}
