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
package org.apache.geode_examples.functions;

import static org.jgroups.util.Util.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Execution;
import org.apache.geode.cache.execute.ResultCollector;

public class ExampleTest {
  @Test
  public void testExample() throws Exception {
    Example example = new Example(10);

    Region<Integer, String> region = mock(Region.class);
    List<Integer> primes = Arrays.asList(1, 2, 3, 5, 7);
    ResultCollector resultCollector = mock(ResultCollector.class);
    when(resultCollector.getResult()).thenReturn(primes);
    Execution execution = mock(Execution.class);
    when(execution.execute(PrimeNumber.ID)).thenReturn(resultCollector);

    assertEquals(new HashSet(primes), example.getPrimes(region, execution));
  }
}
