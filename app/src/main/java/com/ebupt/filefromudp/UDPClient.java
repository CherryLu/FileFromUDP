package com.ebupt.filefromudp;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient{

    private DatagramSocket datagramSocket;

    private volatile boolean isStop;

    private volatile  boolean isClose;


    public UDPClient() {
        try {
            datagramSocket = new DatagramSocket();
            isStop = false;
            isClose = false;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打断
     */
    public void interrupt() {
        this.isStop = true;
    }

    /**
     * 发送消息
     */
    public void sendMsg(String msg,String targetHostName, int targetPort,long interval){

        try {
            byte[] bytes = msg.getBytes();
            InetAddress targetInetAddress = InetAddress.getByName(targetHostName);
            DatagramPacket localDatagramPacket = new DatagramPacket(bytes, bytes.length, targetInetAddress, targetPort);
            datagramSocket.send(localDatagramPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
