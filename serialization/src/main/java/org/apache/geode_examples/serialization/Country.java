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
package org.apache.geode_examples.serialization;

/**
 * <strong>Explicitly</strong> not serializable by java.io.Serializable,
 * org.apache.geode.DataSerializable, or org.apache.geode.pdx.PdxSerializable.
 */
public class Country {
  protected String name;
  protected String capitol;
  protected String language;
  protected String currency;
  protected int population;

  public Country() {
    this("", "", "", "", 0);
  }

  protected Country(String name, String capitol, String language, String currency, int population) {
    this.name = name;
    this.capitol = capitol;
    this.language = language;
    this.currency = currency;
    this.population = population;
  }

  public String getName() {
    return name;
  }

  public String getCapitol() {
    return capitol;
  }

  public void setCapitol(String capitol) {
    this.capitol = capitol;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (name != null && !name.isEmpty()) {
      builder.append(name);
      builder.append(" (");

      if (capitol != null && !capitol.isEmpty()) {
        if (0 < builder.length() && '(' != builder.charAt(builder.length() - 1)) {
          builder.append(", ");
        }
        builder.append("Capitol: ");
        builder.append(capitol);
      }

      if (language != null && !language.isEmpty()) {
        if (0 < builder.length() && '(' != builder.charAt(builder.length() - 1)) {
          builder.append(", ");
        }
        builder.append("Language: ");
        builder.append(language);
      }

      if (currency != null && !currency.isEmpty()) {
        if (0 < builder.length() && '(' != builder.charAt(builder.length() - 1)) {
          builder.append(", ");
        }
        builder.append("Currency: ");
        builder.append(currency);
      }

      if (0 < population) {
        if (0 < builder.length() && '(' != builder.charAt(builder.length() - 1)) {
          builder.append(", ");
        }
        builder.append("Population: ");
        builder.append(population);
      }

      builder.append(")");
    }
    return builder.toString();
  }
}
