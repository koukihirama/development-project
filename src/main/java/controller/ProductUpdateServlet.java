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

@WebServlet("/products/update")
public class ProductUpdateServlet extends HttpServlet {

  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/product_management"
    + "?useUnicode=true&characterEncoding=utf8"
    + "&serverTimezone=Asia/Tokyo";

  private static final String USER = "java";

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");

    String idStr = req.getParameter("id");
    String name = req.getParameter("name");
    String priceStr = req.getParameter("price");
    String stockStr = req.getParameter("stock");
    String categoryIdStr = req.getParameter("categoryId");

    // ---- バリデーション（登録と同じルールでOK）----
    String error = null;

    if (idStr == null || idStr.isBlank()) {
      error = "不正な操作です";
    }
    if (error == null && (name == null || name.isBlank())) {
      error = "商品名は必須です";
    }

    int id = 0, price = 0, stock = 0, categoryId = 0;
    if (error == null) {
      try {
        id = Integer.parseInt(idStr);
        price = Integer.parseInt(priceStr);
        stock = Integer.parseInt(stockStr);
        categoryId = Integer.parseInt(categoryIdStr);

        if (price < 0 || stock < 0) {
          error = "価格と在庫は0以上で入力してください";
        }
      } catch (NumberFormatException e) {
        error = "価格・在庫・カテゴリは正しい形式で入力してください";
      }
    }

    if (error != null) {
      // 編集画面に戻す（最短はsessionで返してeditにredirect）
      req.getSession().setAttribute("error", error);
      resp.sendRedirect(req.getContextPath() + "/products/edit?id=" + idStr);
      return;
    }

    String password = System.getenv("DB_PASSWORD");
    if (password == null || password.isBlank()) password = "pass";

    String sql = """
        UPDATE products
        SET name = ?, price = ?, stock = ?, category_id = ?
        WHERE id = ?
        """;

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      try (Connection conn = DriverManager.getConnection(URL, USER, password);
           PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, name);
        ps.setInt(2, price);
        ps.setInt(3, stock);
        ps.setInt(4, categoryId);
        ps.setInt(5, id);

        int updated = ps.executeUpdate();
        if (updated == 0) {
          req.getSession().setAttribute("error", "更新対象の商品が存在しません");
        }
      }

      resp.sendRedirect(req.getContextPath() + "/products");

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }
}
