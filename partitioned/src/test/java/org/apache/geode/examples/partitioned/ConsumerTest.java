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
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.geode.cache.client.ClientRegionFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;

public class ConsumerTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Consumer consumer;
  private ClientCache clientCache = mock(ClientCache.class);
  private Region region1 = mock(Region.class);
  private Region region2 = mock(Region.class);
  private ClientRegionFactory clientRegionFactory = mock(ClientRegionFactory.class);
  private Set keys = mock(Set.class);
  private static final String[] EMPTYARGS = new String[0];
  private static final String[] GOODARGS1 = {"EmployeeRegion"};
  private static final String[] GOODARGS2 = {"BadEmployeeRegion"};
  private static final String[] BADARGS1 = {""};
  private static final String[] BADARGS2 = {"BadRegionName"};
  private static final String[] BADARGS3 = {"BadEmployeeRegion", "2"};

  @Before
  public void setup() {
    when(region1.getName()).thenReturn(Consumer.REGION1_NAME);
    when(region2.getName()).thenReturn(Consumer.REGION2_NAME);
    when(keys.size()).thenReturn(Consumer.NUM_ENTRIES);
    when(clientCache.createClientRegionFactory(ClientRegionShortcut.PROXY))
        .thenReturn(clientRegionFactory);
    when(clientRegionFactory.create(Consumer.REGION1_NAME)).thenReturn(region1);
    when(clientRegionFactory.create(Consumer.REGION2_NAME)).thenReturn(region2);

    /* Make a small map that will provide values as a region would */
    Map<EmployeeKey, EmployeeData> emplMap = new HashMap<>();
    EmployeeKey k1 = new EmployeeKey("Bertie Bell", 170);
    EmployeeData d1 = new EmployeeData(k1, 72000, 40);
    emplMap.put(k1, d1);
    EmployeeKey k2 = new EmployeeKey("Toni Tiptoe", 180);
    EmployeeData d2 = new EmployeeData(k2, 70000, 40);
    emplMap.put(k2, d2);
    /* Use HashMap as fake region for keySetOnServer, size, and get methods */
    when(region1.keySetOnServer()).thenReturn(emplMap.keySet());
    when(region1.size()).thenReturn(emplMap.size());
    when(region1.get(eq(k1))).thenReturn(emplMap.get(k1));
    when(region1.get(eq(k2))).thenReturn(emplMap.get(k2));

    Map<BadEmployeeKey, EmployeeData> badEmplMap = new HashMap<>();
    BadEmployeeKey bk1 = new BadEmployeeKey("Bertie Bell", 170);
    EmployeeData bd1 = new EmployeeData(bk1, 72000, 40);
    badEmplMap.put(bk1, bd1);
    BadEmployeeKey bk2 = new BadEmployeeKey("Toni Tiptoe", 180);
    EmployeeData bd2 = new EmployeeData(bk2, 70000, 40);
    badEmplMap.put(bk2, bd2);
    /* Use HashMap as fake region for keySetOnServer, size, and get methods */
    when(region2.keySetOnServer()).thenReturn(badEmplMap.keySet());
    when(region2.size()).thenReturn(badEmplMap.size());
    when(region2.get(eq(bk1))).thenReturn(badEmplMap.get(bk1));
    when(region2.get(eq(bk2))).thenReturn(badEmplMap.get(bk2));

    consumer = new Consumer(clientCache);
  }

  @Test
  public void numberOfEntriesShouldBeGreaterThanZero() throws Exception {
    assertTrue(consumer.NUM_ENTRIES > 0);
  }

  @Test
  public void testConsumerGetRegion1() {
    assertEquals("Region names do not match", Consumer.REGION1_NAME,
        consumer.getRegion1().getName());
  }

  @Test
  public void testConsumerGetRegion2() {
    assertEquals("Region names do not match", Consumer.REGION2_NAME,
        consumer.getRegion2().getName());
  }

  @Test
  public void testPrintRegionContents() {
    consumer.printRegionContents();
  }

  @Test
  public void testPrintBadRegionContents() {
    consumer.printBadRegionContents();
  }

  @Test
  public void testCheckAndPrint1() {
    /* no exception expected */
    consumer.checkAndPrint(GOODARGS1);
  }

  @Test
  public void testCheckAndPrint2() {
    /* no exception expected */
    consumer.checkAndPrint(GOODARGS2);
  }

  @Test
  public void testCheckAndPrint3() {
    /* no arguments specified, array length should be 0 */
    expectedException.expect(Exception.class);
    expectedException.expectMessage("Expected argument specifying region name.");
    spy(Consumer.class).checkAndPrint(EMPTYARGS);
  }

  @Test
  public void testCheckAndPrint4() {
    /* First argument is the empty string */
    expectedException.expect(Exception.class);
    expectedException.expectMessage("Unrecognized region name in argument specification.");
    spy(Consumer.class).checkAndPrint(BADARGS1);
  }

  @Test
  public void testCheckAndPrint5() {
    /* Arguments are an invalid region name */
    expectedException.expect(Exception.class);
    expectedException.expectMessage("Unrecognized region name in argument specification.");
    spy(Consumer.class).checkAndPrint(BADARGS2);
  }

  @Test
  public void testCheckAndPrint6() {
    /* Arguments are an invalid region name and an extra but unused argument */
    expectedException.expect(Exception.class);
    expectedException.expectMessage("Expected only 1 argument, and received more than 1.");
    spy(Consumer.class).checkAndPrint(BADARGS3);
  }

}
