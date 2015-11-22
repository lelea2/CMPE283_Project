<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Create Tenant</title>
</head>
<body>
<h2>${createTenant}</h2>
<form action="/createTenant" method="post">
  Username: <input type="text" name="name">
  <br>
  Password: <input type="text" name="desc">
  <br>
  <button type="submit">Create Tenant</button>
</form>
</body>
</html>
