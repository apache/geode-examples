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
package org.apache.geode.examples.lucene;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.lucene.LuceneQuery;
import org.apache.geode.cache.lucene.LuceneQueryException;
import org.apache.geode.cache.lucene.LuceneService;
import org.apache.geode.cache.lucene.LuceneServiceProvider;

public class Example {
  public static void main(String[] args) throws LuceneQueryException {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, EmployeeData> region =
        cache.<Integer, EmployeeData>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    insertValues(region);
    query(cache);
    cache.close();
  }

  private static void query(ClientCache cache) throws LuceneQueryException {
    LuceneService lucene = LuceneServiceProvider.get(cache);
    LuceneQuery<Integer, EmployeeData> query = lucene.createLuceneQueryFactory()
        .create("simpleIndex", "example-region", "firstName:Chris~2", "firstname");
    System.out.println("Employees with first names like Chris: " + query.findValues());
  }


  public static void insertValues(Map<Integer, EmployeeData> region) {
    // insert values into the region
    String[] firstNames = "Alex,Bertie,Kris,Dale,Frankie,Jamie,Morgan,Pat,Ricky,Taylor".split(",");
    String[] lastNames = "Able,Bell,Call,Driver,Forth,Jive,Minnow,Puts,Reliable,Tack".split(",");
    int salaries[] = new int[] {60000, 80000, 75000, 90000, 100000};
    int hours[] = new int[] {40, 40, 40, 30, 20};
    int emplNumber = 10000;
    for (int index = 0; index < firstNames.length; index++) {
      emplNumber = emplNumber + index;
      Integer key = emplNumber;
      String email = firstNames[index] + "." + lastNames[index] + "@example.com";
      // Generating random number between 0 and 100000 for salary
      int salary = salaries[index % 5];
      int hoursPerWeek = hours[index % 5];
      EmployeeData val = new EmployeeData(firstNames[index], lastNames[index], emplNumber, email,
          salary, hoursPerWeek);
      region.put(key, val);
    }
  }
}
