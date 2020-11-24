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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Example {
  private static final String GEODE_REST_END_POINT = "http://localhost:8080/gemfire-api/v1/";
  private static final String QUERY_PARAMETER = "?limit=ALL";
  private static final String data = "{\"name\": \"Dan Smith\", \"technology\": \"Java\"}";

  public static void main(String[] args) throws IOException, InterruptedException {
    var httpClient = createHttpClient();
    HttpRequest postRequest = createHttpPostRequest();
    sendHttpPostRequest(httpClient, postRequest);
    HttpRequest getRequest = createHttpGetRequest();
    sendHttpGetRequest(httpClient, getRequest);
  }

  private static HttpRequest createHttpPostRequest() {
    return HttpRequest.newBuilder().uri(URI.create(GEODE_REST_END_POINT + "example-region"))
        .header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(data))
        .build();
  }

  private static HttpRequest createHttpGetRequest() {
    return HttpRequest.newBuilder()
        .uri(URI.create(GEODE_REST_END_POINT + "example-region" + QUERY_PARAMETER)).GET().build();
  }

  static void sendHttpPostRequest(HttpClient httpClient, HttpRequest httpRequest)
      throws IOException, InterruptedException {
    HttpResponse<String> httpResponse =
        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    System.out.println("Response status code " + httpResponse.statusCode());
  }

  static void sendHttpGetRequest(HttpClient httpClient, HttpRequest httpRequest)
      throws IOException, InterruptedException {
    HttpResponse<String> httpResponse =
        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    System.out.println("Response status code " + httpResponse.statusCode());
    System.out.println("Response body " + httpResponse.body());
  }

  private static HttpClient createHttpClient() {
    return HttpClient.newBuilder().build();
  }
}

