# JSP

1. What is JSP？

jsp 的全称是java server pages。Java 的服务器页面

2. 作用

   代替Servlet 程序回传html 页面的数据

   Servlet 程序回传html 页面数据非常繁锁

   ```java
   public class PringHtml extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           // 通过响应的回传流回传html 页面数据
           resp.setContentType("text/html; charset=UTF-8");
           PrintWriter writer = resp.getWriter();
           writer.write("<!DOCTYPE html>\r\n");
           writer.write(" <html lang=\"en\">\r\n");
           writer.write(" <head>\r\n");
           writer.write(" <meta charset=\"UTF-8\">\r\n");
           writer.write(" <title>Title</title>\r\n");
           writer.write(" </head>\r\n");
           writer.write(" <body>\r\n");
           writer.write(" 这是html 页面数据\r\n");
           writer.write(" </body>\r\n");
           writer.write("</html>\r\n");
           writer.write("\r\n");
       }
   }
   ```

   ```jsp
   <%@ page contentType="text/html;charset=UTF-8" language="java" %>
   <html>
   <head>
   <title>Title</title>
   </head>
   <body>
   	这是 html 页面数据
   </body>
   </html>
   ```

3. jsp如何访问：

   jsp 页面和html 页面一样，都是存放在web 目录下。访问也跟访问html 页面一样

   在web 目录下有如下的文件：
   web 目录
   a.html 页面访问地址是--->>>>>> http://ip:port/工程路径/a.html
   b.jsp 页面访问地址是--->>>>>> http://ip:port/工程路径/b.jsp





































