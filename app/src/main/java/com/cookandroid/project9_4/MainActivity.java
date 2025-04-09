package com.cookandroid.project9_4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;



import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static int LINE = 1, CIRCLE = 2, RECTANGLE = 3;
    static int curShape = LINE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(new MyGraphicView(this));
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        setTitle("간단 그림판");
    }

    public static abstract class Shape {
        int color;

        public Shape(int color) {
            this.color = color;
        }

        abstract void draw(Canvas canvas, Paint paint);
    }

    public static class Line extends Shape {
        int startX, startY, stopX, stopY;

        public Line(int startX, int startY, int stopX, int stopY, int color){
            super(color);
            this.startX = startX;
            this.startY = startY;
            this.stopX = stopX;
            this.stopY = stopY;
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            paint.setColor(this.color);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }

    public static class Circle extends Shape {
        int centerX, centerY, radius;

        public Circle(int centerX, int centerY, int radius, int color){
            super(color);
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            paint.setColor(this.color);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(centerX, centerY, radius, paint);
        }
    }

    public static class Rectangle extends Shape {
        int left, top, right, bottom;

        public Rectangle(int left, int top, int right, int bottom, int color){
            super(color);
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        @Override
        void draw(Canvas canvas, Paint paint) {
            paint.setColor(this.color);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(left, top, right, bottom, paint);
    }


    public static class MyGraphicView extends View {
        int currentStartX = -1, currentStartY = -1, currentStopX = -1, currentStopY = -1;
        boolean isDrawing = false;

        Paint paint;
        List<Shape> shapes = new ArrayList<>();

        public MyGraphicView(Context context) {
            super(context);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isDrawing = true;
                    currentStartX = x;
                    currentStartY = y;
                    currentStopX = x;
                    currentStopY = y;
                    this.invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDrawing) {
                        currentStopX = x;
                        currentStopY = y;
                        this.invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!isDrawing) {
                        break;
                    }
                    isDrawing = false;

                    currentStopX = x;
                    currentStopY = y;

                    Shape newShape = null;
                    switch (curShape) {
                        case LINE:
                            newShape = new Line(currentStartX, currentStartY, currentStopX, currentStopY, Color.RED);
                            break;
                        case CIRCLE:
                            newShape = new Circle(currentStartX, currentStartY, (int) Math.sqrt(Math.pow(currentStopX - currentStartX, 2) + Math.pow(currentStopY - currentStartY, 2)), Color.RED);
                            break;
                        case RECTANGLE:
                            newShape = new Rectangle(currentStartX, currentStartY, currentStopX, currentStopY, Color.RED);
                            break;
                    }

                    if (newShape != null){
                        shapes.add(newShape);
                    }

                    invalidate();
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            for (Shape shape : shapes) {
                shape.draw(canvas, paint);
            }

            if (isDrawing && currentStartX != -1) {
                paint.setColor(Color.RED);
                paint.setStyle(Paint.Style.STROKE);

                switch (curShape) {
                    case LINE:
                        canvas.drawLine(currentStartX, currentStartY, currentStopX, currentStopY, paint);
                        break;
                    case CIRCLE:
                        canvas.drawCircle(currentStartX, currentStartY, (int) Math.sqrt(Math.pow(currentStopX - currentStartX, 2) + Math.pow(currentStopY - currentStartY, 2)), paint);
                        break;
                    case RECTANGLE:
                        canvas.drawRect(currentStartX, currentStartY, currentStopX, currentStopY, paint);
                        break;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, 1, 0, "선 추가");
        menu.add(0, 2, 0, "원 추가");
        menu.add(0, 3, 0, "사각형 추가");
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case 1:
                curShape = LINE;
                return true;
            case 2:
                curShape = CIRCLE;
                return true;
            case 3:
                curShape = RECTANGLE;
                return true;
        }
        return super.onOptionsItemSelected(item);

    }
}