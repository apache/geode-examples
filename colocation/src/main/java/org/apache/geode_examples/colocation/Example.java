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
package org.apache.geode_examples.colocation;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.util.HashMap;
import java.util.Map;

public class Example {
  private int maximum;

  public static void main(String[] args) {
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    Region<Integer, Customer> customerRegion =
        cache.<Integer, Customer>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("customer");

    Region<OrderKey, Order> orderRegion =
        cache.<OrderKey, Order>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("order");

    Map<Integer, Customer> customers = generateCustomers();

    for (int i : customers.keySet()) {
      Customer customer = customers.get(i);
      Order order = new Order(i * 10, customer.getId());
      customerRegion.put(customer.getId(), customer);
      orderRegion.put(order.getKey(), order);
    }
    cache.close();
  }

  public static Map<Integer, Customer> generateCustomers() {
    String firstNames[] =
        {"Albert", "Bob", "Charles", "Daniel", "Ethan", "Frank", "Gregory", "Henrik"};
    String lastNames[] =
        {"Anthony", "Barkley", "Chen", "Dalembert", "English", "French", "Gobert", "Hakanson"};
    String emails[] = new String[firstNames.length];
    for (int i = 0; i < firstNames.length; i++) {
      emails[i] = firstNames[i].toLowerCase() + "." + lastNames[i].toLowerCase() + "@example.com";
    }
    Map<Integer, Customer> customers = new HashMap<Integer, Customer>();

    for (int i = 0; i < firstNames.length; i++) {
      customers.put(i + 1, new Customer(i + 1, firstNames[i], lastNames[i], emails[i]));
    }
    return customers;
  }
}
