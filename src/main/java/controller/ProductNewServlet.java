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

@WebServlet("/products/new")
public class ProductNewServlet extends HttpServlet {

    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/product_management"
          + "?useUnicode=true&characterEncoding=UTF-8"
          + "&serverTimezone=Asia/Tokyo";

    private static final String USER = "java";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	String error = (String) request.getSession().getAttribute("error");
    	if (error != null) {
    	    request.setAttribute("error", error);
    	    request.getSession().removeAttribute("error");
    	}

        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isBlank()) password = "pass";

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT id, category_name FROM categories ORDER BY id";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, password);
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("id"),
                            rs.getString("category_name")
                    ));
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new ServletException(e);
        }

        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/product_new.jsp").forward(request, response);
    }

    // JSPに渡すためのカテゴリ入れ物
    public static class Category {
        private final int id;
        private final String name;

        public Category(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
    }
}
