package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LoginServletTest {

    @Test
    void 正しいIDとパスワードならtrue() {
        assertTrue(LoginServlet.isValidCredential("test", "test"));
    }

    @Test
    void パスワードが違えばfalse() {
        assertFalse(LoginServlet.isValidCredential("test", "wrong"));
    }

    @Test
    void IDがnullならfalse() {
        assertFalse(LoginServlet.isValidCredential(null, "test"));
    }
    
    @Test
    void パスワードがnullならfalse() {
        assertFalse(LoginServlet.isValidCredential("test", null));
    }

}
