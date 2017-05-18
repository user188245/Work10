package com.example.user.work10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by user on 2017-05-18.
 */

public class MyDrawingView extends View {
    int xOffset = -1;
    int yOffset = -1;
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint;
    String fileName = getContext().getFilesDir() + "/mybitmap.png";


    private boolean isStamp = false;
    private boolean isBlurring = false;
    private boolean isColoring = false;
    private boolean isRotate = false;
    private boolean isSkew = false;
    private boolean isMove = false;
    private boolean isScale = false;
    private boolean isRainbowMode = false;

    private int h = 0;
    private int r = 255;
    private int g = 0;
    private int b = 0;



    float[] array = {
            2,0,0,4,-1f,
            0,2,0,4,-1f,
            4,0,2,0,-1f,
            4,0,0,0,2};
    ColorMatrix colorMatrix = new ColorMatrix(array);
    BlurMaskFilter blurMaskFilter = new BlurMaskFilter(100,BlurMaskFilter.Blur.INNER);


    private void alterRGB(){
        h++;
        if(h<(256) && g<255){
            g++;
        }else if(h<(256*2) && r>0){
            r--;
        }else if(h<(256*3) && b<255){
            b++;
        }else if(h<(256*4) && g>0){
            g--;
        }else if(h<(256*5) && r<255){
            r++;
        }else if(h<(256*6) && b>0){
            b--;
        }else if(h==(256*6)){
            h=0;
        }
        Log.e("E",r+","+g+","+b);
    }


    public MyDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

    }

    public MyDrawingView(Context context) {
        this(context, null);
    }

    public void setParam(String tag, boolean param){
        if(tag.equals("ERASER")){
            mBitmap.eraseColor(Color.YELLOW);
            invalidate();
        }else if(tag.equals("OPEN")) {
            readFile();
        }else if(tag.equals("SAVE")){
            saveFile();
        }else if(tag.equals("ROTATE")){
            isRotate = true;
        }else if(tag.equals("MOVE")){
            isMove = true;
        }else if(tag.equals("SCALE")){
            isScale = true;
        }else if(tag.equals("SKEW")){
            isSkew = true;
        }else if(tag.equals("RED")){
            mPaint.setColor(Color.RED);
            isRainbowMode = false;
        }else if(tag.equals("BLUE")){
            mPaint.setColor(Color.BLUE);
            isRainbowMode = false;
        }else if(tag.equals("RAINBOW")){
            isRainbowMode = true;
        }else if(tag.equals("BLURRING")){
            isBlurring = param;
        }else if(tag.equals("COLORING")){
            isColoring = param;
        }else if(tag.equals("BOLD")){
            if(param)
                mPaint.setStrokeWidth(8);
            else
                mPaint.setStrokeWidth(5);
        }else if(tag.equals("STAMP")){
            isStamp = param;
            if(!isStamp){
                isMove = false;
                isRotate = false;
                isScale = false;
                isSkew = false;
                mCanvas = new Canvas();
                mCanvas.setBitmap(mBitmap);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmap != null)
            canvas.drawBitmap(mBitmap, 0, 0, null);

    }
    int oldX = -1; int oldY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        xOffset = (int)event.getX();
        yOffset = (int)event.getY();

        if(isStamp) {
            drawStamp();
            invalidate();
        }
        else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                oldX = xOffset;
                oldY = yOffset;

            } else if ((event.getAction() == MotionEvent.ACTION_MOVE)) {
                if (oldX != -1) {
                    if(isRainbowMode){
                        mPaint.setColor(Color.rgb(r,g,b));
                        alterRGB();
                        alterRGB();
                        alterRGB();
                    }
                    mCanvas.drawLine(oldX, oldY, xOffset, yOffset, mPaint);
                    invalidate();
                    oldX = xOffset;
                    oldY = yOffset;
                }
            } else if ((event.getAction() == MotionEvent.ACTION_UP)) {
                oldX = -1;
                oldY = -1;
            }
        }
        return true;
    }

    private void initialize(){
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
        mCanvas.drawColor(Color.YELLOW);
        invalidate();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        initialize();

    }
    float rotationVal = 0f;
    Matrix m;
    private void drawStamp(){
        Bitmap img = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        m = new Matrix();
        m.setTranslate(xOffset,yOffset);
        if(isColoring)
            mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        else
            mPaint.setColorFilter(null);
        if(isBlurring)
            mPaint.setMaskFilter(blurMaskFilter);
        else
            mPaint.setMaskFilter(null);
        if(isScale)
            m.preScale(2f,2f);
        if(isRotate)
            m.preRotate(rotationVal+=30f);
        if(isSkew)
            m.preSkew(0.2f,1f);
        if(isMove)
            m.preTranslate((int)(Math.random()*50-50),(int)(Math.random()*50-50));
        mCanvas.drawBitmap(img,m,mPaint);
        img.recycle();
        mCanvas = new Canvas();
        mCanvas.setBitmap(mBitmap);
    }

    private boolean saveFile(){
        try {
            writeByte(fileName);
            Toast.makeText(getContext(),"성공적으로 저장했습니다. to " + fileName,Toast.LENGTH_SHORT).show();
            return true;
        }catch(Exception e){
            Toast.makeText(getContext(),"저장에 실패했습니다.",Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean readFile(){
        try{
            BitmapFactory.Options bO = new BitmapFactory.Options();
            bO.inMutable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName,bO);
            if(bitmap == null){
                Toast.makeText(getContext(),"로드에 실패했습니다.",Toast.LENGTH_SHORT).show();
                return false;
            }
            bitmap = Bitmap.createScaledBitmap(bitmap,(int)(0.7*mBitmap.getWidth()),(int)(0.7*mBitmap.getHeight()),false);
            mCanvas.drawColor(Color.YELLOW);
            mCanvas.drawBitmap(bitmap, (mCanvas.getWidth() - bitmap.getWidth())/2f , (mCanvas.getHeight() - bitmap.getHeight())/2f, null);
            invalidate();
            Toast.makeText(getContext(),"로드되었습니다.",Toast.LENGTH_SHORT).show();
            return true;
        }
        catch(RuntimeException e) {
            Toast.makeText(getContext(), "파일이 없거나 읽을 수 없습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void writeByte(String dir){
        try{
            FileOutputStream fout = new FileOutputStream(dir,false);
            mBitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e){}
    }
}
