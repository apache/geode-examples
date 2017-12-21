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

import java.util.Collection;
import java.util.Collections;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.spatial.vector.PointVectorStrategy;

import org.apache.geode.cache.lucene.LuceneIndex;
import org.apache.geode.cache.lucene.LuceneSerializer;

/**
 * LuceneSerializer that converts train stops into lucene documents with the gps coordinates indexed
 * using lucene's {@link PointVectorStrategy}
 */
public class TrainStopSerializer implements LuceneSerializer<TrainStop> {
  @Override
  public Collection<Document> toDocuments(LuceneIndex index, TrainStop value) {

    Document doc = new Document();
    // Index the name of the train stop
    doc.add(new TextField("name", value.getName(), Field.Store.NO));

    Field[] fields = SpatialHelper.getIndexableFields(value.getLongitude(), value.getLatitude());

    for (Field field : fields) {
      doc.add(field);
    }

    return Collections.singleton(doc);
  }

}
