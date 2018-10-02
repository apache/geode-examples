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
package org.apache.geode_examples.durableMessaging;

import static org.apache.geode.distributed.ConfigurationProperties.DURABLE_CLIENT_ID;
import static org.apache.geode.distributed.ConfigurationProperties.DURABLE_CLIENT_TIMEOUT;
import static org.apache.geode.distributed.ConfigurationProperties.LOG_LEVEL;

import java.util.concurrent.CountDownLatch;

import org.apache.geode.cache.EntryEvent;
import org.apache.geode.cache.InterestResultPolicy;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.util.CacheListenerAdapter;

public class Example {
  private static final int numEvents = 10;
  private static final CountDownLatch waitForEventsLatch = new CountDownLatch(numEvents);

  public static void main(String[] args) throws Exception {
    ClientCache clientCacheOne = createDurableClient();

    final String regionName = "example-region";

    // Create a local caching proxy region that matches the server region
    ClientRegionFactory<Integer, String> clientOneRegionFactory =
        clientCacheOne.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<Integer, String> exampleClientRegionOne = clientOneRegionFactory.create(regionName);

    // Register interest to create the durable client message queue
    exampleClientRegionOne.registerInterestForAllKeys(InterestResultPolicy.DEFAULT, true);

    // Close the client cache with keepalive set to true so
    // the durable client messages are preserved
    // for the duration of the configured timeout. In practice,
    // it is more likely the client would disconnect
    // due to a temporary network issue, but for this example the cache is explicitly closed.
    clientCacheOne.close(true);

    // Create a second client to do puts with while the first client is disconnected
    ClientCache clientCacheTwo = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    ClientRegionFactory<Integer, String> clientTwoRegionFactory =
        clientCacheTwo.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<Integer, String> exampleClientRegionTwo = clientTwoRegionFactory.create(regionName);

    for (int i = 0; i < numEvents; ++i) {
      exampleClientRegionTwo.put(i, "testValue" + i);
    }

    // Close the second client and restart the durable client
    clientCacheTwo.close(false);

    clientCacheOne = createDurableClient();

    // Add an example cache listener so this client can react
    // when the server sends this client's events from the
    // durable message queue. This isn't required but helps
    // illustrate that the events are delivered successfully.
    clientOneRegionFactory = clientCacheOne.createClientRegionFactory(ClientRegionShortcut.PROXY);
    exampleClientRegionOne = clientOneRegionFactory
        .addCacheListener(new ExampleCacheListener<Integer, String>()).create(regionName);

    // Signal to the server that this client is ready to receive events.
    // Events in this client's durable message queue
    // will then be delivered and trigger our example cache listener.
    clientCacheOne.readyForEvents();

    // Use a count down latch to ensure that this client receives all queued events from the server
    waitForEventsLatch.await();
  }

  private static ClientCache createDurableClient() {
    return new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        // Provide a unique identifier for this client's durable subscription message queue
        .set(DURABLE_CLIENT_ID, "1")
        // Provide a timeout in seconds for how long the server will wait for the client to
        // reconnect.
        // If this property isn't set explicitly, it defaults to 300 seconds.
        .set(DURABLE_CLIENT_TIMEOUT, "200")
        // This is required so the client can register interest for all keys on this durable client
        .setPoolSubscriptionEnabled(true).set(LOG_LEVEL, "WARN").create();
  }

  public static class ExampleCacheListener<Integer, String>
      extends CacheListenerAdapter<Integer, String> {
    public ExampleCacheListener() {}

    @Override
    public void afterCreate(EntryEvent<Integer, String> event) {
      System.out.println(
          "Received create for key " + event.getKey() + " after durable client reconnection");
      waitForEventsLatch.countDown();
    }
  }
}
