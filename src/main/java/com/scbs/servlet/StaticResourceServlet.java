package com.scbs.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(urlPatterns = {"/static/*"})
public class StaticResourceServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Determine MIME type
        if (pathInfo.endsWith(".css")) {
            resp.setContentType("text/css;charset=UTF-8");
        } else if (pathInfo.endsWith(".js")) {
            resp.setContentType("application/javascript;charset=UTF-8");
        } else if (pathInfo.endsWith(".png")) {
            resp.setContentType("image/png");
        } else if (pathInfo.endsWith(".svg")) {
            resp.setContentType("image/svg+xml");
        } else if (pathInfo.endsWith(".ico")) {
            resp.setContentType("image/x-icon");
        } else {
            resp.setContentType("text/plain;charset=UTF-8");
        }

        // Try getting from ServletContext real path or classpath
        String realPath = getServletContext().getRealPath("/static" + pathInfo);
        File file = (realPath != null) ? new File(realPath) : null;

        if (file == null || !file.exists()) {
            // Fallback for standalone embedded development
            file = new File("src/main/webapp/static" + pathInfo);
            if (!file.exists()) {
                file = new File("scbs-jsp-servlet/src/main/webapp/static" + pathInfo);
            }
        }

        if (file != null && file.exists() && file.isFile()) {
            resp.setContentLengthLong(file.length());
            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = resp.getOutputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                out.flush();
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
