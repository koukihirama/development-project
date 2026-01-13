<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="controller.ProductNewServlet.Category" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>商品登録</title>
</head>
<body>

<h1>商品登録</h1>

<%
String error = (String) request.getAttribute("error");
if (error != null) {
%>
  <p style="color:red;"><%= error %></p>
<%
}
%>

<%
List<Category> categories = (List<Category>) request.getAttribute("categories");
%>

<form method="post" action="<%= request.getContextPath() %>/products/create">

  <div>
    商品名：
    <input type="text" name="name">
  </div>

  <div>
    価格：
    <input type="number" name="price">
  </div>

  <div>
    在庫数：
    <input type="number" name="stock">
  </div>

  <div>
    カテゴリ：
    <select name="categoryId">
      <% for (Category c : categories) { %>
        <option value="<%= c.getId() %>"><%= c.getName() %></option>
      <% } %>
    </select>
  </div>

  <button type="submit">登録</button>
</form>

</body>
</html>