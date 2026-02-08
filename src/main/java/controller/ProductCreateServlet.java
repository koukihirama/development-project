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

        String error = validateProductInput(name, priceStr, stockStr, categoryIdStr);

        if (error != null) {
            request.getSession().setAttribute("error", error);
            response.sendRedirect(request.getContextPath() + "/products/new");
            return;
        }

        // ここから下は「パースしてDBに入れる」ので必要
        int price = Integer.parseInt(priceStr);
        int stock = Integer.parseInt(stockStr);
        int categoryId = Integer.parseInt(categoryIdStr);

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
    static String validateProductInput(String name, String priceStr, String stockStr, String categoryIdStr) {
        if (name == null || name.isBlank()) {
            return "商品名は必須です";
        }

        try {
            int price = Integer.parseInt(priceStr);
            int stock = Integer.parseInt(stockStr);
            Integer.parseInt(categoryIdStr); // 形式チェック

            if (price < 0 || stock < 0) {
                return "価格と在庫は0以上で入力してください";
            }
        } catch (NumberFormatException e) {
            return "価格・在庫・カテゴリは正しい形式で入力してください";
        }

        return null; // エラーなし
    }
}