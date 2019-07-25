<!--
 Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 agreements. See the NOTICE file distributed with this work for additional information regarding
 copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance with the License. You may obtain a
 copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 or implied. See the License for the specific language governing permissions and limitations under
 the License.
-->
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*"
%>
<% session=request.getSession();%>
<html>

<head>
  <title>
  </title>
</head>

<body>
<div align="center">
  <table border="1">
    <tr>
      <td>
        Is New Session
      </td>
      <td>
        <% out.print(request.getAttribute("isNew")); %>
      </td>
    </tr>
    <tr>
      <td>
        Session Time Out
      </td>
      <td>
        <% out.println(session.getMaxInactiveInterval());%>
      </td>
    </tr>
    <tr>
      <td>
        Session Creation Time
      </td>
      <td>
        <% out.print(new Date(session.getCreationTime())); %>
      </td>
    </tr>
    <tr>
      <td>
        Session Last Accessed Time
      </td>
      <td>
        <% out.println(new Date(session.getLastAccessedTime())); %>
      </td>
    </tr>
  </table>
  <form action="" method="get">
    <table>
      <tr>
        <td>
          key:
        </td>
        <td>
          <input type="text" name="key">
        </td>
      </tr>
      <tr>
        <td>
          value:
        </td>
        <td>
          <input type="text" name="value">
        </td>
      </tr>
      <tr>
        <td>
          <input type="submit" name="action" value="Set Attribute">
        </td>
      </tr>
    </table>
  </form>
  <form action="" method="get">
    <table>
      <tr>
        <td>
          key:
        </td>
        <td>
          <input type="text" name="key">
        </td>
      </tr>
      <tr>
        <td>
          <input type="submit" name="action" value="Get Attribute" />
        </td>
        <td>
          <input type="submit" name="action" value="Delete Attribute" />
        </td>
      </tr>
    </table>
  </form>
  <p>
    result:
    <%out.println(request.getAttribute("getKey"));%>
  </p>
</div>
</body>

</html>