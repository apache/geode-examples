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
package org.apache.geode.examples.serialization;

import org.apache.geode.DataSerializable;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.pdx.PdxSerializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Example {
  public static final String ARENDELLE = "Arendelle";
  public static final String BORDURIA = "Borduria";
  public static final String CASCADIA = "Cascadia";
  public static final String ELBONIA = "Elbonia";
  public static final String FLORIN = "Florin";
  public static final String GRAUSTARK = "Graustark";
  public static final String LATVERIA = "Latveria";
  public static final String MARKOVIA = "Markovia";
  public static final String PARADOR = "Parador";
  public static final String SIERRA_GORDO = "Sierra Gordo";
  final Region<String, Country> region;

  public Example(Region<String, Country> region) {
    this.region = region;
  }

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<String, Country> region =
        cache.<String, Country>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create("example-region");

    Example example = new Example(region);

    example.demonstrateWithoutCycle(JavaSerializableCountry.class);
    example.demonstrateWithoutCycle(DataSerializableCountry.class);
    example.demonstrateWithoutCycle(PdxSerializableCountry.class);

    example.demonstrateWithCycle(JavaSerializableCountry.class);
    example.demonstrateWithCycle(DataSerializableCountry.class);
    example.demonstrateWithCycle(PdxSerializableCountry.class);

    cache.close();
  }

  public void demonstrateWithoutCycle(Class countryClass) {
    System.out.println();
    System.out.println(countryClass);
    region.clear();
    insertValues(countryClass);
    printValues(getKeys());
  }

  public void demonstrateWithCycle(Class countryClass) {
    System.out.println();
    if (DataSerializableCountry.class.equals(countryClass)
        || PdxSerializableCountry.class.equals(countryClass)) {
      System.out.println("Class " + countryClass + " does not support object cycles.");
      return;
    }
    System.out.println(countryClass);
    region.clear();
    insertValues(countryClass);
    updateValues();
    printValues(getKeys());
  }

  Country create(Class countryClass, String name) throws NoSuchMethodException,
      InstantiationException, IllegalAccessException, InvocationTargetException {
    return create(countryClass, name, name + " City");
  }

  Country create(Class countryClass, String name, String capitol) throws NoSuchMethodException,
      InstantiationException, IllegalAccessException, InvocationTargetException {
    return create(countryClass, name, capitol, "");
  }

  Country create(Class countryClass, String name, String capitol, String language)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException,
      InvocationTargetException {
    return create(countryClass, name, capitol, language, "", 0);
  }

  Country create(Class countryClass, String name, String capitol, String language, String currency,
      int population) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
      InvocationTargetException {
    Constructor constructor = countryClass.getConstructor(
        new Class[] {String.class, String.class, String.class, String.class, int.class});
    Country country =
        (Country) constructor.newInstance(name, capitol, language, currency, population);
    return country;
  }

  Set<String> getKeys() {
    return new HashSet<>(region.keySetOnServer());
  }

  void insertValues(Class countryClass) {
    try {
      insertValue(create(countryClass, ARENDELLE, "Arendelle City", "Arendellii", "Arendelle Krona",
          76573));
      insertValue(
          create(countryClass, BORDURIA, "Szohôd", "Bordurian", "Bordurian Dinar", 1000000));
      insertValue(create(countryClass, CASCADIA, "Portland", "Pacific Northwest English",
          "United States Dollar", 16029520));
      insertValue(create(countryClass, ELBONIA));
      insertValue(create(countryClass, FLORIN));
      insertValue(create(countryClass, GRAUSTARK, "Edelweiss"));
      insertValue(
          create(countryClass, LATVERIA, "Doomstadt", "Latverian", "Latverian Franc", 500000));
      insertValue(create(countryClass, MARKOVIA, "Markovburg", "German"));
      insertValue(create(countryClass, PARADOR));
      insertValue(create(countryClass, SIERRA_GORDO, "Rio Lindo", "Spanish"));
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  void insertValue(Country country) {
    region.put(country.getName(), country);
  }

  void updateValues() {
    updateValue(ARENDELLE, new String[] {BORDURIA, ELBONIA, FLORIN});
    updateValue(BORDURIA, new String[] {ARENDELLE, FLORIN, GRAUSTARK, CASCADIA});
    updateValue(CASCADIA, new String[] {BORDURIA, GRAUSTARK, LATVERIA});
    updateValue(ELBONIA, new String[] {ARENDELLE, FLORIN, MARKOVIA});
    updateValue(FLORIN, new String[] {ARENDELLE, BORDURIA, ELBONIA, GRAUSTARK, MARKOVIA, PARADOR});
    updateValue(GRAUSTARK,
        new String[] {BORDURIA, CASCADIA, FLORIN, LATVERIA, PARADOR, SIERRA_GORDO});
    updateValue(LATVERIA, new String[] {CASCADIA, GRAUSTARK, SIERRA_GORDO});
    updateValue(MARKOVIA, new String[] {ELBONIA, FLORIN, PARADOR});
    updateValue(PARADOR, new String[] {FLORIN, GRAUSTARK, MARKOVIA, SIERRA_GORDO});
    updateValue(SIERRA_GORDO, new String[] {GRAUSTARK, LATVERIA, PARADOR});
  }

  void updateValue(String name, String[] neighbors) {
    Country country = region.get(name);
    for (String neighbor : neighbors) {
      final Country neighborCountry = region.get(neighbor);
      country.addNeighbor(neighborCountry);
    }
    region.put(name, country);
  }

  void printValues(Set<String> keys) {
    for (String key : keys) {
      Country country = region.get(key);
      System.out.println(key + ": " + country);
    }
  }
}
