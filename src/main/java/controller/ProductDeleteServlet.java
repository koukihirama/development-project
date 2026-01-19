package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products/delete")
public class ProductDeleteServlet extends HttpServlet {

  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/product_management"
    + "?useUnicode=true&characterEncoding=utf8"
    + "&serverTimezone=Asia/Tokyo";

  private static final String USER = "java";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    String idStr = req.getParameter("id");
    if (idStr == null || idStr.isEmpty()) {
      req.getSession().setAttribute("error", "不正な操作です");
      resp.sendRedirect(req.getContextPath() + "/products");
      return;
    }

    int id;
    try {
      id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
      req.getSession().setAttribute("error", "不正な商品IDです");
      resp.sendRedirect(req.getContextPath() + "/products");
      return;
    }

    String password = System.getenv("DB_PASSWORD");
    if (password == null || password.isBlank()) password = "pass";

    String sql = "DELETE FROM products WHERE id = ?";

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      try (Connection conn = DriverManager.getConnection(URL, USER, password);
           PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        int deleted = ps.executeUpdate();

        if (deleted == 0) {
          req.getSession().setAttribute("error", "削除対象の商品が存在しません");
        }
      }

      resp.sendRedirect(req.getContextPath() + "/products");

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
