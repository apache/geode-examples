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
package org.apache.geode_examples.lucene;

import java.io.Serializable;
import java.util.Collection;

public class EmployeeData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String firstName;
  private String lastName;
  private int emplNumber;
  private String email;
  private int salary;
  private int hoursPerWeek;
  private Collection<Contact> contacts;

  public EmployeeData(String firstName, String lastName, int emplNumber, String email, int salary,
      int hoursPerWeek, Collection<Contact> contacts) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.emplNumber = emplNumber;
    this.email = email;
    this.salary = salary;
    this.hoursPerWeek = hoursPerWeek;
    this.contacts = contacts;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public int getEmplNumber() {
    return emplNumber;
  }

  public String getEmail() {
    return email;
  }

  public int getSalary() {
    return salary;
  }

  public int getHoursPerWeek() {
    return hoursPerWeek;
  }

  public Collection<Contact> getContacts() {
    return this.contacts;
  }

  @Override
  public String toString() {
    return "EmployeeData [firstName=" + firstName + ", lastName=" + lastName + ", emplNumber="
        + emplNumber + ", email= " + email + ", salary=" + salary + ", hoursPerWeek=" + hoursPerWeek
        + ", contacts=" + contacts + "]";
  }
}
