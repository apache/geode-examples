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
package org.apache.geode_examples.writer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SSNVetterTest {
  @Test
  public void testIsValid() throws Exception {
    SSNVetter vetter = new SSNVetter();

    assertTrue(vetter.isValid("123-45-6789"));
    assertFalse(vetter.isValid("666-66-6666"));
    assertTrue(vetter.isValid("777-77-7777"));
    assertFalse(vetter.isValid("8675309"));
    assertFalse(vetter.isValid("999-000-0000"));
  }
}
