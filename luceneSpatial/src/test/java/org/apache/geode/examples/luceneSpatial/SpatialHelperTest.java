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

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Test;

public class SpatialHelperTest {

  @Test
  public void queryFindsADocumentThatWasAdded() throws IOException {

    // Create an in memory lucene index to add a document to
    RAMDirectory directory = new RAMDirectory();
    IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig());

    // Add a document to the lucene index
    Document document = new Document();
    document.add(new TextField("name", "name", Field.Store.YES));
    Field[] fields = SpatialHelper.getIndexableFields(-122.8515139, 45.5099231);
    for (Field field : fields) {
      document.add(field);
    }
    writer.addDocument(document);
    writer.commit();


    // Make sure a findWithin query locates the document
    Query query = SpatialHelper.findWithin(-122.8515239, 45.5099331, 1);
    SearcherManager searcherManager = new SearcherManager(writer, null);
    IndexSearcher searcher = searcherManager.acquire();
    TopDocs results = searcher.search(query, 100);
    assertEquals(1, results.totalHits);
  }
}
