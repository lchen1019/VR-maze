package me.pqpo.smartcropper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import me.pqpo.smartcropper.utils.Address;
import me.pqpo.smartcropper.utils.Client;

public class MainActivity extends AppCompatActivity {

    private Button btnTake;
    private Button btnSelect;
    private Button uploadBtn;
    private Button selfDrawBtn;
    private ImageView ivShow;
    private File photoFile;
    private ImageView setting;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        btnTake = findViewById(R.id.btn_take);
        btnSelect = findViewById(R.id.btn_select);
        ivShow = findViewById(R.id.iv_show);
        uploadBtn = findViewById(R.id.upload);
        selfDrawBtn = findViewById(R.id.self_draw);
        setting = findViewById(R.id.set);
        photoFile = new File(getExternalFilesDir("img"), "scan.jpg");

        bitmap = null;
        addListener();
    }

    // 监听事件
    public void addListener() {
        // 打开相机
        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CropActivity.getJumpIntent(MainActivity.this, false, photoFile), 100);
            }
        });

        // 打开相册
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(CropActivity.getJumpIntent(MainActivity.this, true, photoFile), 100);
            }
        });

        // 进行端口和IP设置
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingActivity.class));
            }
        });

        // 上传
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try  {
                            String response = new Client().sendImage(bitmap);
                            System.out.println("已经返回");
                            System.out.println("已经返回");
                            System.out.println("已经返回");
                            System.out.println("已经返回");
                            System.out.println("已经返回");
                            System.out.println("已经返回");
                            Intent intent = new Intent(getApplicationContext(), MazeActivity.class);
                            intent.putExtra("message", response);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                if (bitmap == null) {
                    Toast.makeText(MainActivity.this, "请先上传图片", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "解析中", Toast.LENGTH_SHORT).show();
                    thread.start();
                }
            }
        });

        // 自己绘制的图片
        selfDrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelfDrawActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 100 && photoFile.exists()) {
            bitmap = BitmapFactory.decodeFile(photoFile.getPath());
            ivShow.setImageBitmap(bitmap);
        }
    }
}
