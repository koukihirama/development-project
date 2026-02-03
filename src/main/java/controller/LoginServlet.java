package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        if ("test".equals(userId) && "test".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", userId);

            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        request.setAttribute("error", "IDまたはパスワードが違います");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}