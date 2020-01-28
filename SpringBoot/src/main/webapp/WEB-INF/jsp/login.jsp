<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<body>
    <div>
       <form action="/signin" method="post">
       Name : <input type="text" name="usernameOrEmail" value="${login.usernameOrEmail}">
       <font color=red><form:errors path="login.usernameOrEmail"/></font><br/><br/>
       
       Password : <input type="text" name="password" value="${login.password}">
 		<font color=red><form:errors path="login.password"/></font><br/><br/>
      
       <input type="submit" value="Login">
       </form>
    </div>
</body>
</html>