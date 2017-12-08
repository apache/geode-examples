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

import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;

public class PdxSerializableCountry extends Country implements PdxSerializable {
  public PdxSerializableCountry() {
    super();
  }

  public PdxSerializableCountry(String name, String capitol, String language, String currency,
      int population) {
    super(name, capitol, language, currency, population);
  }

  @Override
  public void toData(PdxWriter writer) {
    writer.writeString("name", name);
    writer.writeString("capitol", capitol);
    writer.writeString("language", language);
    writer.writeString("currency", currency);
    writer.writeInt("population", population);
  }

  @Override
  public void fromData(PdxReader reader) {
    name = reader.readString("name");
    capitol = reader.readString("capitol");
    language = reader.readString("language");
    currency = reader.readString("currency");
    population = reader.readInt("population");
  }
}
