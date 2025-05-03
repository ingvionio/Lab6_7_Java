package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.example.model.User;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/download")
public class FileDownloadServlet extends HttpServlet {

    private static final String BASE_USER_DIR = "C:/lab5Test";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        User user = (User) session.getAttribute("user");
        String username = user.getUsername();
        Path userHomePath = Paths.get(BASE_USER_DIR, username).toAbsolutePath().normalize();
        File userHomeFile = userHomePath.toFile();

        String requestedPathStr = request.getParameter("path");

        File file;
        try {
            file = new File(requestedPathStr);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path.");
            return;
        }

        if (!file.exists() || !file.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found.");
            return;
        }

        response.setContentType("application/octet-stream");
        response.setContentLengthLong(file.length());
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
        response.setHeader(headerKey, headerValue);

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}