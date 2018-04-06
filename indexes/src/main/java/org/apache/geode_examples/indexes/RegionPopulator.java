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

import java.util.Date;
import java.util.LinkedList;
import java.util.PrimitiveIterator;
import java.util.Random;

public class RegionPopulator {
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
  final Random random = new Random();
  final LinkedList<FlightCode> flights = new LinkedList<>();

  void populateRegion(Region<String, Passenger> region) {
    populateFlights(50);
    insertPassengers(250, region);
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
}
