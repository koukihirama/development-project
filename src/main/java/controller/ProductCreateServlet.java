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

@WebServlet("/products/create")
public class ProductCreateServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/product_management"
          + "?useUnicode=true&characterEncoding=utf8"
          + "&serverTimezone=Asia/Tokyo";

    private static final String USER = "java";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String categoryIdStr = request.getParameter("categoryId");

        String error = null;

        if (name == null || name.isBlank()) {
            error = "商品名は必須です";
        }

        int price = 0;
        int stock = 0;
        int categoryId = 0;

        try {
            price = Integer.parseInt(priceStr);
            stock = Integer.parseInt(stockStr);
            categoryId = Integer.parseInt(categoryIdStr);

            if (price < 0 || stock < 0) {
                error = "価格と在庫は0以上で入力してください";
            }
        } catch (NumberFormatException e) {
            error = "価格・在庫・カテゴリは正しい形式で入力してください";
        }

        if (error != null) {
            request.getSession().setAttribute("error", error);
            response.sendRedirect(request.getContextPath() + "/products/new");
            return;
        }

        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isBlank()) password = "pass";

        String sql = """
            INSERT INTO products (name, price, stock, category_id)
            VALUES (?, ?, ?, ?)
            """;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, password);
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, name);
                ps.setInt(2, price);
                ps.setInt(3, stock);
                ps.setInt(4, categoryId);

                ps.executeUpdate();
            }

            response.sendRedirect(request.getContextPath() + "/products");

        } catch (Exception e) {
            throw new ServletException(e); // ←握りつぶさず原因を見える化
        }
    }
}