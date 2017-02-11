package com.coldface.netty.delimiterbasedemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 类EchoServer.java的实现描述：EchoServer
 * @author coldface
 * @date 2017年2月11日下午2:59:40
 */
public class EchoServer {
  
  public static void main(String[] args){
    int port = 8080;
    new EchoServer().bind(port);
  }
  
  public void bind(int port){
    //配置服务端NIO线程组
 // 配置服务端的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(new ChildChannelHandler());
      // 绑定端口，同步等待成功
      ChannelFuture f = b.bind(port).sync();
      // 等待服务器监听端口关闭
      f.channel().closeFuture().sync();
    } catch (Exception ex) {

    } finally {
      // 优雅的退出，释放线程池资源
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }
  
  private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel arg0) {
      ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
      //以$_为分隔符
      arg0.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
      arg0.pipeline().addLast(new StringDecoder());
      arg0.pipeline().addLast(new EchoServerHandler());
    }
  }

}
