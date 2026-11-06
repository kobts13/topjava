<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method="POST" action='${pageContext.request.contextPath}/meals' name="frmEditMeal">
    <label hidden="hidden">
        User ID: <input type="number" readonly="readonly" name="id"
                        value="${meal.id}"/>
    </label><br/>
    <label style="display: block; margin-bottom: -10px;">
        DataTime: <input style="margin-left: 110px;"
                         type="datetime-local" name="datetime-local"
                         value="${meal.dateTime}"/>
    </label><br/>
    <label style="display: block;margin-bottom: -10px;">
        Description: <input style="margin-left: 100px;"
                            type="text" name="description"
                            value="${meal.description}"/>
    </label><br/>
    <label style="display: block;margin-bottom: -10px;">
        Calories: <input style="margin-left: 120px;" type="number" name="calories"
                         value="${meal.calories}"/>
    </label><br/>
    <input type="submit" value="Save"/>
    <input type="button" value="Cancel" onclick="window.location.href='${pageContext.request.contextPath}/meals'"/>
</form>
</body>
</html>
