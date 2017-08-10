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
package org.apache.geode.examples.oql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geode.examples.util.Mocks;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.query.SelectResults;
import org.apache.geode.cache.query.internal.types.CollectionTypeImpl;
import org.apache.geode.cache.query.types.CollectionType;
import org.apache.geode.cache.query.types.ObjectType;

public class ExampleTest {
  class SimpleObjectType implements ObjectType {
    String className;

    public SimpleObjectType(Class clazz) {
      className = clazz.getName();
    }

    public boolean isCollectionType() {
      return false;
    }

    public boolean isMapType() {
      return false;
    }

    public boolean isStructType() {
      return false;
    }

    public String getSimpleClassName() {
      return className;
    }

    public Class resolveClass() {
      return Object.class;
    }

    public void toData(DataOutput out) {}

    public void fromData(DataInput in) {}

    public boolean equals(Object o) {
      return o instanceof SimpleObjectType;
    }
  }

  class ExampleResultSet<T> extends HashSet<T> implements SelectResults<T> {
    private ObjectType elementType;

    public ExampleResultSet(Collection<T> c, Class clazz) {
      addAll(c);
      elementType = new SimpleObjectType(clazz);
    }

    @Override
    public boolean isModifiable() {
      return false;
    }

    @Override
    public int occurrences(Object element) {
      return 0;
    }

    @Override
    public Set asSet() {
      return this;
    }

    @Override
    public List asList() {
      return new ArrayList(this);
    }

    @Override
    public CollectionType getCollectionType() {
      return new CollectionTypeImpl(Set.class, elementType);
    }

    @Override
    public void setElementType(ObjectType elementType) {
      this.elementType = elementType;
    }
  }

  @Rule
  public SystemOutRule systemOutRule = new SystemOutRule().enableLog();

  @Test
  public void testExample() throws Exception {
    Region<Integer, String> region = Mocks.region("example-region");
    when(region.query("SELECT DISTINCT entry.value FROM /example-region.entries entry"))
        .then(inv -> {
          return new ExampleResultSet<String>(Arrays.asList("value5", "value5", "value6", "value3", "value4", "value5", "value6", "value7", "value8", "value9"), String.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value3'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(1), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value4'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(1), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value5'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(3), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value6'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(2), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value7'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(1), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value8'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(1), Integer.class);
        });
    when(region.query("SELECT COUNT(*) FROM /example-region.entries entry WHERE entry.value = 'value9'"))
        .then(inv -> {
          return new ExampleResultSet<Integer>(Arrays.asList(1), Integer.class);
        });

    new Example().accept(region);

    assertThat(systemOutRule.getLog()).contains("value6 found [2] times");
    assertThat(systemOutRule.getLog()).contains("value5 found [3] times");
  }
}
