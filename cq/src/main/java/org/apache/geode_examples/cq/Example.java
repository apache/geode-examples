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
import org.apache.geode.cache.query.CqAttributes;
import org.apache.geode.cache.query.CqAttributesFactory;
import org.apache.geode.cache.query.CqEvent;
import org.apache.geode.cache.query.CqException;
import org.apache.geode.cache.query.CqExistsException;
import org.apache.geode.cache.query.CqQuery;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.RegionNotFoundException;
import org.apache.geode.cache.util.CqListenerAdapter;
import org.apache.geode.pdx.ReflectionBasedAutoSerializer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Example extends CqListenerAdapter {
  static final String REGION_NAME = "example-region";
  static final String QUERY_NAME = "query";
  static final String QUERY = "SELECT * FROM /" + REGION_NAME + " r where r.hp >= 0";
  private final Region<String, PC> region;
  private Process child = null;
  private Map<String, Integer> hps = new HashMap<>();

  public Example(Region<String, PC> region) {
    this.region = region;

    Map<String, PC> pcs = new HashMap<>();
    while (pcs.size() < 5) {
      final PC pc = PC.generate();
      if (!pcs.containsKey(pc.getName())) {
        pcs.put(pc.getName(), pc);
      }
    }
    region.putAll(pcs);
  }

  static String constructJVMPath() {
    StringBuilder builder = new StringBuilder();
    builder.append(System.getProperty("java.home"));
    builder.append(File.separator);
    builder.append("bin");
    builder.append(File.separator);
    builder.append("java");
    if (System.getProperty("os.name").toLowerCase().contains("win")) {
      builder.append("w.exe");
    }
    return builder.toString();
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").setPoolSubscriptionEnabled(true)
        .setPdxSerializer(new ReflectionBasedAutoSerializer("org.apache.geode_examples.cq.PC"))
        .create();

    // create a local region that matches the server region
    Region<String, PC> region =
        cache.<String, PC>createClientRegionFactory(ClientRegionShortcut.PROXY).create(REGION_NAME);

    Example example = new Example(region);

    // Create the query and execute it to begin receiving asynchronous events.
    CqAttributesFactory cqf = new CqAttributesFactory();
    cqf.addCqListener(example);
    CqAttributes cqa = cqf.create();
    CqQuery query = null;
    QueryService queryService = cache.getQueryService();
    try {
      query = queryService.newCq(QUERY_NAME, QUERY, cqa);
      query.execute();
    } catch (CqExistsException | CqException cqe) {
      cqe.printStackTrace();
    } catch (RegionNotFoundException rnfe) {
      rnfe.printStackTrace();
    }

    // Run the child process to update the region.
    example.executeChildProcess(new String[0]);
    example.waitForChildProcess();

    // Close the query.
    if (Objects.nonNull(query)) {
      try {
        query.close();
      } catch (CqException cqe) {
        cqe.printStackTrace();
      }
    }
    cache.close();
  }

  void executeChildProcess(String[] args) {
    String[] command = new String[4 + args.length];
    command[0] = constructJVMPath();
    command[1] = "-classpath";
    command[2] = System.getProperty("java.class.path") + ":build/libs/cq.jar";
    command[3] = "org.apache.geode_examples.cq.Engine";
    for (int i = 0; i < args.length; ++i) {
      command[i + 4] = args[i];
    }
    try {
      child = Runtime.getRuntime().exec(command);
      System.out.println("Executed child");
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  void waitForChildProcess() {
    System.out.println("Waiting for child...");
    try {
      for (int i = 0; i < 60; ++i) {
        if (child.waitFor(1000, TimeUnit.MILLISECONDS)) {
          break;
        }
        System.out.println();
      }
      System.out.println("Reaped child");
    } catch (InterruptedException ie) {
      ie.printStackTrace();
    }
  }

  @Override
  public void onEvent(CqEvent event) {
    PC pc = (PC) event.getNewValue();
    if (0 < pc.getHp()) {
      if (hps.containsKey(pc.getName())) {
        final int delta = hps.get(pc.getName()) - pc.getHp();
        if (delta < 0) {
          System.out.println(String.format("%12s: Ouch!", pc.getName()));
        } else if (0 < delta) {
          System.out.println(String.format("%12s: Nom nom nom...", pc.getName()));
        } else {
          System.out.println("(" + pc.getName() + " shot the food.)");
        }
      }
      hps.put(pc.getName(), pc.getHp());
    } else {
      System.out.println("(" + pc.getName() + " has shuffled off this mortal coil...)");
    }
  }

  @Override
  public void onError(CqEvent event) {
    if (Objects.nonNull(event.getThrowable())) {
      System.err.println(event.getThrowable().getMessage());
    }
  }
}
