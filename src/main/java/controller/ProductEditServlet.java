package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products/edit")
public class ProductEditServlet extends HttpServlet {

  private static final String URL =
      "jdbc:mysql://127.0.0.1:3306/product_management"
    + "?useUnicode=true&characterEncoding=utf8"
    + "&serverTimezone=Asia/Tokyo";

  private static final String USER = "java";

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	  
	String sessionError = (String) req.getSession().getAttribute("error");
	  if (sessionError != null) {
	    req.setAttribute("error", sessionError);
	    req.getSession().removeAttribute("error");
    }

    String idStr = req.getParameter("id");
    if (idStr == null || idStr.isBlank()) {
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

    // 商品1件
    String productSql = "SELECT id, name, price, stock, category_id FROM products WHERE id = ?";
    // カテゴリ一覧
    String categorySql = "SELECT id, category_name FROM categories ORDER BY id";

    try {
      Class.forName("com.mysql.cj.jdbc.Driver");

      try (Connection conn = DriverManager.getConnection(URL, USER, password)) {

        // 商品取得
        ProductData product = null;
        try (PreparedStatement ps = conn.prepareStatement(productSql)) {
          ps.setInt(1, id);
          try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
              product = new ProductData(
                  rs.getInt("id"),
                  rs.getString("name"),
                  rs.getInt("price"),
                  rs.getInt("stock"),
                  rs.getInt("category_id")
              );
            }
          }
        }

        if (product == null) {
          req.getSession().setAttribute("error", "編集対象の商品が存在しません");
          resp.sendRedirect(req.getContextPath() + "/products");
          return;
        }

        // カテゴリ取得
        List<CategoryData> categories = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(categorySql);
             ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            categories.add(new CategoryData(rs.getInt("id"), rs.getString("category_name")));
          }
        }

        req.setAttribute("product", product);
        req.setAttribute("categories", categories);

        req.getRequestDispatcher("/product_edit.jsp").forward(req, resp);
      }

    } catch (Exception e) {
      throw new ServletException(e);
    }
  }

  // 編集フォーム表示用
  public static class ProductData {
    private final int id;
    private final String name;
    private final int price;
    private final int stock;
    private final int categoryId;

    public ProductData(int id, String name, int price, int stock, int categoryId) {
      this.id = id;
      this.name = name;
      this.price = price;
      this.stock = stock;
      this.categoryId = categoryId;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public int getCategoryId() { return categoryId; }
  }

  public static class CategoryData {
    private final int id;
    private final String name;
    public CategoryData(int id, String name) {
      this.id = id;
      this.name = name;
    }
    public int getId() { return id; }
    public String getName() { return name; }
  }
}