package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/product_management"
          + "?useUnicode=true&characterEncoding=utf8"
          + "&serverTimezone=Asia/Tokyo";

    private static final String USER = "java";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String sessionError = (String) request.getSession().getAttribute("error");
    	if (sessionError != null) {
    	    request.setAttribute("error", sessionError);
    	    request.getSession().removeAttribute("error"); // 1回表示したら消す
    	}

        // DBパスワード（環境変数が無ければローカル用に pass）
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isBlank()) {
            password = "pass";
        }

        // 表示用に「行」をまとめて持つ（まずは String でもOK）
        List<ProductRow> products = new ArrayList<>();

        String sql =
                "SELECT p.id, p.name, p.price, p.stock, c.category_name " +
                "FROM products p " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "ORDER BY p.id";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, password);
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    products.add(new ProductRow(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("price"),
                            rs.getInt("stock"),
                            rs.getString("category_name")
                    ));
                }
            }

            request.setAttribute("products", products);
            request.getRequestDispatcher("/product_list.jsp").forward(request, response);

        } catch (ClassNotFoundException | SQLException e) {
            // 失敗を握りつぶさず、画面に出せる形で返す
            request.setAttribute("error", "商品一覧の取得に失敗しました: " + e.getMessage());
            request.setAttribute("products", products); // 空でも渡す
            request.getRequestDispatcher("/product_list.jsp").forward(request, response);
        }
    }

    // JSPに渡すための「1行分のデータ」をまとめたクラス（Servlet内に置いてOK）
    public static class ProductRow {
        private final int id;
        private final String name;
        private final int price;
        private final int stock;
        private final String categoryName;

        public ProductRow(int id, String name, int price, int stock, String categoryName) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
            this.categoryName = categoryName;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public int getPrice() { return price; }
        public int getStock() { return stock; }
        public String getCategoryName() { return categoryName; }
    }
}