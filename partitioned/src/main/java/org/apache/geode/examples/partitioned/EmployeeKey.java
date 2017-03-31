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
package org.apache.geode.examples.partitioned;

import java.io.Serializable;

public class EmployeeKey implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private int emplNumber;

  public EmployeeKey() {}

  public EmployeeKey(String n, int en) {
    this.name = n;
    this.emplNumber = en;
  }

  public String getName() {
    return (name);
  }

  public int getEmplNumber() {
    return (emplNumber);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    EmployeeKey that = (EmployeeKey) o;

    if (emplNumber != that.emplNumber) {
      return false;
    }
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + emplNumber;
    return result;
  }

  public String toString() {
    return ("Name: " + this.name + " Employee Number: " + this.emplNumber);
  }

}
