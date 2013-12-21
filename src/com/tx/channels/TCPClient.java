package com.tx.channels;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TCPClient{
  // �ŵ�ѡ����
  private Selector selector;
  
  // �������ͨ�ŵ��ŵ�
  SocketChannel socketChannel;
  
  // Ҫ���ӵķ�����Ip��ַ
  private String hostIp;
  
  // Ҫ���ӵ�Զ�̷������ڼ����Ķ˿�
  private int hostListenningPort;
  
  /**
   * ���캯��
   * @param HostIp
   * @param HostListenningPort
   * @throws IOException
   */
  public TCPClient(String HostIp,int HostListenningPort)throws IOException{
    this.hostIp=HostIp;
    this.hostListenningPort=HostListenningPort;   
    
    initialize();
  }
  
  /**
   * ��ʼ��
   * @throws IOException
   */
  private void initialize() throws IOException{
    // �򿪼����ŵ�������Ϊ������ģʽ
    socketChannel=SocketChannel.open();
    Socket sock = socketChannel.socket();
    sock.setSoLinger(true, 1);
    sock.setKeepAlive(false);
    sock.setTcpNoDelay(true);
    boolean connected = socketChannel.connect(new InetSocketAddress(hostIp, hostListenningPort));
    System.out.println("connected:"+connected);
    socketChannel.configureBlocking(false);
    
    // �򿪲�ע��ѡ�������ŵ�
    selector = Selector.open();
    socketChannel.register(selector, SelectionKey.OP_READ);
    
    // ������ȡ�߳�
    new TCPClientReadThread(selector);
  }
  
  /**
   * �����ַ�����������
   * @param message
   * @throws IOException
   */
  public void sendMsg(String message) throws IOException{
    ByteBuffer writeBuffer=ByteBuffer.wrap(message.getBytes("UTF-16"));
    socketChannel.write(writeBuffer);
  }
  
  public static void main(String[] args) throws IOException{
    TCPClient client=new TCPClient("127.0.0.1",1978);
    
    client.sendMsg("���!Nio!�������ƿ���,�λش�����Ӫ");
  }
}