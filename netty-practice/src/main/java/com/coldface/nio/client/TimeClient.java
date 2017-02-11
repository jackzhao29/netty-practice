package com.coldface.nio.client;

/**
 * 类TimeClient.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date 2017年2月11日上午11:37:06
 */
public class TimeClient {
 
  /**
   * @author coldface
   * @date 2017年2月11日上午11:37:06
   * @param args
   */
  public static void main(String[] args) {
    int port = 8080;
    // TODO Auto-generated method stub
    if(args != null && args.length > 0){
      try{
        port = Integer.valueOf(args[0]);
      }catch(Exception ex){
        //采用默认值
      }
    }
    
    new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();

  }

}
