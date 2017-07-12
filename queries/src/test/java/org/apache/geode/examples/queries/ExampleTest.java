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

import static org.junit.Assert.assertEquals;

import org.apache.geode.cache.Region;
import org.geode.examples.util.Mocks;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.util.HashMap;
import java.util.Map;

public class ExampleTest {
  static String REGIONNAME = "example-region";

  @Rule
  public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void testExample() throws Exception {
    Example example = new Example();

    Region<Integer, EmployeeData> region = Mocks.region(REGIONNAME);

    // SET UP FIRST QUERY
    Mocks.addQuery(region, String.format(Example.QUERY1, REGIONNAME),
        example.populateEmployeeData().values());


    // SET UP SECOND QUERY
    // Second query returns 4 results. Create new hashmap with exactly those 4 results,
    // and use it in the mock of the second query.
    Map<Integer, EmployeeData> query2Values = new HashMap<Integer, EmployeeData>();
    String[] firstNames = "Casey,Jaime,Jessie,Frankie".split(",");
    String[] lastNames = "Catch,Jive,Jam,Forth".split(",");
    int salaries[] = new int[] {60000, 60000, 80000, 100000};
    int hours[] = new int[] {30, 20, 20, 30};
    int emplNumber[] = new int[] {10055, 10015, 10066, 10010};

    for (int index = 0; index < firstNames.length; index++) {
      String email = firstNames[index] + "." + lastNames[index] + "@example.com";
      EmployeeData value = new EmployeeData(firstNames[index], lastNames[index], emplNumber[index],
          email, salaries[index], hours[index]);
      query2Values.put(emplNumber[index], value);
    }
    Mocks.addQuery(region, String.format(Example.QUERY2, REGIONNAME), query2Values.values());

    // SET UP THIRD QUERY
    // Third query returns a single employee. Create a new hashmap with that single
    // employee, and used it in comparison to the third query result.
    Map<Integer, EmployeeData> query3Values = new HashMap<Integer, EmployeeData>();
    String firstName = "Jaime";
    String lastName = "Jive";
    int salary = 60000;
    int hour = 20;
    int employeeNumber = 10015;
    String email = firstName + "." + lastName + "@example.com";
    EmployeeData value3 =
        new EmployeeData(firstName, lastName, employeeNumber, email, salary, hour);
    query3Values.put(employeeNumber, value3);
    Mocks.addQuery(region, String.format(Example.QUERY3, REGIONNAME), query3Values.values());

    // run all the mocked queries, and check quantities of query results
    example.accept(region);
    assertEquals(example.populateEmployeeData().size(), example.getNumberOfQ1Employees());

    assertEquals(query2Values.size(), example.getNumberOfQ2Employees());

    assertEquals(query3Values.size(), example.getNumberOfQ3Employees());

  }
}
