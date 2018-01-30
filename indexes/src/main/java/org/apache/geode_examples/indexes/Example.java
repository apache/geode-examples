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
package org.apache.geode_examples.indexes;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;

import java.util.Date;
import java.util.LinkedList;
import java.util.PrimitiveIterator;
import java.util.Random;

public class Example {
  static final String[] airlines = "FFT,NKS,ASQ,AAL,UAL,SKW,SWA,HAL,JBU,VRD,DAL,ASA,EIA".split(",");
  static final String[] cities =
      "Arendelle City,Szohôd,Portland,Elbonia City,Florin City,Edelweiss,Doomstadt,Markovburg,Parador City,Rio Lindo"
          .split(",");
  static final String[] firstNames =
      "Ava,Brooklyn,Charlotte,Delilah,Emma,Faith,Grace,Harper,Isabella,Julia,Kaylee,Lillian,Mia,Natalie,Olivia,Peyton,Quinn,Riley,Sophia,Taylor,Unique,Victoria,Willow,Ximena,Yaretzi,Zoey"
          .split(",");
  static final String[] lastNames =
      "Smith,Johnson,Williams,Brown,Jones,Miller,Davis,Garcia,Rodriguez,Wilson,Martinez,Anderson,Taylor,Thomas,Hernandez,Moore,Martin,Jackson,Thompson,White,Lopez,Lee,Gonzalez,Harris,Clark,Lewis"
          .split(",");
  static String REGIONNAME = "example-region";
  static String NON_INDEXED_QUERY = "SELECT DISTINCT * FROM /" + REGIONNAME;
  static String COMPACT_RANGE_INDEXED_QUERY =
      // "SELECT DISTINCT * FROM /" + REGIONNAME + " p WHERE p.lastName=$1";
      "SELECT DISTINCT * FROM /" + REGIONNAME + " p WHERE p.name LIKE $1";
  static String NON_COMPACT_RANGE_INDEXED_QUERY =
      "SELECT DISTINCT * FROM /" + REGIONNAME + " p WHERE p.flight.airlineCode=$1";
  Random random = new Random();
  LinkedList<FlightCode> flights = new LinkedList<>();

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    Example example = new Example();

    // create a local region that matches the server region
    ClientRegionFactory<String, Passenger> clientRegionFactory =
        cache.createClientRegionFactory(ClientRegionShortcut.PROXY);
    Region<String, Passenger> region = clientRegionFactory.create("example-region");
    QueryService queryService = cache.getQueryService();

    example.populateFlights(50);
    example.insertPassengers(250, region);
    System.out.println("Total number of passengers: "
        + example.countResults(queryService, NON_INDEXED_QUERY, new Object[] {}));
    for (String lastName : lastNames) {
      System.out.println("Flights for " + lastName + ": " + example.countResults(queryService,
          COMPACT_RANGE_INDEXED_QUERY, new Object[] {"%" + lastName}));
    }
    for (String airline : airlines) {
      System.out.println("Flights for " + airline + ": " + example.countResults(queryService,
          NON_COMPACT_RANGE_INDEXED_QUERY, new Object[] {airline}));
    }

    cache.close();
  }

  void populateFlights(int numberOfFlights) {
    PrimitiveIterator.OfInt flightNumbers = random.ints(1, 1000).iterator();
    PrimitiveIterator.OfInt airlineIndexes = random.ints(0, airlines.length).iterator();
    PrimitiveIterator.OfInt cityIndexes = random.ints(0, cities.length).iterator();
    while (flights.size() < numberOfFlights) {
      String departure = cities[cityIndexes.next()];
      String arrival = cities[cityIndexes.next()];
      while (departure.equals(arrival)) {
        arrival = cities[cityIndexes.next()];
      }
      FlightCode flight =
          new FlightCode(airlines[airlineIndexes.next()], flightNumbers.next(), departure, arrival);
      flights.add(flight);
    }
  }

  void insertPassengers(int numberOfPassengers, Region<String, Passenger> region) {
    PrimitiveIterator.OfInt firstNameIndexes = random.ints(0, firstNames.length).iterator();
    PrimitiveIterator.OfInt lastNameIndexes = random.ints(0, lastNames.length).iterator();
    PrimitiveIterator.OfInt ages = random.ints(20, 100).iterator();
    PrimitiveIterator.OfInt flightIndexes = random.ints(0, flights.size()).iterator();
    PrimitiveIterator.OfInt milliSeconds = random.ints(0, 7 * 24 * 60 * 60 * 1000).iterator();
    while (region.sizeOnServer() < numberOfPassengers) {
      String name = firstNames[firstNameIndexes.next()] + " " + lastNames[lastNameIndexes.next()];
      if (!region.containsKey(name)) {
        final long departure = System.currentTimeMillis() + milliSeconds.next();
        final long arrival = departure + milliSeconds.next();
        Passenger passenger = new Passenger(name, ages.next(), flights.get(flightIndexes.next()),
            new Date(departure), new Date(arrival));
        region.put(passenger.getName(), passenger);
      }
    }
  }

  int countResults(QueryService queryService, String queryString, Object[] params) {
    try {
      int count = 0;
      SelectResults<Passenger> results =
          (SelectResults<Passenger>) queryService.newQuery(queryString).execute(params);
      for (Passenger passenger : results) {
        ++count;
      }
      return count;
    } catch (FunctionDomainException | TypeMismatchException | NameResolutionException
        | QueryInvocationTargetException e) {
      e.printStackTrace();
      return -1;
    }
  }
}
