package com.coldface.netty.delimiterbasedemo.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 类EchoClientHandler.java的实现描述：TODO 类实现描述
 * @author zhaofei
 * @date 2017年2月11日下午3:15:46
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

  private int counter;
  static final String ECHO_REQ = "Hi, Coldface. Welcome to Netty.$_";

  /**
   * 
   */
  public EchoClientHandler() {
    
  }
  
  @Override
  public void channelActive(ChannelHandlerContext ctx){
    for(int i = 0; i < 10; i++){
      Object message = Unpooled.copiedBuffer(ECHO_REQ.getBytes());
      ctx.writeAndFlush(message);
    }
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
    System.out.println("the counter is :"+ ++counter +" tiems receive server:[" + msg + "]");
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx){
    ctx.flush();
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
    //释放资源
    System.out.println(cause.getMessage());
    ctx.close();
  }

}
