<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.File" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.nio.file.Paths" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>File Browser - ${sessionScope.user.username}</title> <%-- Показываем имя пользователя в заголовке --%>
    <style>
        body { font-family: sans-serif; }
        .top-right { position: absolute; top: 10px; right: 10px; }
        ul { list-style: none; padding-left: 0; }
        li { margin-bottom: 5px; }
        a { text-decoration: none; color: #0066cc; }
        a:hover { text-decoration: underline; }
        .logout-btn {
            padding: 5px 10px;
            background-color: #f44336;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .logout-btn:hover { background-color: #d32f2f; }
    </style>
</head>
<body>


<div class="top-right">
    <span>Пользователь: ${sessionScope.user.username}</span>
    <a href="${pageContext.request.contextPath}/logout">
        <button class="logout-btn">Выход</button>
    </a>
</div>

<h2>Содержимое папки</h2>

<p>Дата генерации: ${timestamp}</p>
<p>Текущий путь: ${currentPath}</p>

<ul>
    <c:if test="${!currentPath.equals(userHomePath)}">
        <%
            
                String currentPathStr = (String) request.getAttribute("currentPath");
                if (currentPathStr != null) {
                    Path current = Paths.get(currentPathStr);
                    Path parent = current.getParent();
                    if (parent != null) {
                        // Убедимся, что родитель не выходит за пределы домашней папки
                        Path userHome = Paths.get((String)request.getAttribute("userHomePath"));
                        if (parent.normalize().toString().length() >= userHome.normalize().toString().length()) {
                            pageContext.setAttribute("parentPath", parent.toString().replace('\\', '/'));
                        }
                    }
                }
        %>
        <c:if test="${not empty parentPath}">
            <li><a href="?path=${parentPath}">.. (Вверх)</a></li>
        </c:if>
    </c:if>

    <c:forEach var="file" items="${files}">
        <li>
            <c:choose>
                <c:when test="${file.isDirectory()}">
                    <img src="https://img.icons8.com/color/16/000000/folder-invoices.png" alt="[DIR]"/>
                    <a href="?path=${fn:replace(file.getAbsolutePath(), '\\', '/')}">${file.getName()}</a>
                </c:when>
                <c:otherwise>
                    <img src="https://img.icons8.com/ios-glyphs/16/000000/document.png" alt="[FILE]"/>
                    <a href="download?path=${fn:replace(file.getAbsolutePath(), '\\', '/')}">${file.getName()}</a>
                </c:otherwise>
            </c:choose>
        </li>
    </c:forEach>
    <c:if test="${empty files}">
        <li>Папка пуста</li>
    </c:if>
</ul>

</body>
</html>