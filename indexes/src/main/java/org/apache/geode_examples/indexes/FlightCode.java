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

import java.io.Serializable;

public class FlightCode implements Serializable {
  private static final long serialVersionUID = -4252046061229265115L;

  String airlineCode;
  int flightNumber;
  String departure;
  String arrival;

  public FlightCode(String airlineCode, int flightNumber, String departure, String arrival) {
    this.airlineCode = airlineCode;
    this.flightNumber = flightNumber;
    this.departure = departure;
    this.arrival = arrival;
  }

  public String getAirlineCode() {
    return airlineCode;
  }

  public int getFlightNumber() {
    return flightNumber;
  }

  public String getDeparture() {
    return departure;
  }

  public String getArrival() {
    return arrival;
  }

  public String toString() {
    return getAirlineCode() + String.format("%03d", getFlightNumber()) + " from " + getDeparture()
        + " to " + getArrival();
  }
}
