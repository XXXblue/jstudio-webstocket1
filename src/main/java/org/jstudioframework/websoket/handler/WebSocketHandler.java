package org.jstudioframework.websoket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Websocket处理器
 */

public class WebSocketHandler extends TextWebSocketHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(WebSocketHandler.class);

    //已建立连接的用户 存静态变量感觉不太合适
    private static Map<String, WebSocketSession> SESSION_MAP = new ConcurrentHashMap<String, WebSocketSession>();
    /**
     * 处理前端发送的文本信息
     * js调用websocket.send时候，会调用该方法
     *
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        // 获取提交过来的消息详情
        LOGGER.debug("收到用户 " + username + "的消息:" + message.toString());
        //给所有用户发送消息
        //sendMessageToUsers(new TextMessage("reply msg:" + message.getPayload()));
        //回复一条信息，
        session.sendMessage(new TextMessage("reply msg:" + message.getPayload()));
    }


    /**
     * 当新连接建立的时候，被调用
     * 连接成功时候，会触发页面上onOpen方法
     *
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        SESSION_MAP.put((String) session.getAttributes().get("WEBSOCKET_USERNAME"),session);
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        LOGGER.info("用户 " + username + " Connection Established");
        session.sendMessage(new TextMessage(username + " connect"));
        session.sendMessage(new TextMessage("hello wellcome"));
    }

    /**
     * 当连接关闭时被调用
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        LOGGER.info("用户 " + username + " Connection closed. Status: " + status);
        SESSION_MAP.remove((String) session.getAttributes().get("WEBSOCKET_USERNAME"));
    }

    /**
     * 传输错误时调用
     *
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String username = (String) session.getAttributes().get("WEBSOCKET_USERNAME");
        if (session.isOpen()) {
            session.close();
        }
        LOGGER.debug("用户: " + username + " websocket connection closed......");
        SESSION_MAP.remove((String) session.getAttributes().get("WEBSOCKET_USERNAME"));
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for(Map.Entry<String,WebSocketSession> entry: SESSION_MAP.entrySet()){
            try {
                WebSocketSession tempSession = entry.getValue();
                if (tempSession.isOpen()) {
                    tempSession.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {
        if(SESSION_MAP.containsKey(userName)){
            try {
                if (SESSION_MAP.get(userName).isOpen()) {
                    SESSION_MAP.get(userName).sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
