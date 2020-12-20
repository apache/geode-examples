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
package org.apache.geode_examples.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Example {
  private static final String GEODE_REST_END_POINT = "http://localhost:8080/gemfire-api/v1/";
  private static final String GET_REQUEST_PARAMETER = "?limit=ALL";
  private static final String POST_REQUEST_PARAMETER = "?key=1";
  private static final String DATA = "{\"name\": \"Dan Smith\", \"technology\": \"Java\"}";

  public static void main(String[] args) throws IOException {
    var httpPostRequestConnection = createHttpPostRequest();
    writeData(httpPostRequestConnection);
    var httpGetRequestConnection = createHttpGetRequest();
    printValues(httpGetRequestConnection);
  }

  private static HttpURLConnection createHttpGetRequest() throws IOException {
    URL url = new URL(GEODE_REST_END_POINT + "example-region" + GET_REQUEST_PARAMETER);
    HttpURLConnection httpURLConnection = getHttpURLConnection(url);
    httpURLConnection.setRequestMethod("GET");
    httpURLConnection.setRequestProperty("Accept", "application/json");
    return httpURLConnection;
  }

  private static void printValues(HttpURLConnection conn) throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
      String response;
      while ((response = br.readLine()) != null) {
        System.out.println(response);
      }
    } finally {
      conn.disconnect();
    }
  }

  private static HttpURLConnection createHttpPostRequest() throws IOException {
    URL url = new URL(GEODE_REST_END_POINT + "example-region" + POST_REQUEST_PARAMETER);
    HttpURLConnection httpURLConnection = getHttpURLConnection(url);
    httpURLConnection.setRequestMethod("POST");
    httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
    httpURLConnection.setRequestProperty("Accept", "application/json");
    httpURLConnection.setDoOutput(true);
    return httpURLConnection;
  }

  private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
    return (HttpURLConnection) url.openConnection();
  }

  private static void writeData(HttpURLConnection conn) throws IOException {
    try (OutputStream outputStream = conn.getOutputStream()) {
      outputStream.write(DATA.getBytes(StandardCharsets.UTF_8));
      conn.getInputStream();
    }
  }
}

