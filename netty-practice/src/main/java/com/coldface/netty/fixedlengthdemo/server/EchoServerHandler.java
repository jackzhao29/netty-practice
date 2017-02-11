package com.coldface.netty.fixedlengthdemo.server;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 类EchoServerHandler.java的实现描述：EchoServerHandler的实现
 * @author coldface
 * @date 2017年2月11日下午3:00:30
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
   int counter = 0;
   
   @Override
   public void channelRead(ChannelHandlerContext ctx, Object msg){
     System.out.println("Receive client : [" + msg + "]");
   }
   
   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
     cause.printStackTrace();
     //发生异常，关闭链路
     ctx.close();
   }
}
