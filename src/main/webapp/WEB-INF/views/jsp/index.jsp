<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Log In</title>
</head>
<body>
<header>
    <h1>CMPE283_Project_Group5</h1>
</header>
<h2>${message}</h2>
<form action="/login" method="post">
    Username: <input type="text" name="user">
    <br>
    Password: <input type="password" name="pwd">
    <br>
    <button type="submit">Log In</button>
</form>
</body>
</html>