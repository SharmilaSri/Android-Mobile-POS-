package com.retailx.dreamdx.retailx.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class DrawView extends View {

    private Rect rectangle;
    private Paint paint;

    public DrawView(Context context) {
        super(context);
        int x = 50;
        int y = 50;
        int sideLength = 200;




    }

    @Override
    public void onDraw(Canvas canvas) {
        // create a rectangle that we'll draw later
        rectangle = new Rect(50, 50, 80, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(rectangle, paint);

        rectangle = new Rect(80, 350, 110, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(rectangle, paint);




        rectangle = new Rect(130, 100, 160, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(rectangle, paint);

        rectangle = new Rect(160, 200, 190, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(rectangle, paint);



        rectangle = new Rect(210, 200, 240, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(rectangle, paint);

        rectangle = new Rect(240, 400, 270, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(rectangle, paint);


        rectangle = new Rect(290, 50, 320, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.GRAY);
        canvas.drawRect(rectangle, paint);

        rectangle = new Rect(320, 400, 350, 500);
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(rectangle, paint);

    }

}
