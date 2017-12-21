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
package org.apache.geode_examples.clientSecurity;

import java.util.Properties;

import org.apache.logging.log4j.Logger;

import org.apache.geode.LogWriter;
import org.apache.geode.distributed.DistributedMember;
import org.apache.geode.internal.logging.LogService;
import org.apache.geode.security.AuthInitialize;
import org.apache.geode.security.AuthenticationFailedException;

public class ExampleAuthInit implements AuthInitialize {

  private static final Logger logger = LogService.getLogger();

  private static final String USER_NAME = "security-username";
  private static final String PASSWORD = "security-password";

  private static final String INSECURE_PASSWORD_FOR_EVERY_USER = "123";

  /**
   * The implementer would use their existing infrastructure (e.g., ldap) here to populate these
   * properties with the user credentials. These properties will in turn be handled by the
   * implementer's design of SecurityManager to authenticate users and authorize operations.
   */
  @Override
  public Properties getCredentials(Properties securityProps) throws AuthenticationFailedException {
    Properties credentials = new Properties();
    String userName = securityProps.getProperty(USER_NAME);
    if (userName == null) {
      throw new AuthenticationFailedException(
          "ExampleAuthInit: user name property [" + USER_NAME + "] not set.");
    }
    credentials.setProperty(USER_NAME, userName);
    credentials.setProperty(PASSWORD, INSECURE_PASSWORD_FOR_EVERY_USER);
    logger.info("SampleAuthInit: successfully obtained credentials for user " + userName);
    return credentials;
  }

  @Override
  public void close() {}

  @Override
  @Deprecated
  public void init(LogWriter systemLogger, LogWriter securityLogger)
      throws AuthenticationFailedException {}

  @Override
  @Deprecated
  public Properties getCredentials(Properties securityProps, DistributedMember server,
      boolean isPeer) throws AuthenticationFailedException {
    return getCredentials(securityProps);
  }
}
