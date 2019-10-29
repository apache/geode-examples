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
package org.apache.geode_examples.micrometerMetrics;

import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;
import static java.lang.Integer.getInteger;
import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;

import org.apache.geode.metrics.MetricsPublishingService;
import org.apache.geode.metrics.MetricsSession;

public class SimpleMetricsPublishingService implements MetricsPublishingService {
  private static final String PORT_PROPERTY = "prometheus.metrics.port";
  private static final int DEFAULT_PORT = 0; // If no port specified, use any port
  private static final String HOSTNAME = "localhost";
  private static final int PORT = getInteger(PORT_PROPERTY, DEFAULT_PORT);

  private static Logger LOG = getLogger(SimpleMetricsPublishingService.class);

  private final int port;
  private MetricsSession session;
  private PrometheusMeterRegistry registry;
  private HttpServer server;

  public SimpleMetricsPublishingService() {
    this(PORT);
  }

  public SimpleMetricsPublishingService(int port) {
    this.port = port;
  }

  @Override
  public void start(MetricsSession session) {
    this.session = session;
    registry = new PrometheusMeterRegistry(DEFAULT);
    this.session.addSubregistry(registry);

    InetSocketAddress address = new InetSocketAddress(HOSTNAME, port);
    server = null;
    try {
      server = HttpServer.create(address, 0);
    } catch (IOException thrown) {
      LOG.error("Exception while starting " + getClass().getSimpleName(), thrown);
    }
    HttpContext context = server.createContext("/");
    context.setHandler(this::requestHandler);
    server.start();

    int boundPort = server.getAddress().getPort();
    LOG.info("Started {} http://{}:{}/", getClass().getSimpleName(), HOSTNAME, boundPort);
  }

  private void requestHandler(HttpExchange httpExchange) throws IOException {
    final byte[] scrapeBytes = registry.scrape().getBytes();
    httpExchange.sendResponseHeaders(200, scrapeBytes.length);
    final OutputStream responseBody = httpExchange.getResponseBody();
    responseBody.write(scrapeBytes);
    responseBody.close();
  }

  @Override
  public void stop(MetricsSession session) {
    session.removeSubregistry(registry);
    registry = null;
    server.stop(0);
  }
}
