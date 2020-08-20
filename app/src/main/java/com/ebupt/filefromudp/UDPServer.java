package com.ebupt.filefromudp;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.support.annotation.MainThread;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class UDPServer {

    private Context context;

    private DatagramSocket socket;

    private WifiManager.MulticastLock mLock;

    private volatile boolean isClose;


    public UDPServer(int port,int timeout,Context context) {

        try {
            socket = new DatagramSocket(null);
            this.context = context;
            socket.setSoTimeout(timeout);
            socket.setBroadcast(true);
            socket.bind(new InetSocketAddress(port));
            this.isClose = false;

            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            mLock = wifiManager.createMulticastLock("test_wifi");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }


    public synchronized void acquireLock(){
        if (mLock!=null&&mLock.isHeld()){
            mLock.acquire();
        }
    }


    public synchronized void releaseLock(){
        if (mLock!=null&&mLock.isHeld()){
            try {
                mLock.release();
            }catch (Exception e){

            }

        }
    }


    //接受消息 会阻塞线程 要在子线程调用
    public byte[] receiverMsg(){
        if (Thread.currentThread().getId()==Looper.getMainLooper().getThread().getId()){
            throw new RuntimeException("请在子线程调用");
        }

        try {
            acquireLock();
            byte[] bytes = new byte[1024];
            DatagramPacket packet = new DatagramPacket(bytes,bytes.length);
            socket.receive(packet);
            return packet.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    public synchronized void closeServer(){
        if (isClose){
            return;
        }
        socket.close();
        releaseLock();
        isClose = true;
    }

    @Override
    protected void finalize() throws Throwable {
        closeServer();
        super.finalize();
    }
}
