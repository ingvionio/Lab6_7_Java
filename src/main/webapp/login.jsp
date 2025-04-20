<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
    <style>
        .error { color: red; }
    </style>
</head>
<body>
<h2>Вход в систему</h2>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>
<c:if test="${not empty message}">
    <p style="color: green;">${message}</p>
</c:if>

<form action="login" method="post">
    <div>
        <label for="username">Логин:</label>
        <input type="text" id="username" name="username" required>
    </div>
    <div>
        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div>
        <button type="submit">Войти</button>
    </div>
</form>

<p>Нет аккаунта? <a href="register">Зарегистрироваться</a></p>
</body>
</html>