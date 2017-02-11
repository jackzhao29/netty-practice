package com.coldface.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 类TimeClient.java的实现描述：TODO 类实现描述
 * 
 * @author coldface
 * @date 2017年2月11日下午1:00:57
 */
public class TimeClient {

  /**
   * @author coldface
   * @date 2017年2月11日下午1:00:57
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    int port = 8080;
    new TimeClient().connect(port, "127.0.0.1");

  }

  public void connect(int port, String host) {
    // 配置客户端NIO线程组
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
              //LineBasedFrameDecoder和StringDecode解决TCP粘包
              ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
              ch.pipeline().addLast(new StringDecoder());
              ch.pipeline().addLast(new TimeClientHandler());
            }
          });
      //发起异步连接操作
      ChannelFuture f = b.connect(host, port).sync();
      //等待客户端链路关闭
      f.channel().closeFuture().sync();
    } catch (Exception ex) {

    }finally{
      //优雅退出，释放NIO线程组
      group.shutdownGracefully();
    }
  }

}
