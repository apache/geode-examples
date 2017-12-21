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
package org.apache.geode_examples.luceneSpatial;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.apache.lucene.document.Document;
import org.junit.Test;

public class TrainStopSerializerTest {

  @Test
  public void serializerReturnsSingleDocument() {
    TrainStopSerializer serializer = new TrainStopSerializer();
    Collection<Document> documents =
        serializer.toDocuments(null, new TrainStop("here", -122.8515139, 45.5099231));

    assertEquals(1, documents.size());
  }
}
