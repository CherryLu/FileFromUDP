package com.ebupt.filefromudp;

import android.util.Log;



import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;

public class NettyManager implements NettyListener {

    private static NettyManager nettyManager;

    private NettyClient nettyClient;

    private NettyManager() {
    }


    public static NettyManager getInstance(){
        synchronized (NettyManager.class){

            if (nettyManager==null){
                nettyManager = new NettyManager();
            }

            return nettyManager;
        }
    }

    /**
     * 发送消息
     */
    public void setMessage(String data, ChannelFutureListener listener){
        if (nettyClient!=null){
            nettyClient.sendMsgToServer(data,listener);
        }

    }


    /**
     * 初始化传入IP和Port
     * @param host
     * @param tcp_port
     */
    public void initNettyClient(String host, int tcp_port){
        if (nettyClient==null){
            nettyClient = new NettyClient(host,tcp_port);
        }
        nettyClient.setListener(this);
        nettyClient.connect();

    }

    /**
     * 关闭文件
     */
    public void closeConnection(){
        if (nettyClient!=null&&nettyClient.isConnecting()){
            nettyClient.disconnect();
            FileUtil.getInstance().closeWrite();
        }
    }

    @Override
    public void onMessageResponse(Object msg) {
        ByteBuf result = (ByteBuf) msg;
        byte[] result1 = new byte[result.readableBytes()];
        result.readBytes(result1);
        result.release();
        String ss = new String(result1);
        Log.e("Netty","收到信息 : "+ss);

        //写入文件

        FileUtil.getInstance().writeToTxt(ss);


    }

    @Override
    public void onServiceStatusConnectChanged(int statusCode) {
        switch (statusCode){
            case STATUS_CONNECT_SUCCESS://连接成功

                break;
           case STATUS_CONNECT_CLOSED://连接关闭

                    break;


        }

    }
}
