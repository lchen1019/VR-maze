package me.pqpo.smartcropper.utils;

import static android.util.Base64.encodeToString;

import static java.text.DateFormat.DEFAULT;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class Client {

    /**
     *  用于发送图片到服务器段，进行处理
     */
    public String sendImage(Bitmap bitmap) {
        String info = null;

        try {
            Socket socket = new Socket(Address.ip, Address.port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            String msg = "0";

            // 将图片转化成base64编码，用于发送
            String base64 = bitmapToBase64(bitmap);
            msg += base64;
            out.write(msg.getBytes(Charset.defaultCharset()));
            out.flush();
            socket.shutdownOutput();

            // 读取返回结果
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br= new BufferedReader(isr);
            String temp = null;
            while((temp = br.readLine())!=null){
                System.out.println("在此循环");
                info = temp;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     *  用于将迷宫发送给客户端
     */
    public String sendMaze(boolean row[][], boolean col[][]) {
        String info = null;

        try {
            Socket socket = new Socket(Address.ip, Address.port);
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();
            StringBuilder msg = new StringBuilder("1");

            // 将迷宫转化为字符串
            for(int i = 0; i < row.length; i++) {
                for (int j = 0; j < row[i].length; j++) {
                    msg.append(row[i][j] ? '1' : '0');
                }
            }
            for(int i = 0; i < col.length; i++) {
                for (int j = 0; j < col[i].length; j++) {
                    msg.append(col[i][j] ? '1' : '0');
                }
            }
            out.write(msg.toString().getBytes(Charset.defaultCharset()));
            out.flush();
            socket.shutdownOutput();

            // 读取返回结果
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br= new BufferedReader(isr);
            String temp = null;
            while((temp = br.readLine())!=null){
                info = temp;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static String bitmapToBase64(Bitmap bitmapOrigin) {
        double ratio = (double) bitmapOrigin.getHeight() / 500;

        Bitmap bitmap = Bitmap.createBitmap((int) (bitmapOrigin.getWidth() / ratio),
                (int) (bitmapOrigin.getHeight() / ratio), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect(0, 0, (int) (bitmapOrigin.getWidth() / ratio),
                (int) (bitmapOrigin.getHeight() / ratio));
        canvas.drawBitmap(bitmapOrigin, null, rect, null);

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                //压缩图片至100kb
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                //接收压缩的图片数据流，并转换成base64编码
                byte[] bitmapBytes = baos.toByteArray();
                result = encodeToString(bitmapBytes, DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
