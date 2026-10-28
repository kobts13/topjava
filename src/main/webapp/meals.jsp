<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1" cellspacing="0" cellpadding="10">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <jsp:useBean id="mealTos" scope="request" type="java.util.List<ru.javawebinar.topjava.model.MealTo>"/>
    <c:forEach var="meal" items="${mealTos}">
        <tr>
            <c:set var="color" value="${meal.excess ? 'red' : 'green'}"/>
            <td><span
                    style="color: ${color}">${meal.dateTime.format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}</span>
            </td>
            <td><span style="color: ${color}">${meal.description}</span></td>
            <td><span style="color: ${color}">${meal.calories}</span></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
