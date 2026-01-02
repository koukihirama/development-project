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
    private static final long serialVersionUID = 1L;

    // GET /login → ログイン画面表示
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // POST /login → ログイン処理
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ① フォームから送られた値を受け取る
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        // ② 仮認証（まずは test/test で成功扱い）
        if ("test".equals(userId) && "test".equals(password)) {

            // ③ セッションを作ってログイン状態を保存
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", userId);

            // ④ ログイン後ページへリダイレクト（再送信防止）
            response.sendRedirect(request.getContextPath() + "/home.jsp");
            return;
        }

        // ⑤ 失敗：エラーメッセージをセットしてログイン画面に戻す
        request.setAttribute("error", "IDまたはパスワードが違います");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}