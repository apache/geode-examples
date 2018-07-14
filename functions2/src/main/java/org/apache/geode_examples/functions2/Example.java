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
package org.apache.geode_examples.functions2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.IntStream;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.client.PoolManager;
import org.apache.geode.cache.client.internal.ExecuteFunctionNoAckOp;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.FunctionService;
import org.apache.geode.cache.execute.ResultCollector;

public class Example {

  public static void main(String[] args) throws IOException {
    int maximum = 100;

    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().set("log-level", "WARN").create();

    PoolManager.createFactory().addLocator("localhost", 10334).create("myPool");

    // create a local region that matches the server region
    Region<Integer, String> regionOne =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("regionOne");

    // add data
    for (Integer key : (Iterable<Integer>) () -> IntStream.rangeClosed(1, maximum).iterator()) {
      regionOne.put(key, key.toString());
    }

    System.out.println("Press enter to continue...");
    System.in.read();

    String[] functionArgs = {"regionOne", "regionTwo"};
    Execution execution =
        FunctionService.onServer(PoolManager.find("myPool")).setArguments(functionArgs);

    execution.execute(CopyRegionData.ID);

    System.out.println("Done");

    cache.close();
  }
}
