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
package org.apache.geode.examples.partitioned;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by kmiller on 1/31/17.
 */
public class EmployeeDataTest {
  private EmployeeData d;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setup() {
    EmployeeKey k = new EmployeeKey("First Last", 3001);
    d = new EmployeeData(k, 40000, 38);
  }

  @Test
  public void testGetNameAndNumber() {
    assertEquals("First Last", d.getNameAndNumber().getName());
    assertEquals(3001, d.getNameAndNumber().getEmplNumber());
  }

  @Test
  public void testGetSalary() {
    assertEquals(40000, d.getSalary());
  }

  @Test
  public void testGetHoursPerWeek() {
    assertEquals(38, d.getHoursPerWeek());
  }

  @Test
  public void testEquals() {
    EmployeeKey otherKey = new EmployeeKey("First Last", 3001);
    EmployeeData otherData = new EmployeeData(otherKey, 40000, 38);
    assertTrue(d.equals(otherData));
    EmployeeKey nonMatchingKey = new EmployeeKey("Othername", 1);
    EmployeeData nonMatchingData = new EmployeeData(nonMatchingKey, 39999, 40);
    assertFalse(d.equals(nonMatchingData));
  }

  @Test
  public void testToString() {
    assertEquals(d.getNameAndNumber().toString() + " salary=40000 hoursPerWeek=38", d.toString());
  }

}
