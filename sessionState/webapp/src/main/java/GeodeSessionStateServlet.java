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
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GeodeSessionStateServlet", urlPatterns = {"/index"})
public class GeodeSessionStateServlet extends HttpServlet {
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession();
    if (session.isNew()) {
      request.setAttribute("isNew", "Session is new.");
    } else {
      request.setAttribute("isNew", "Session already existing");
      session.setMaxInactiveInterval(90);
    }

    if (request.getParameter("action") != null) {
      if (request.getParameter("action").equals("Set Attribute")
          && request.getParameter("key") != null && !request.getParameter("value").equals("null")) {
        session.setAttribute(request.getParameter("key"), request.getParameter("value"));
      }

      if (request.getParameter("action").equals("Get Attribute")
          && request.getParameter("key") != null) {
        request.setAttribute("getKey", session.getAttribute(request.getParameter("key")));
      }

      if (request.getParameter("action").equals("Delete Attribute")
          && request.getParameter("key") != null) {
        session.removeAttribute(request.getParameter("key"));
      }
    }

    request.getRequestDispatcher("/index.jsp").forward(request, response);
  }
}
