package ru.icl.test.servlets;

import javax.servlet.ServletException;
import ru.icl.test.beans.Messages;
import ru.icl.test.db.Database;
import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.apache.commons.logging.Log;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


@WebServlet(name = "WriteMsg", urlPatterns = {"/WriteMsg"})
public class WriteMsg extends HttpServlet {
    
    public static final String SESSION_MAP = "sessionMap";
    
    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                                                    
        //вводится коллекция для хранения сообщений сторонних клиентов
        HashMap<String, List> sessionMap = (HashMap<String,List>)request.getServletContext().getAttribute(SESSION_MAP);                
        if (sessionMap==null) {
            sessionMap = new HashMap<String, List>();
        }       
        
        response.setContentType("text/html; charset=UTF-8"); 
                                
        PrintWriter out = response.getWriter();                 
        out.println("<html>");
        out.println("<head>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        out.println("<title>Онлайн ЧАТ</title>");
        out.println("<link href=\"css/style_main.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("</head>");
        out.println("<body>");
        out.println("<div class=\"container\">");
        out.println("<div class=\"logo\">");
        out.println("<a href=\"main.jsp\"><img src=\"Images/chat_.png\" width=\"86\" height=\"87\" alt=\"\" name=\"logo\" /></a>");
        out.println("</div>");
        out.println("<div class=\"descr\">");
        out.println("<h3>Онлайн ЧАТ</h3>");
        out.println("</div>");
        out.println("<div class=\"welcome\">"); 
        
        try {
             //считывание параметров                                    
            String username = request.getParameter("username");
            String msg = request.getParameter("msg");

            //определение или создание сессии
            HttpSession session = request.getSession(true);               

            //if (request.getParameter("username") != null) {
            //    session.setAttribute("username", request.getParameter("username"));            
            //}

            out.println("<h5>Добро пожаловать, " + username + "!</h5>");    
            out.println("<h5 class=\"sessia\">Ваш ClientId: "  + session.getId() + "</h5>");
            out.println("<h6>");
            out.println("<a href=\"pages/showmsg.jsp\">Все сообщения</a>");
            out.println("&nbsp;");
            out.println("<a href=\"index.jsp\">Выход</a>");
            out.println("</h6>");
            out.println("</div>");
            out.println("<div class=\"search_form\">");
            out.println("<form name=\"search_form\" method=\"GET\" action=\"" + request.getContextPath() + "/WriteMsg\">");
            out.println("<input type=\"text\" name=\"msg\" size=\"135\"/>");
            out.println("<input class=\"search_button\" type=\"submit\" value=\"Отправить\"/>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class=\"big_column\">");
            out.println("<div>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>id</th>");
            out.println("<th>ClientId</th>");
            out.println("<th>Message</th>");
            out.println("</tr>");
                
            //вводится коллекция для сообщений текущего клиента
            ArrayList<String> listMessages;

            // для новой сессии создаем новый список
            if (session.isNew()) {                
                listMessages = new ArrayList<String>();
            } else { // иначе получаем список из атрибутов сессии
                listMessages = (ArrayList<String>) session.getAttribute("messages");
            }
            
            //если параметр msg не пустой
            if (!msg.isEmpty()) {
                listMessages.add(msg);            
                session.setAttribute("messages", listMessages);
            }            
                                  
            //JSONObject jsonObject = new JSONObject();
            //jsonObject.put("msg", listMessages);                
            
            int id= 1;    
            // вывод сообщений текущего клиента       
            for (String mess : listMessages) {
                out.println("<tr>");
                out.println("<td>" + (id++) + "</td>");
                out.println("<td>" + session.getId() + "</td>");
                out.println("<td>" + mess + "</td>");
                out.println("</tr>");
            }

            //запись параметров в БД 
            Messages m = new Messages();            
            m.setClientid(session.getId());
            m.setFname(username);
            m.setMessage(msg);

            Connection conn = null;
            PreparedStatement pstmt1 = null;
            PreparedStatement pstmt2 = null;
            String sql1 = "insert into users (clientid, fname) values (?, ?)";
            String sql2 = "insert into messages (clientid, message) values (?, ?)";

            try {
                conn = Database.getConnection();                       
                if (session.isNew()) { 
                    pstmt1 = conn.prepareStatement(sql1);
                    pstmt1.setString(1, m.getClientid());
                    pstmt1.setString(2, m.getFname());
                    pstmt1.executeUpdate();
                }

                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setString(1, m.getClientid());
                pstmt2.setString(2, m.getMessage());
                pstmt2.executeUpdate();                                            

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {                
                try {                    
                    if (pstmt1!=null) pstmt1.close();
                    if (pstmt2!=null) pstmt2.close();
                    if (conn!=null)conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }                
        /*        
        RequestDispatcher dispatcher = request.getRequestDispatcher("pages/main.jsp");
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        }
        */
        
        //вывод сообщений других клиентов
        sessionMap.put(session.getId(), listMessages);
        getServletContext().setAttribute(SESSION_MAP, sessionMap);
        
        // вывод сообщений других клиентов       
        for (Map.Entry<String, List> entry : sessionMap.entrySet()) {
            String sessionId = entry.getKey();
            List listMsg = entry.getValue();
            
            //сообщения текущего пользователя пропускаются 
            if (sessionId.equals(session.getId())) continue;
                       
            for (Object str : listMsg) {
                 out.println("<tr>");
                out.println("<td>" + (id++) + "</td>");
                out.println("<td style=\"color:red\">" + sessionId + "</td>");
                out.println("<td>" + str + "</td>");
                out.println("</tr>");
            }
        }
       
        out.println("</table>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");        
        out.println("<div class=\"footer\">© 2015 ICL. Test project</div>");

        } catch (Exception ex) {           
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);         
        } finally {
            out.println("</body>");
            out.println("</html>");
            out.close();
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
 
    