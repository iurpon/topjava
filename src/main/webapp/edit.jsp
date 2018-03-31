
<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.03.2018
  Time: 0:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>EDIT MEAL</title>


</head>
<body>
<c:if test="${param.id != -1}">
    <h2>EDIT MEAL</h2>
</c:if>
<c:if test="${param.id == -1}">
    <h2>ADD NEW MEAL</h2>
</c:if>

<form method="POST" action='meals' name="frmAddUser">
    <input type="number"  name = "id" hidden
        value="<c:out value="${meal.id}" />" /> <br />
    Date&Time : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.dateTime}" />" /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />

        <input type="submit" value="Submit" />
</form>

</body>
</html>
