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
package org.apache.geode_examples.async;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;

public class ExampleAsyncEventListener implements AsyncEventListener {
  private final String[] words =
      {"the", "be", "to", "of", "and", "I", "a", "in", "that", "have", "it", "for", "not", "on",
          "with", "he", "as", "you", "do", "at", "this", "but", "his", "by", "from", "they", "we",
          "say", "her", "she", "or", "an", "will", "my", "one", "all", "would", "there", "their",
          "what", "so", "up", "out", "if", "about", "who", "get", "which", "go", "me"};

  private final LevenshteinDistance distance = new LevenshteinDistance();

  public String spellCheck(String candidate) {
    int index = -1;
    int shortest = Integer.MAX_VALUE;
    for (int i = 0; i < words.length; ++i) {
      final String word = words[i];
      final int score = distance.calculate(word, candidate);
      if (score < shortest) {
        index = i;
        shortest = score;
      }
    }
    if (0 <= index) {
      return words[index];
    }
    return candidate;
  }

  @Override
  public boolean processEvents(List<AsyncEvent> events) {
    final ExecutorService exService = Executors.newSingleThreadExecutor();
    for (AsyncEvent<Integer, String> event : events) {
      final String oldValue = event.getDeserializedValue();
      final String newValue = spellCheck(oldValue);
      exService.submit(() -> {
        Cache cache = (Cache) event.getRegion().getRegionService();
        Region<String, String> region = cache.getRegion(Example.OUTGOING_REGION_NAME);
        region.put(oldValue, newValue);
      });
    }
    return true;
  }

  @Override
  public void close() {
    // NOP
  }
}
