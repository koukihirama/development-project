<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h2>削除確認</h2>

<p>本当に削除しますか？</p>

<form method="post" action="<%= request.getContextPath() %>/products/delete">
  <input type="hidden" name="id" value="<%= request.getAttribute("id") %>">
  <button type="submit">はい</button>
</form>

<a href="<%= request.getContextPath() %>/products">いいえ</a>

</body>
</html>