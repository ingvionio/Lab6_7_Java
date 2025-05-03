package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/files")
public class FileBrowserServlet extends HttpServlet {

    private static final String BASE_USER_DIR = "C:/lab5Test";
    private static final Map<String, String> firstVisitTimes = new HashMap<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);


        User user = (User) session.getAttribute("user");
        String username = user.getUsername();


        Path userHomePath = Paths.get(BASE_USER_DIR, username).toAbsolutePath().normalize();

        if (!Files.exists(userHomePath)) {
            try {
                Files.createDirectories(userHomePath);

            } catch (IOException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не удалось создать домашнюю директорию пользователя.");
                return;
            }
        }

        String requestedPathStr = request.getParameter("path");
        Path currentPath;

        if (requestedPathStr == null || requestedPathStr.trim().isEmpty()) {
            currentPath = userHomePath;
        } else {

            try {
                currentPath = Paths.get(requestedPathStr).toAbsolutePath().normalize();
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Некорректный путь.");
                return;
            }


            File currentFile = currentPath.toFile();
            File userHomeFile = userHomePath.toFile();

            if (!currentFile.getCanonicalPath().startsWith(userHomeFile.getCanonicalPath())) {
                response.sendRedirect(request.getContextPath() + "/files");
                return;
            }
        }

        File currentDirectory = currentPath.toFile();


        request.setAttribute("currentPath", currentPath.toString().replace('\\', '/')); // Используем единообразные слеши
        request.setAttribute("files", currentDirectory.listFiles()); // Получаем список файлов/папок
        request.setAttribute("userHomePath", userHomePath.toString().replace('\\', '/')); // Передаем путь к домашней папке для логики "Вверх"
        if (firstVisitTimes.containsKey(currentPath.toString())) {
            request.setAttribute("timestamp", firstVisitTimes.get(currentPath.toString()));
        }
        else {
            firstVisitTimes.put(currentPath.toString(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            request.setAttribute("timestamp", firstVisitTimes.get(currentPath.toString()));
        }

        request.getRequestDispatcher("/file_browser.jsp").forward(request, response);
    }
}