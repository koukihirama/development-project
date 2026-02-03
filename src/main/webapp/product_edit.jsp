<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="controller.ProductEditServlet.ProductData" %>
<%@ page import="controller.ProductEditServlet.CategoryData" %>

<%
ProductData product = (ProductData) request.getAttribute("product");
List<CategoryData> categories = (List<CategoryData>) request.getAttribute("categories");
%>

<!DOCTYPE html>
<html>
<head><meta charset="UTF-8"><title>商品編集</title></head>
<body>
<h2>商品編集</h2>

<%
String error = (String) request.getAttribute("error");
if (error != null) {
%>
  <p style="color:red;"><%= error %></p>
<%
}
%>

<form method="post" action="<%= request.getContextPath() %>/products/update">
  <input type="hidden" name="id" value="<%= product.getId() %>">

  <div>
    商品名：<input type="text" name="name" value="<%= product.getName() %>">
  </div>
  <div>
    価格：<input type="number" name="price" value="<%= product.getPrice() %>">
  </div>
  <div>
    在庫：<input type="number" name="stock" value="<%= product.getStock() %>">
  </div>

  <div>
    カテゴリ：
    <select name="categoryId">
      <% for (CategoryData c : categories) { %>
        <option value="<%= c.getId() %>" <%= (c.getId() == product.getCategoryId()) ? "selected" : "" %>>
          <%= c.getName() %>
        </option>
      <% } %>
    </select>
  </div>

  <button type="submit">保存</button>
</form>

<p><a href="<%= request.getContextPath() %>/products">一覧へ戻る</a></p>
</body>
</html>