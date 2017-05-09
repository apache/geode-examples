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
package org.apache.geode.examples.loader;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.geode.cache.CacheLoader;
import org.apache.geode.cache.CacheLoaderException;
import org.apache.geode.cache.Declarable;
import org.apache.geode.cache.LoaderHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuoteLoader implements CacheLoader<String, String>, Declarable {
  private static final Logger log = LogManager.getLogger(QuoteLoader.class);

  private final Map<String, String> quotes;

  public QuoteLoader() {
    quotes = getQuotes();
  }

  @Override
  public void init(Properties props) {}

  @Override
  public void close() {}

  @Override
  public String load(LoaderHelper<String, String> helper) throws CacheLoaderException {

    log.info("Loading quote for {} into region {}", helper.getKey(), helper.getRegion().getName());
    String quote = quotes.get(helper.getKey());

    try {
      // simulate network delay for a REST call or a database query
      Thread.sleep(100);
      return quote;

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new CacheLoaderException(e);
    }
  }

  Map<String, String> getQuotes() {
    Map<String, String> quotes = new ConcurrentHashMap<String, String>();

    // sourced from http://www.writersdigest.com/writing-quotes
    quotes.put("Anton Chekhov",
        "My own experience is that once a story has been written, one has to cross out the beginning and the end. It is there that we authors do most of our lying.");
    quotes.put("C. J. Cherryh",
        "It is perfectly okay to write garbage—as long as you edit brilliantly.");
    quotes.put("Dorothy Parker", "I can’t write five words but that I change seven.");
    quotes.put("Douglas Adams",
        "I love deadlines. I like the whooshing sound they make as they fly by.");
    quotes.put("Emily Dickinson", "A wounded deer leaps the highest.");
    quotes.put("Ernest Hemingway", "Prose is architecture, not interior decoration.");
    quotes.put("F. Scott Fitzgerald",
        "Begin with an individual, and before you know it you have created a type; begin with a type, and you find you have created – nothing.");
    quotes.put("Henry David Thoreau",
        "Not that the story need be long, but it will take a long while to make it short.");
    quotes.put("Henry Wadsworth Longfellow",
        "Great is the art of beginning, but greater is the art of ending.");
    quotes.put("Herman Melville", "To produce a mighty book, you must choose a mighty theme.");
    quotes.put("Jean-Paul Sartre", "Poetry creates the myth, the prose writer draws its portrait.");
    quotes.put("Mark Twain",
        "Most writers regard the truth as their most valuable possession, and therefore are most economical in its use.");
    quotes.put("Orson Scott Card",
        "Everybody walks past a thousand story ideas every day. The good writers are the ones who see five or six of them. Most people don’t see any.");
    quotes.put("Ray Bradbury",
        "Any man who keeps working is not a failure. He may not be a great writer, but if he applies the old-fashioned virtues of hard, constant labor, he’ll eventually make some kind of career for himself as writer.");
    quotes.put("Robert Benchley",
        "It took me fifteen years to discover I had no talent for writing, but I couldn’t give it up because by that time I was too famous.");
    quotes.put("Somerset Maugham",
        "If you can tell stories, create characters, devise incidents, and have sincerity and passion, it doesn’t matter a damn how you write.");
    quotes.put("Stephen King",
        "I try to create sympathy for my characters, then turn the monsters loose.");
    quotes.put("Terry Pratchett",
        "There’s no such thing as writer’s block. That was invented by people in California who couldn’t write.");
    quotes.put("Ursula K. Le Guin",
        "The unread story is not a story; it is little black marks on wood pulp. The reader, reading it, makes it live: a live thing, a story.");
    quotes.put("William Faulkner",
        "Get it down. Take chances. It may be bad, but it’s the only way you can do anything really good.");

    return quotes;
  }
}
