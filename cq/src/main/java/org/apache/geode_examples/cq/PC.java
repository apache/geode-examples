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
package org.apache.geode_examples.cq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;

public class PC {
  private static final String[] names =
      "Arnd,Bigby,Drawmij,Elminster,Flint,Gord,Hazlik,Iggwilv,Loran,Mordenkainen,Nystul,Otiluke,Porthios,Raistlin,Sturm,Tenser,Vecna"
          .split(",");
  private String name = "";
  private int hp = 0;

  public static PC generate() {
    final Random random = new Random();
    final PrimitiveIterator.OfInt nameIndexes = random.ints(0, names.length).iterator();
    final PrimitiveIterator.OfInt hps = random.ints(10, 20).iterator();
    return new PC(names[nameIndexes.next()], hps.next());
  }

  public PC() {
    // Do nothing.
  }

  public PC(String name, int hp) {
    this.name = name;
    this.hp = hp;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getHp() {
    return hp;
  }

  public void setHp(int hp) {
    this.hp = hp;
  }

  @Override
  public String toString() {
    return "PC[name=" + name + ",hp=" + Integer.toString(hp) + "]";
  }
}
