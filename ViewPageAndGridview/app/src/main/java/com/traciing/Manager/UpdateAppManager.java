package com.traciing.Manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.traciing.corecode.R;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Carl_yang on 2016/6/13.
 */
public class UpdateAppManager {

    private static final int UPDARE_TOKEN = 0x29;
    private static final int INSTALL_TOKEN = 0x31;
    private Context context;
    private String message = "检测到本程序有新版本发布，建议您更新！";
    private String url_apk=null;
    private Dialog dialog;
    private ProgressBar progressBar;
    private int curProgress;
    private boolean isCancel;
    private File tempFile = null;

    public UpdateAppManager(Context context) {
        this.context=context;
    }

    /**
     * 建立线程
     */
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDARE_TOKEN:
                    progressBar.setProgress(curProgress);
                    break;
                case INSTALL_TOKEN:
                    Instanll();
                    break;
            }
        }
     };

    /**
     * 检查更新状态
     * @param url
     */
    public void checkUpdateInfo(String url) {
        url_apk=url;
        showNoticeDialog();
    }

    /**
     * 创建提示框,提示是否需要更新
     */
    private void showNoticeDialog(){
        new AlertDialog.Builder(context)
                .setTitle("软件版本更新")
                .setMessage(message)
                .setPositiveButton("下载",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("----------"+dialog);
                        dialog.dismiss();
                        showDownloadDialog();
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    /**
    *   创建显示更新进度条对话框
     */
    private void showDownloadDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.progressbare, null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isCancel = true;
            }
        });
        dialog=builder.create();
        dialog.show();
        downloadApp();
    }

    /**
     * 下载新版本应用
    */
    private void downloadApp() {
        System.out.println("过来的地址:"+url_apk);
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection conn = null;
                try {
                    url = new URL(url_apk);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    long fileLength = conn.getContentLength();
                    in = conn.getInputStream();
                    File filePath = new File("/sdcard",
                            "/app");
                    if (!filePath.exists()) {
                        filePath.mkdir();
                    }
                    tempFile = new File(
                            "/sdcard",
                            "/app/"
                                    + url_apk.substring(url_apk.lastIndexOf("/") + 1)
                    );
                    if (tempFile.exists())
                        tempFile.delete();
                    tempFile.createNewFile();
                    out = new FileOutputStream(tempFile);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if (isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDARE_TOKEN);
                        if (readedLength >= fileLength) {
                            dialog.dismiss();
                            // 下载完毕，通知安装
                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 安装下载后的apk文件
     */
    private void Instanll() {
        String aa=tempFile.toString();
        DataOutputStream dos=null;
        try {
            Runtime.getRuntime().exec("chmod 644 " + aa);//添加读写权限
        } catch (Exception e) {
            System.out.println("");
            e.printStackTrace();
        }finally {
            if(dos!=null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + tempFile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
