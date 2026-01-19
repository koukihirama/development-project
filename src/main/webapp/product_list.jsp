<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="controller.ProductListServlet.ProductRow" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>商品一覧</title>
</head>
<body>

<h1>商品一覧</h1>

<a href="<%= request.getContextPath() %>/products/new">商品登録</a>
<hr>

<%
String error = (String) request.getAttribute("error");
if (error != null) {
%>
  <p style="color:red;"><%= error %></p>
<%
}
%>

<%
List<ProductRow> products = (List<ProductRow>) request.getAttribute("products");
%>

<% if (products == null || products.isEmpty()) { %>
  <p>登録された商品はまだありません。</p>
<% } else { %>
  <table border="1" cellpadding="6">
  <tr>
    <th>ID</th>
    <th>商品名</th>
    <th>価格</th>
    <th>在庫</th>
    <th>カテゴリ</th>
    <th>操作</th>
  </tr>

  <% for (ProductRow p : products) { %>
    <tr>
      <td><%= p.getId() %></td>
      <td><%= p.getName() %></td>
      <td><%= p.getPrice() %></td>
      <td><%= p.getStock() %></td>
      <td><%= p.getCategoryName() %></td>
      <td>
        <a href="<%= request.getContextPath() %>/products/delete/confirm?id=<%= p.getId() %>">
          削除
        </a>
      </td>
    </tr>
  <% } %>
</table>
<% } %>

</body>
</html>