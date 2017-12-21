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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;

public class PrimeNumber implements Function {
  public static final String ID = PrimeNumber.class.getSimpleName();

  private boolean isPrime(int number) {
    int limit = (int) Math.floor(Math.sqrt(number));
    for (int divisor = 2; divisor <= limit; ++divisor) {
      if (number % divisor == 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public void execute(FunctionContext context) {
    RegionFunctionContext regionContext = (RegionFunctionContext) context;
    Region<Integer, String> region = regionContext.getDataSet();

    List<Integer> primes = new ArrayList<>();
    Set<Integer> keys = region.keySet();
    for (Integer key : keys) {
      if (isPrime(key)) {
        primes.add(key);
      }
    }
    Collections.sort(primes);

    context.getResultSender().lastResult(primes);
  }
}
