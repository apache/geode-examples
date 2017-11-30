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
package org.apache.geode.examples.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class JavaSerializableCountry extends Country implements Serializable {
  private static final long serialVersionUID = -6228801855337908064L;

  public JavaSerializableCountry() {
    super();
  }

  public JavaSerializableCountry(String name, String capitol, String language, String currency,
      int population) {
    super(name, capitol, language, currency, population);
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    out.writeUTF(name);
    out.writeUTF(capitol);
    out.writeUTF(language);
    out.writeUTF(currency);
    out.writeInt(population);
    out.writeInt(neighbors.length);
    for (int i = 0; i < neighbors.length; ++i) {
      out.writeObject((JavaSerializableCountry) neighbors[i]);
    }
  }

  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    name = in.readUTF();
    capitol = in.readUTF();
    language = in.readUTF();
    currency = in.readUTF();
    population = in.readInt();
    neighbors = new Country[in.readInt()];
    for (int i = 0; i < neighbors.length; ++i) {
      neighbors[i] = (JavaSerializableCountry) in.readObject();
    }
  }

  private void readObjectNoData() throws ObjectStreamException {
    name = "";
    capitol = "";
    language = "";
    currency = "";
    population = 0;
    neighbors = new Country[0];
  }
}
