package com.coldface.netty.client;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 类TimeClientHandler.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date 2017年2月11日下午12:54:12
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

  //private final ByteBuf firstMessage;
  private int counter;
  private byte[] req;
  /**
   * 
   */
  public TimeClientHandler() {
    // TODO Auto-generated constructor stub
    req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    //firstMessage = Unpooled.buffer(req.length);
    //firstMessage.writeBytes(req);
  }
  
  @Override
  public void channelActive(ChannelHandlerContext ctx){
    ByteBuf message = null;
    for(int i = 0; i < 100; i++){
      message = Unpooled.buffer(req.length);
      message.writeBytes(req);
      ctx.writeAndFlush(message);
    }
    //ctx.writeAndFlush(firstMessage);
  }
  
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
    String body = (String) msg;
    System.out.println("client receive : "+body+" ; the counter is :"+ ++counter);
    /**
     * ByteBuf buf = (ByteBuf) msg;
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    String body = new String(req, "UTF-8");
    System.out.println("client receive : "+body);
    **/
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
    //释放资源
    System.out.println(cause.getMessage());
    ctx.close();
  }

}
