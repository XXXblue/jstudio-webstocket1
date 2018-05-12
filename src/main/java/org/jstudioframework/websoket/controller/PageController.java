package org.jstudioframework.websoket.controller;

import org.jstudioframework.websoket.handler.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 测试类
 */

@Controller
public class PageController {

    @Autowired
    private WebSocketHandler webSocketHandler;

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        System.out.println(username + "登录");
        HttpSession session = request.getSession();
        session.setAttribute("SESSION_USERNAME", username);
        response.sendRedirect("websocket.jsp");
    }

    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request) {
        String username = request.getParameter("username");
        webSocketHandler.sendMessageToUser(username, new TextMessage("你好，欢迎测试！！！！"));
        return null;
    }

    @RequestMapping("/login.jsp")
    public ModelAndView loginJsp(){
        return new ModelAndView("login");
    }

}
