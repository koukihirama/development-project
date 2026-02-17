package integration;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IntegrationTest {

    private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/product_management"
      + "?useUnicode=true&characterEncoding=utf8"
      + "&serverTimezone=Asia/Tokyo";

    private static final String USER = "java";
    private static String PASS;

    @BeforeAll
    static void beforeAll() throws Exception {
        PASS = System.getenv("DB_PASSWORD");
        if (PASS == null || PASS.isBlank()) PASS = "pass";
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @BeforeEach
    void setUp() throws Exception {
        // テストが汚れないように、テスト用データだけ消す/用意する
        // 例：名前に IT_ を付けたデータを掃除
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement("DELETE FROM products WHERE name LIKE 'IT_%'")) {
            ps.executeUpdate();
        }
    }

    @Test
    void IT_登録して一覧に存在する() throws Exception {
        int newId = insertProduct("IT_Laptop", 100000, 10, 1);

        assertTrue(existsProductById(newId));
        assertEquals("IT_Laptop", getProductNameById(newId));
    }

    @Test
    void IT_編集して内容が更新される() throws Exception {
        int id = insertProduct("IT_Before", 100, 1, 1);

        updateProduct(id, "IT_After", 200, 2, 2);

        assertEquals("IT_After", getProductNameById(id));
    }

    @Test
    void IT_削除して存在しない() throws Exception {
        int id = insertProduct("IT_DeleteMe", 100, 1, 1);

        deleteProduct(id);

        assertFalse(existsProductById(id));
    }
    
    @Test
    void IT_登録した商品が一覧SQLに含まれる() throws Exception {
        int id = insertProduct("IT_ListIn", 500, 5, 1);

        assertTrue(
            existsInProductListByName("IT_ListIn"),
            "商品一覧に登録した商品が含まれていること"
        );
    }
    
    @Test
    void IT_削除した商品が一覧SQLから消える() throws Exception {
        int id = insertProduct("IT_ListOut", 500, 5, 1);

        deleteProduct(id);

        assertFalse(
            existsInProductListByName("IT_ListOut"),
            "商品一覧に削除した商品が含まれていないこと"
        );
    }

    // --------------------
    // helper methods
    // --------------------
    private int insertProduct(String name, int price, int stock, int categoryId) throws Exception {
        String sql = "INSERT INTO products (name, price, stock, category_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, categoryId);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    private void updateProduct(int id, String name, int price, int stock, int categoryId) throws Exception {
        String sql = "UPDATE products SET name=?, price=?, stock=?, category_id=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, price);
            ps.setInt(3, stock);
            ps.setInt(4, categoryId);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    private void deleteProduct(int id) throws Exception {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private boolean existsProductById(int id) throws Exception {
        String sql = "SELECT 1 FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private String getProductNameById(int id) throws Exception {
        String sql = "SELECT name FROM products WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("name");
            }
        }
    }
    
    private boolean existsInProductListByName(String name) throws Exception {
        String sql =
            "SELECT p.name " +
            "FROM products p " +
            "LEFT JOIN categories c ON p.category_id = c.id " +
            "ORDER BY p.id";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                if (name.equals(rs.getString("name"))) {
                    return true;
                }
            }
            return false;
        }
    }
}
