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
 * Created by kmiller on 1/30/17.
 */
public class EmployeeKeyTest {

  private EmployeeKey k;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setup() {
    k = new EmployeeKey("First Last", 3001);
  }

  @Test
  public void testGetName() {
    assertEquals("First Last", k.getName());
  }

  @Test
  public void testGetEmplNumber() {
    assertEquals(3001, k.getEmplNumber());
  }

  @Test
  public void testEquals() {
    EmployeeKey otherKey = new EmployeeKey("First Last", 3001);
    assertTrue(k.equals(otherKey));
    EmployeeKey nonMatchingKey = new EmployeeKey("Othername", 1);
    assertFalse(k.equals(nonMatchingKey));
  }

  @Test
  public void testToString() {
    assertEquals("Name: First Last Employee Number: 3001", k.toString());
  }
}
