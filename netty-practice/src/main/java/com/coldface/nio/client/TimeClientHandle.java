package com.coldface.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 类TimeClientHandle.java的实现描述：处理异步连接和读写操作
 * @author coldface
 * @date 2017年2月11日上午11:37:37
 */
public class TimeClientHandle implements Runnable {

  private String host;
  private int port;
  private Selector selector;
  private SocketChannel socketChannel;
  private volatile boolean stop;
  
  public TimeClientHandle(String host, int port){
    this.host = host == null ? "127.0.0.1" : host;
    this.port = port;
    try{
      selector = Selector.open();
      socketChannel = SocketChannel.open();
      socketChannel .configureBlocking(false);
    }catch(Exception ex){
      ex.printStackTrace();
      System.exit(1);
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    // TODO Auto-generated method stub
    try{
      doConnect();
    }catch(Exception ex){
      ex.printStackTrace();
      System.exit(1);
    }
    
    while(!stop){
      try{
        selector.select(1000);
        Set<SelectionKey> selectedKeys = selector.selectedKeys();
        Iterator<SelectionKey> it = selectedKeys.iterator();
        SelectionKey key = null;
        while(it.hasNext()){
          key = it.next();
          it.remove();
          try{
            handleInput(key);
          }catch(Exception ex){
            if(key != null){
              key.cancel();
              if(key.channel() != null){
                key.channel().close();
              }
            }
          }
        }
      }catch(Exception ex){
        ex.printStackTrace();
        System.exit(1);
      }
    }
    
    //多路复用器关闭后，所有注册在上面的channel和pipe等资源都会被自动取注册并关闭，所以不需要重复释放资源

  }
  
  private void handleInput(SelectionKey key) throws IOException{
    if(key.isValid()){
      //判断是否连接成功
      SocketChannel sc = (SocketChannel) key.channel();
      if(key.isConnectable()){
        if(sc.finishConnect()){
          sc.register(selector, SelectionKey.OP_READ);
          doWrite(sc);
        }
      }else{
        //连接失败，进线退出
        System.exit(1);
      }
    }
  }
  
  private void doConnect() throws ClosedChannelException, IOException{
    //如果直接连接成功，则注册到多路服务器上，发送请求消息，读应答
    if(socketChannel.connect(new InetSocketAddress(host, port))){
      socketChannel.register(selector, SelectionKey.OP_READ);
      doWrite(socketChannel);
    }else{
      socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }
  }
  
  private void doWrite(SocketChannel sc) throws IOException{
    byte[] req = "QUERY TIME ORDER".getBytes();
    ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
    writeBuffer.put(req);
    writeBuffer.flip();
    sc.write(writeBuffer);
    if(!writeBuffer.hasRemaining()){
      System.out.println("Send order 2 server succeed.");
    }
  }

}
