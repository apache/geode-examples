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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.quality.Strictness.STRICT_STUBS;

import java.io.IOException;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import org.apache.geode.metrics.MetricsPublishingService;
import org.apache.geode.metrics.MetricsSession;

public class SimpleMetricsPublishingServiceTest {
  @Rule
  public MockitoRule mockitoRule = MockitoJUnit.rule().strictness(STRICT_STUBS);

  @Mock
  public MetricsSession metricsSession;

  private MetricsPublishingService subject;

  @Before
  public void setUp() {
    subject = new SimpleMetricsPublishingService(9000);
  }

  @Test
  public void start_addsRegistryToMetricsSession() {
    subject.start(metricsSession);

    verify(metricsSession).addSubregistry(any(PrometheusMeterRegistry.class));

    subject.stop(metricsSession);
  }

  @Test
  public void start_addsAnHttpEndpointThatReturnsStatusOK() throws IOException {
    subject.start(metricsSession);

    HttpGet request = new HttpGet("http://localhost:9000/");
    HttpResponse response = HttpClientBuilder.create().build().execute(request);

    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

    subject.stop(metricsSession);
  }

  @Test
  public void start_addsAnHttpEndpointThatContainsRegistryData() throws IOException {
    subject.start(metricsSession);

    HttpGet request = new HttpGet("http://localhost:9000/");
    HttpResponse response = HttpClientBuilder.create().build().execute(request);

    String responseBody = EntityUtils.toString(response.getEntity());
    assertThat(responseBody).isEmpty();

    subject.stop(metricsSession);
  }

  @Test
  public void stop_removesRegistryFromMetricsSession() {
    subject.start(metricsSession);
    subject.stop(metricsSession);

    verify(metricsSession).removeSubregistry(any(PrometheusMeterRegistry.class));
  }

  @Test
  public void stop_hasNoHttpEndpointRunning() {
    subject.start(metricsSession);
    subject.stop(metricsSession);

    HttpGet request = new HttpGet("http://localhost:9000/");

    Throwable thrown = catchThrowable(() -> HttpClientBuilder.create().build().execute(request));

    assertThat(thrown).isInstanceOf(HttpHostConnectException.class);
  }
}
