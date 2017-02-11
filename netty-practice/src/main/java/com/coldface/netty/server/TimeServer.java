package com.coldface.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 类TimeServer.java的实现描述：TODO 类实现描述
 * 
 * @author coldface
 * @date 2017年2月11日下午12:28:13
 */
public class TimeServer {

  public static void main(String[] args) {
    int port = 8080;
    if (args != null && args.length > 0) {
      try {
        port = Integer.valueOf(args[0]);
      } catch (Exception ex) {
        // 采用默认值
      }
    }
    new TimeServer().bind(port);
  }

  public void bind(int port) {
    // 配置服务端的NIO线程组
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
          .option(ChannelOption.SO_BACKLOG, 1024).childHandler(new ChildChannelHandler());
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
      //LineBasedFrameDecoder和StringDecode解决TCP粘包
      arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
      arg0.pipeline().addLast(new StringDecoder());
      arg0.pipeline().addLast(new TimeServerHandler());
    }
  }
}
