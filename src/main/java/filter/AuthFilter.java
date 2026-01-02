package filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        // ログイン画面とログイン処理は通す
        boolean isLogin = path.equals(contextPath + "/login");
        boolean isStatic = path.endsWith(".css") || path.endsWith(".js");

        if (isLogin || isStatic) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            request.setAttribute("error", "ログインしてください");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        chain.doFilter(req, res);
    }
}