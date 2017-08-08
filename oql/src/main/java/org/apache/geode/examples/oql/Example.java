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
package org.apache.geode.examples.oql;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import com.sun.org.apache.bcel.internal.generic.Select;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.Struct;
import org.apache.geode.cache.query.types.StructType;

public class Example implements Consumer<Region<Integer, String>> {
  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, String> region =
        cache.<Integer, String>createClientRegionFactory(ClientRegionShortcut.CACHING_PROXY)
            .create("example-region");

    new Example().accept(region);
    cache.close();
  }

  @Override
  public void accept(Region<Integer, String> region) {
    simpleQueryExample(region);
    //keyValueQueryExample(region);
    //groupingQueryExample(region);
  }

  private void simpleQueryExample(Region<Integer, String> region)
  {
    try {

      SelectResults<String> results = region.query("SELECT * FROM /example-region");

      for (Iterator iter = results.iterator(); iter.hasNext();) {
        String value = (String) iter.next();
        System.out.println("Value Found: " + value);
      }

    } catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
  }

  private void keyValueQueryExample(Region<Integer, String> region)
  {
    try {

      SelectResults<Object> results = region.query("SELECT entry.key, entry.value FROM /example-region.entries entry");

      int counter = 0;
      for (Iterator iter = results.iterator(); iter.hasNext();) {
        Struct entry = (Struct) iter.next();
        System.out.println("Entry #" + (counter++));
        StructType metaData = entry.getStructType();
        String[] names = metaData.getFieldNames();
        for (String name : names) {
          System.out.println("\t" + name + " -> " + entry.get(name));
        }
      }

    } catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
  }

  private void groupingQueryExample(Region<Integer, String> region)
  {
    Map<Integer, String> previousValues = region.getAll(region.keySet());
    try {
      region.put(1, "value5");
      region.put(2, "value5");
      region.put(3, "value6");

      SelectResults<String> results = region.query("SELECT DISTINCT entry.value FROM /example-region.entries entry");

      for (Iterator iter = results.iterator(); iter.hasNext();) {
        String value = (String) iter.next();
        System.out.print(value + ":");
        SelectResults<Integer> keyCount = region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = '" + value + "'");
        System.out.println("\t " + value + " found " + keyCount + " times");
        System.out.println();
      }

    } catch (Exception ex)
    {
      System.out.println(ex.toString());
    }
    finally {
      region.putAll(previousValues);
    }
  }
}
