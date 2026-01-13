package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // セッション取得（なければ null）
        HttpSession session = request.getSession(false);

        // セッションがあれば破棄
        if (session != null) {
            session.invalidate();
        }

        // ログイン画面へ戻す
        response.sendRedirect(request.getContextPath() + "/login");
    }
}