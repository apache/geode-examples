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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Passenger implements Serializable {
  private static final long serialVersionUID = -991115968572408216L;
  static final DateFormat ISO_8601_TIMESTAMP_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

  String name;
  int age;
  FlightCode flight;
  Date departure;
  Date arrival;

  public Passenger(String name, int age, FlightCode flight, Date departure, Date arrival) {
    this.name = name;
    this.age = age;
    this.flight = flight;
    this.departure = departure;
    this.arrival = arrival;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public FlightCode getFlight() {
    return flight;
  }

  public Date getDeparture() {
    return departure;
  }

  public Date getArrival() {
    return arrival;
  }

  public String toString() {
    return getName() + ", age " + getAge() + ", flight " + getFlight() + ", departing at "
        + ISO_8601_TIMESTAMP_FORMAT.format(getDeparture()) + ", arriving at "
        + ISO_8601_TIMESTAMP_FORMAT.format(getArrival());
  }
}
