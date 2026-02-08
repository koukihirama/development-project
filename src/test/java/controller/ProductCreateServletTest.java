package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductCreateServletTest {

    @Test
    void 正常入力ならエラーなし() {
        String error = ProductCreateServlet.validateProductInput(
                "Laptop", "100000", "10", "1"
        );
        assertNull(error);
    }

    @Test
    void 商品名が空ならエラー() {
        String error = ProductCreateServlet.validateProductInput(
                "", "100000", "10", "1"
        );
        assertEquals("商品名は必須です", error);
    }

    @Test
    void 価格が負数ならエラー() {
        String error = ProductCreateServlet.validateProductInput(
                "Laptop", "-1", "10", "1"
        );
        assertEquals("価格と在庫は0以上で入力してください", error);
    }

    @Test
    void 数字に変換できないならエラー() {
        String error = ProductCreateServlet.validateProductInput(
                "Laptop", "abc", "10", "1"
        );
        assertEquals("価格・在庫・カテゴリは正しい形式で入力してください", error);
    }
    
    @Test
    void 在庫が負数ならエラー() {
        String error = ProductCreateServlet.validateProductInput(
                "Laptop", "100000", "-1", "1"
        );
        assertEquals("価格と在庫は0以上で入力してください", error);
    }
    
    @Test
    void カテゴリが数字でないならエラー() {
        String error = ProductCreateServlet.validateProductInput(
                "Laptop", "100000", "10", "abc"
        );
        assertEquals("価格・在庫・カテゴリは正しい形式で入力してください", error);
    }
}