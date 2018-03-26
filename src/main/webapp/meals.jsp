<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@taglib uri="http://ru.javawebinar.topjava/functions" prefix="f" %>
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 26.03.2018
  Time: 20:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table border=1>
    <thead>
    <tr>

        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Exceed</th>
        <%--<th colspan=2>Action</th>--%>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${list}" var="meal">

            <tr >
                <td><c:out value="${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy : HH.mm ')}" /></td>
                <td><c:out value="${meal.description}" /></td>
                <td><c:out value="${meal.calories}" /></td>
                <td><c:out value="${meal.exceed}" /></td>

                    <%--<td><a href="UserController?action=edit&userId=<c:out value="${user.userid}"/>">Update</a></td>--%>
                    <%--<td><a href="UserController?action=delete&userId=<c:out value="${user.userid}"/>">Delete</a></td>--%>
            </tr>

    </c:forEach>
    </tbody>
</table>

</body>
</html>
