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
package org.apache.geode_examples.geodeForRedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class Example {
  public static final String SORTED_SET_KEY = "{tag}leaderboard";

  public static void main(String[] args) {
    JedisCluster jedis = new JedisCluster(new HostAndPort("127.0.0.1", 6379));

    populateSortedSet(jedis);

    printSortedSetContents("Initial leaderboard with key '" + SORTED_SET_KEY + "': ", jedis);

    System.out.println("Updating scores...");
    modifyScores(jedis);

    printSortedSetContents("Updated leaderboard with key '" + SORTED_SET_KEY + "': ", jedis);

    System.out.println("Removing lowest scoring member...");
    jedis.zpopmin(SORTED_SET_KEY);

    printSortedSetContents("Updated leaderboard with key '" + SORTED_SET_KEY + "': ", jedis);
  }

  private static void populateSortedSet(JedisCluster jedis) {
    Map<String, Double> memberScoreMap = new HashMap<>();
    memberScoreMap.put("John", 0.0);
    memberScoreMap.put("Maria", 0.0);
    memberScoreMap.put("Jose", 0.0);
    memberScoreMap.put("Wei", 0.0);
    memberScoreMap.put("Ahmed", 0.0);

    jedis.zadd(SORTED_SET_KEY, memberScoreMap);
  }

  private static void printSortedSetContents(String baseMessage, JedisCluster jedis) {
    System.out
        .println(baseMessage + jedis.zrevrangeWithScores(SORTED_SET_KEY, 0, Integer.MAX_VALUE));
  }

  private static void modifyScores(JedisCluster jedis) {
    Random random = new Random();
    jedis.zincrby(SORTED_SET_KEY, random.nextInt(10_000) / 100.0, "John");
    jedis.zincrby(SORTED_SET_KEY, random.nextInt(10_000) / 100.0, "Maria");
    jedis.zincrby(SORTED_SET_KEY, random.nextInt(10_000) / 100.0, "Jose");
    jedis.zincrby(SORTED_SET_KEY, random.nextInt(10_000) / 100.0, "Wei");
    jedis.zincrby(SORTED_SET_KEY, random.nextInt(10_000) / 100.0, "Ahmed");
  }
}
