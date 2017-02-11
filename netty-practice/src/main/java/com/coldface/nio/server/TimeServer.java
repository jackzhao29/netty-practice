package com.coldface.nio.server;

/**
 * 类TimeServer.java的实现描述：NIO时间服务器
 * @author coldface
 * @date 2017年2月11日上午10:57:12
 */
public class TimeServer {

  public static void main(String[] args){
    int port = 8080;
    if(args != null && args.length > 0){
      port = Integer.valueOf(args[0]);
    }
    MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
    new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
  }
}
