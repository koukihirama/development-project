<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
</head>
<body>

<h1>ログイン画面</h1>

<%-- エラーメッセージがあれば表示 --%>
<%
  String error = (String) request.getAttribute("error");
  if (error != null) {
%>
  <p style="color:red;"><%= error %></p>
<%
  }
%>

<form method="post" action="<%= request.getContextPath() %>/login">
  <div>
    <label>ID：</label>
    <input type="text" name="userId">
  </div>

  <div>
    <label>パスワード：</label>
    <input type="password" name="password">
  </div>

  <button type="submit">ログイン</button>
</form>

</body>
</html>
