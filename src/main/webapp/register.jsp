<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Registration</title>
    <style>
        .error { color: red; }
    </style>
</head>
<body>
<h2>Регистрация</h2>

<c:if test="${not empty error}">
    <p class="error">${error}</p>
</c:if>

<form action="register" method="post">
    <div>
        <label for="username">Логин:</label>
        <input type="text" id="username" name="username" value="${param.username}" required>
    </div>
    <div>
        <label for="email">Email:</label>
        <input type="text" id="email" name="email" value="${param.email}" required>
    </div>
    <div>
        <label for="password">Пароль:</label>
        <input type="password" id="password" name="password" required>
    </div>
    <div>
        <button type="submit">Зарегистрироваться</button>
    </div>
</form>

<p>Уже есть аккаунт? <a href="login">Войти</a></p>
</body>
</html>