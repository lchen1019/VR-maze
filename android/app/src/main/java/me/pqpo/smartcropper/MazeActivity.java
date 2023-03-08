package me.pqpo.smartcropper;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import me.pqpo.smartcropper.utils.Client;


public class MazeActivity  extends AppCompatActivity {

    private ImageView back;
    private Button check;
    private ImageView view;
    private Canvas canvas;
    private Bitmap bitmap;
    private Paint paint;
    private Paint bgPaint;
    private int[][][] centerRow;
    private int[][][] centerCol;
    private boolean[][] row;
    private boolean[][] col;

    private int width;
    private int height;
    private int widthPad;
    private int heightPad;
    private int d;
    private boolean who;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // 获得组件
        back = findViewById(R.id.back);
        check = findViewById(R.id.check);
        view = findViewById(R.id.iv_canvas);

        // 初始化画布
        ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 初始化绘图相关
                if (bitmap == null) {
                    // 初始化绘图宽高
                    int canvasWidth = view.getWidth();
                    int canvasHeight = view.getHeight();
                    if (canvasHeight > canvasWidth) {
                        d = (canvasHeight - canvasWidth) / 2;
                        who = true;
                    } else {
                        d = (canvasWidth - canvasHeight) / 2;
                        who = false;
                    }

                    canvasWidth = Math.min(canvasHeight, canvasWidth);
                    canvasHeight = canvasWidth;


                    widthPad = (int) (canvasWidth * 0.1);
                    heightPad = (int) (canvasHeight * 0.1);
                    width = ((canvasWidth - 2 * widthPad) / 4);
                    height = ((canvasHeight - 2 * heightPad) / 4);

                    // 初始化画笔
                    paint = new Paint();
                    paint.setStrokeWidth(12);
                    paint.setColor(Color.BLACK);

                    // 初始化背景画笔
                    bgPaint = new Paint();
                    bgPaint.setStrokeWidth(8);
                    bgPaint.setColor(Color.argb(100,42,194,209));
                }
                initCenter();
                paint();
            }
        });

        // 初始化矩阵
        initMaze();

        // 设置监听
        addListener();
    }

    private void initMaze() {
        row = new boolean[5][4];
        col = new boolean[4][5];

        centerRow = new int[5][4][2];
        centerCol = new int[4][5][2];

        String response = getIntent().getStringExtra("message");
        System.out.println(response);
        String[] ans = response.split(";");
        System.out.println(ans.toString());

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (ans[0].charAt(i * 4 + j) == '1') {
                    row[i][j] = true;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (ans[1].charAt(i * 5 + j) == '1') {
                    col[i][j] = true;
                }
            }
        }
    }

    private void initCenter() {
        // 绘制每一行
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row[i].length; j++) {
                int startX = j * width + widthPad;
                int stopX = (j + 1) * width + widthPad;
                int startY = i * height + heightPad;
                int stopY = i * height + heightPad;
                centerRow[i][j][0] = (startX + stopX) / 2;
                centerRow[i][j][1] = (startY + stopY) / 2;
                if (who) {
                    centerRow[i][j][1] += d;
                } else {
                    centerRow[i][j][0] += d;
                }
            }
        }

        // 绘制每一列
        for (int i = 0; i < col.length; i++) {
            for (int j = 0; j < col[i].length; j++) {
                int startX = j * width + widthPad;
                int stopX = j * width + widthPad;
                int startY = i * height + heightPad;
                int stopY = (i + 1) * height + heightPad;
                centerCol[i][j][0] = (startX + stopX) / 2;
                centerCol[i][j][1] = (startY + stopY) / 2;
                if (who) {
                    centerCol[i][j][1] += d;
                } else {
                    centerCol[i][j][0] += d;
                }
            }
        }
    }

    private void paint() {
        // 初始化画布
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getWidth(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        // 绘制每一行
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row[i].length; j++) {
                int startX = j * width + widthPad;
                int stopX = (j + 1) * width + widthPad;
                int startY = i * height + heightPad;
                int stopY = i * height + heightPad;
                if (row[i][j]) {
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                } else {
                    canvas.drawLine(startX, startY, stopX, stopY, bgPaint);
                }
            }
        }

        // 绘制每一列
        for (int i = 0; i < col.length; i++) {
            for (int j = 0; j < col[i].length; j++) {
                int startX = j * width + widthPad;
                int stopX = j * width + widthPad;
                int startY = i * height + heightPad;
                int stopY = (i + 1) * height + heightPad;
                if (col[i][j]) {
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                } else {
                    canvas.drawLine(startX, startY, stopX, stopY, bgPaint);
                }
            }
        }

        // 显示bitmap
        view.setImageBitmap(bitmap);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addListener() {
        // 监听返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 确认按钮
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try  {
                            String response = new Client().sendMaze(row, col);
                            Looper.prepare();
                            if (response.equals("suc")) {
                                Toast.makeText(MazeActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MazeActivity.this, "unity端失败", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();


            }
        });
        // 画布点击事件
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int[] inx = getNear(motionEvent.getX(), motionEvent.getY());
                    if (inx[0] != -1 && inx[1] != -1) {
                        if (inx[2] == 2) {
                            col[inx[0]][inx[1]] = !col[inx[0]][inx[1]];
                        } else if (inx[2] == 1){
                            row[inx[0]][inx[1]] = !row[inx[0]][inx[1]];
                        }
                    }
                    paint();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    paint();
                }
                return true;
            }
        });
    }

    private int[] getNear(double x, double y) {
        int[] inx = new int[3];
        for (int i = 0; i < row.length; i++) {
            for (int j = 0; j < row[i].length; j++) {
                if ((Math.abs(centerRow[i][j][0] - x) < (width / 4.0)) && (Math.abs(centerRow[i][j][1] - y) < (height / 4.0))) {
                    inx[2] = 1;
                    inx[0] = i;
                    inx[1] = j;
                    return inx;
                }
            }
        }
        for (int i = 0; i < col.length; i++) {
            for (int j = 0; j < col[i].length; j++) {
                if ((Math.abs(centerCol[i][j][0] - x) < (width / 4.0)) && (Math.abs(centerCol[i][j][1] - y) < (height / 4.0))) {
                    inx[2] = 2;
                    inx[0] = i;
                    inx[1] = j;
                    return inx;
                }
            }
        }
        return inx;
    }

}
