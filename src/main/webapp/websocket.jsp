<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Java API for WebSocket (JSR-356)</title>
</head>
<body>
<script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
<script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
<script type="text/javascript">
    var websocket = null;
//这里之所以这么麻烦是因为ie7和ie8内核不止websocket，在不支持的情况下我们用sockjs来代替websocket
    if ('WebSocket' in window) {
        //Websocket的连接
        websocket = new WebSocket("ws://localhost:8080/ws/websocket/socketServer.do");//WebSocket对应的地址
    }
    else if ('MozWebSocket' in window) {
        //Websocket的连接
        websocket = new MozWebSocket("ws://localhost:8080/ws/websocket/socketServer.do");//SockJS对应的地址
    }
    else {
        //SockJS的连接
        websocket = new SockJS("http://localhost:8080/ws/sockjs/socketServer.do");    //SockJS对应的地址
    }

//    重写websocket的四个方法
    websocket.onopen = onOpen;
    websocket.onmessage = onMessage;
    websocket.onerror = onError;
    websocket.onclose = onClose;

    function onOpen(openEvt) {
//        console.log(openEvt.Data);
        console.log("建立连接");
    }

    function onMessage(evt) {
        alert(evt.data);
    }
    function onError() {
    }
    function onClose() {
        console.log("关闭连接。");
    }

    function doSend() {
        if (websocket.readyState == websocket.OPEN) {
            var msg = document.getElementById("inputMsg").value;
            websocket.send(msg);//调用后台handleTextMessage方法
            alert("发送成功!");
        } else {
            alert("连接失败!");
        }
    }
//    窗口关闭websocket就关闭
    window.close = function () {
        websocket.onclose();
    }
</script>
请输入：<textarea rows="3" cols="100" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSend();">发送</button>
</body>
</html>