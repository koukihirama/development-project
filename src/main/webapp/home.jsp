<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home</title>
</head>
<body>
<h1>ログイン成功！</h1>

<form method="post" action="<%= request.getContextPath() %>/logout">
    <button type="submit">ログアウト</button>
</form>

</body>
</html>