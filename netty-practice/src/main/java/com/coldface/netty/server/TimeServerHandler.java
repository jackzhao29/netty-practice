package com.coldface.netty.server;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 类TimeServerHandler.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date 2017年2月11日下午12:29:09
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

  private int counter;
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException{
    /**
    ByteBuf buf = (ByteBuf)msg;
    byte[] req = new byte[buf.readableBytes()];
    buf.readBytes(req);
    String body = new String(req, "UTF-8");
    **/
    String body = (String)msg;
    System.out.println("The time server receive message:"+body+"; the counter is:"+ ++counter);
    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? "Server:"+new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
    currentTime = currentTime + System.getProperty("line.separator");
    ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
    //ctx.write(resp);
    ctx.writeAndFlush(resp);
  }
  
  @Override
  public void channelReadComplete(ChannelHandlerContext ctx){
    ctx.flush();
  }
  
  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
    ctx.close();
  }
}
