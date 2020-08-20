package com.ebupt.filefromudp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import io.netty.util.Constant;

public class FloatMonkService extends Service {



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (TextUtils.isEmpty(intent.getAction())){
            showNotification(0+"","日志采集中","点击结束采集");
        }else {
            FileUtil.getInstance().closeWrite();
            stopSelf();

        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void showNotification(){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(this,FloatMonkService.class);
        intent.setAction("FINISH");

        builder.setContentIntent(PendingIntent.getService(this,0,intent,0))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setContentTitle("日志采集中")
                .setContentText("点击结束采集")
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build();

        startForeground(0,notification);
        notificationManager.notify(0,notification);
        Log.e("ZZZZZZZZZZZZZ","启动前台弹窗");
    }

    public void showNotification(String targetId, String name, String content) {


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(targetId, getPackageName(), NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);

            mBuilder.setChannelId(targetId);
        }



        mBuilder.setPriority(Notification.PRIORITY_MAX);//可以让通知显示在最上面
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setAutoCancel(true);

        mBuilder.setDefaults(Notification.DEFAULT_ALL);//使用默认的声音、振动、闪光



        Bitmap bmIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        Bitmap largeIcon = bmIcon;
        content =  content;

        mBuilder.setLargeIcon(largeIcon);

        mBuilder.setContentTitle(name);

        Intent intent = new Intent(this,FloatMonkService.class);
        intent.setAction("FINISH");
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //通知首次出现在通知栏，带上升动画效果的
        // mBuilder.setTicker(content);
        mBuilder.setFullScreenIntent(null,true);
        mBuilder.setDefaults(~0);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        //内容
        mBuilder.setContentText(content);

         mBuilder.setContentIntent(pendingIntent);
        Notification notification = mBuilder.build();

        int notifyId = 0;
        try {
            notifyId = Integer.parseInt(targetId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            notifyId = -1;
        }

        startForeground(0,notification);
        //弹出通知栏
        notificationManager.notify(notifyId, notification);

    }
}
