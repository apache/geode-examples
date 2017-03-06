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

import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.geode.cache.Region;


public class ConsumerTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Region region = mock(Region.class);


  @Before
  public void setup() {

    /* Make a small map that will provide values as a region would */
    Map<EmployeeKey, EmployeeData> emplMap = new HashMap<>();
    EmployeeKey k1 = new EmployeeKey("Bertie Bell", 170);
    EmployeeData d1 = new EmployeeData(k1, 72000, 40);
    emplMap.put(k1, d1);
    EmployeeKey k2 = new EmployeeKey("Toni Tiptoe", 180);
    EmployeeData d2 = new EmployeeData(k2, 70000, 40);
    emplMap.put(k2, d2);
    /* Use HashMap as fake region for keySetOnServer, size, and get methods */
    when(region.keySetOnServer()).thenReturn(emplMap.keySet());
    when(region.size()).thenReturn(emplMap.size());
    when(region.get(eq(k1))).thenReturn(emplMap.get(k1));
    when(region.get(eq(k2))).thenReturn(emplMap.get(k2));

  }


  @Test
  public void testPrintRegionContents() {
    Consumer c = new Consumer(region);
    c.printRegionContents();
  }


}
