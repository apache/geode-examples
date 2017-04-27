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
package org.apache.geode.examples.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.geode.cache.LoaderHelper;
import org.apache.geode.cache.Region;
import org.geode.examples.util.Mocks;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class ExampleTest {

  @Rule
  public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void testExample() throws Exception {
    QuoteLoader loader = new QuoteLoader();
    Region<String, String> region = Mocks.region("example-region");

    @SuppressWarnings("unchecked")
    LoaderHelper<String, String> helper = mock(LoaderHelper.class);
    when(helper.getRegion()).thenReturn(region);

    when(region.get(any())).then(inv -> {
      String key = inv.getArgumentAt(0, String.class);
      when(helper.getKey()).thenReturn(key);

      return loader.load(helper);
    });

    new Example().accept(region);

    assertThat(systemOutRule.getLog()).contains("Anton Chekhov");
    assertThat(systemOutRule.getLog()).contains("Loaded 20 definitions");
    assertThat(systemOutRule.getLog()).contains("Fetched 20 cached definitions");
  }
}
