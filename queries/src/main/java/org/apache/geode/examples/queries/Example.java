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
package org.apache.geode.examples.queries;

import java.util.HashMap;
import java.util.Map;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.apache.geode.cache.query.FunctionDomainException;
import org.apache.geode.cache.query.NameResolutionException;
import org.apache.geode.cache.query.Query;
import org.apache.geode.cache.query.QueryInvocationTargetException;
import org.apache.geode.cache.query.QueryService;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.TypeMismatchException;


public class Example {
  static String REGIONNAME = "example-region";
  static String QUERY1 = "SELECT DISTINCT * FROM /%s";
  static String QUERY2 = "SELECT DISTINCT * FROM /%s h WHERE h.hoursPerWeek < 40";
  static String QUERY3 = "SELECT DISTINCT * FROM /%s x WHERE x.lastName='Jive'";

  public static void main(String[] args) {
    // connect to the locator using default port 10334
    ClientCache cache = new ClientCacheFactory().addPoolLocator("127.0.0.1", 10334)
        .set("log-level", "WARN").create();

    // create a local region that matches the server region
    Region<Integer, EmployeeData> region =
        cache.<Integer, EmployeeData>createClientRegionFactory(ClientRegionShortcut.PROXY)
            .create(REGIONNAME);

    Example e = new Example();
    Map<Integer, EmployeeData> employees = e.populateEmployeeData();
    for (EmployeeData value : employees.values()) {
      region.put(value.getEmplNumber(), value);
    }
    // count the values in the region
    int inserted = region.keySetOnServer().size();
    System.out.println(String.format("Counted %d keys in region %s", inserted, region.getName()));

    // fetch and print all values in the region (without using a query)
    region.keySetOnServer().forEach(key -> System.out.println(region.get(key)));
    // do a set of queries, printing the results of each query
    e.doQueries((ClientCache) region.getRegionService(), region);
    cache.close();
  }


  public Map<Integer, EmployeeData> populateEmployeeData() {
    // put entries in the hashmap
    String[] firstNames =
        "Alex,Bertie,Kris,Dale,Frankie,Jamie,Morgan,Pat,Ricky,Taylor,Casey,Jessie,Ryan,Skyler"
            .split(",");
    String[] lastNames =
        "Able,Bell,Call,Driver,Forth,Jive,Minnow,Puts,Reliable,Tack,Catch,Jam,Redo,Skip".split(",");
    int salaries[] = new int[] {60000, 80000, 75000, 90000, 100000};
    int hours[] = new int[] {40, 40, 40, 40, 30, 20};
    int emplNumber = 10000;

    Map<Integer, EmployeeData> employees = new HashMap<Integer, EmployeeData>();
    for (int index = 0; index < firstNames.length; index++) {
      emplNumber = emplNumber + index;
      Integer key = emplNumber;
      String email = firstNames[index] + "." + lastNames[index] + "@example.com";
      int salary = salaries[index % 5];
      int hoursPerWeek = hours[index % 6];
      EmployeeData value = new EmployeeData(firstNames[index], lastNames[index], emplNumber, email,
          salary, hoursPerWeek);
      employees.put(emplNumber, value);
    }

    return employees;
  }


  // Demonstrate querying using the API by doing a set of 3 queries.
  public void doQueries(ClientCache cache, Region<Integer, EmployeeData> region) {
    String regionName = region.getName();
    QueryService queryService = cache.getQueryService();

    try {

      // Query for every entry in the region, and print query results.
      SelectResults<EmployeeData> results = doQuery(queryService, QUERY1, regionName);
      printSetOfEmployees(results);

      // Query for all part time employees, and print query results.
      results = doQuery(queryService, QUERY2, regionName);
      printSetOfEmployees(results);

      // Query for last name of Jive, and print the full name and employee number.
      results = doQuery(queryService, QUERY3, regionName);
      for (EmployeeData eachEmployee : results) {
        System.out.println(String.format("Employee %s %s has employee number %d",
            eachEmployee.getFirstName(), eachEmployee.getLastName(), eachEmployee.getEmplNumber()));
      }

    } catch (FunctionDomainException | NameResolutionException | QueryInvocationTargetException
        | TypeMismatchException e) {
      e.printStackTrace();
    }

  }

  private SelectResults<EmployeeData> doQuery(QueryService queryService, String queryString,
      String regionName) throws FunctionDomainException, NameResolutionException,
      QueryInvocationTargetException, TypeMismatchException {
    String fullQueryString = String.format(queryString, regionName);
    System.out.println(String.format("\nQuery: %s", fullQueryString));
    Query query = queryService.newQuery(fullQueryString);
    SelectResults<EmployeeData> results = null;
    results = (SelectResults<EmployeeData>) query.execute();
    return results;
  }

  private void printSetOfEmployees(SelectResults<EmployeeData> results) {
    System.out.println("Query returned " + results.size() + " results.");
    for (EmployeeData eachEmployee : results) {
      System.out.println(String.format("Employee: %s", eachEmployee.toString()));
    }
  }
}
