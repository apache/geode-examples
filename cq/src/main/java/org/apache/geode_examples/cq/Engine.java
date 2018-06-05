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
package org.apache.geode_examples.cq;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Engine {
  private final Random random = new Random();
  private final Region<String, PC> region;

  public Engine(Region<String, PC> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache =
        new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334).set("log-level", "WARN")
            .setPdxSerializer(new ReflectionBasedAutoSerializer("org.apache.geode_examples.cq.PC"))
            .create();

    // create a local region that matches the server region
    Region<String, PC> region =
        cache.<String, PC>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create(Example.REGION_NAME);

    Engine engine = new Engine(region);
    engine.run();

    cache.close();
  }

  void run() {
    while (0 < getNumberOfActivePlayers()) {
      final Set<String> keys = new HashSet(region.keySetOnServer());
      for (String key : keys) {
        PC pc = region.get(key);
        if (0 < pc.getHp()) {
          pc.setHp(pc.getHp() + (random.nextInt(5) - 3));
          System.out.println(pc);
          region.put(key, pc);
        }
      }

      try {
        Thread.sleep(200);
      } catch (InterruptedException ie) {
        // Do nothing.
      }
    }
  }

  int getNumberOfActivePlayers() {
    int count = 0;
    final Set<String> keys = new HashSet(region.keySetOnServer());
    for (String key : keys) {
      if (0 < region.get(key).getHp()) {
        ++count;
      }
    }
    return count;
  }
}
