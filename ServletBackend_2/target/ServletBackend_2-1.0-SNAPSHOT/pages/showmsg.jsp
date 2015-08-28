<%@page import="ru.icl.test.beans.Messages"%>
<%@page import="ru.icl.test.beans.MessagesList"%>
<%@ page pageEncoding="UTF-8" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Онлайн ЧАТ</title>
        <link href="../css/style_main.css" rel="stylesheet" type="text/css">
    </head>    
    <body>
    <%--
        request.setCharacterEncoding("UTF-8");
        
        if (request.getParameter("username") != null) {
            session.setAttribute("username", request.getParameter("username"));
            
        }
    --%>     
    <div class="container">        
        <div class="logo">
            <a href="main.jsp"><img src="../Images/chat_.png" width="86" height="87" alt="" name="logo" /></a>
        </div>
        <div class="descr">
            <h3>Онлайн ЧАТ</h3>
        </div>
        <div class="welcome">
            <h5>Добро пожаловать, <%=session.getAttribute("username")%> !</h5>
            <h5 class="sessia">Ваш ClientId: <%=session.getId()%></h5>
            <h6>
                <a href="../pages/main.jsp"><-Назад</a>
                &nbsp;
                <a href="../index.jsp">Выход</a>
            </h6>
        </div>            
        <jsp:useBean id="msgList" class="ru.icl.test.beans.MessagesList" scope="application"/>
        <div class="big_column">               
            <div>
                <table>
                    <tr>
                        <th>id</th> 
                        <th>ClientId</th> 
                        <th>Message</th>
                    </tr>
                    <%
                        for (Messages messages : msgList.getMessagesList()) {
                    %>                     
                    <tr>
                        <td><%=messages.getId()%></td>
                        <td><%=messages.getClientid()%></td>
                        <td><%=messages.getMessage()%></td>
                    </tr>
                    <%}%>
                </table>
            </div>                                                                   
        </div>                     
    </div>                                                                                                                   
    <div style="clear: both; width:1100px;">&nbsp;</div>
<div class="footer">© 2015 ICL. Test project</div>
</body>
</html>

