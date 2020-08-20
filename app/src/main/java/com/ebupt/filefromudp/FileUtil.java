package com.ebupt.filefromudp;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 写入文件
 */
public class FileUtil {

    private final String ROOT_PATH =  Environment.getExternalStorageDirectory().getPath()+"/";
    private BufferedWriter bufferedWriter;
    private String currentPath;

    private static FileUtil fileUtil;

    private FileUtil() {
    }

    public static FileUtil getInstance(){

        synchronized (FileUtil.class){
            if (fileUtil==null){
                fileUtil = new FileUtil();
            }

            return fileUtil;
        }
    }

    /**
     * 写入文件
     * @param msg
     */
    public synchronized void writeToTxt(String msg){
        try {
            if (bufferedWriter==null){
                String currentDateTimeString = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.CHINA).format(new Date());
                File file = new File(ROOT_PATH+currentDateTimeString+".txt");

                currentPath = file.getPath();
                if (file.exists()){
                    file.createNewFile();
                }
                FileOutputStream outputStream = new FileOutputStream(file);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            }

            bufferedWriter.write(msg);
            bufferedWriter.flush();

        }catch (Exception e){
            e.printStackTrace();

        }


    }

    /**
     * 关闭 文件形成
     */
    public void closeWrite(){
        if (bufferedWriter!=null){
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
