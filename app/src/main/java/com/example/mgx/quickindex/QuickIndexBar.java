package com.example.mgx.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mgx on 2016/6/30.
 */
public class QuickIndexBar extends View {

    private onLetterIndexListener mOnLetterIndexListener;

    public void setOnLetterIndexListener(onLetterIndexListener onLetterIndexListener) {
        mOnLetterIndexListener = onLetterIndexListener;
    }

    public interface onLetterIndexListener {
        void onLetterIndex(String letter);
    }
    private Paint mPaint;
    // 字母数组
    private static final String[] LETTERS = new String[]{"A", "B", "C", "D",
            "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
            "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private float mCellHeight;
    private float mCellWight;

    public QuickIndexBar(Context context) {
        this(context, null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        init();
    }

    private void init() {
        //创建绘画笔
        mPaint = new Paint();
        //设置颜色为白色
        mPaint.setColor(Color.WHITE);
        //消锯齿
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(30f);
        //设置字体为粗体
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        for (int i = 0; i<LETTERS.length;i++) {
            String letter = LETTERS[i];
            //测量x坐标
            float x = mCellWight*0.5f - mPaint.measureText(letter)*0.5f;
            //测量y坐标
            Rect bound = new Rect();
            mPaint.getTextBounds(letter,0,letter.length(),bound);
            float y = mCellHeight*0.5f + bound.height()+ i*mCellHeight;
            if (lastIndex == i){
                mPaint.setColor(Color.GRAY);
            }else {
                mPaint.setColor(Color.WHITE);
            }
            //绘制文本
            canvas.drawText(letter,x,y, mPaint);

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();//获取测量的宽
        int measuredHeight = getMeasuredHeight();//获取测量的高

        //获得单元的高
        mCellHeight = measuredHeight * 1.0f / LETTERS.length;
        mCellWight = measuredWidth;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    private  int lastIndex = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y ;
        int currentindex;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY();//获取落下的坐标
                currentindex = (int) (y/mCellHeight);
                if (lastIndex != currentindex) {
                    if (currentindex >=0 && currentindex <= LETTERS.length) {
                        String letter = LETTERS[currentindex];
                        if (mOnLetterIndexListener != null) {
                            mOnLetterIndexListener.onLetterIndex(letter);
                        }
                        lastIndex = currentindex;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                y = event.getY();
                currentindex = (int) (y / mCellHeight);
                if (currentindex >= 0 && currentindex < LETTERS.length) {
                    String letter = LETTERS[currentindex];
                    if (mOnLetterIndexListener != null) {
                        mOnLetterIndexListener.onLetterIndex(letter);
                    }
                    lastIndex = currentindex;
                }
                break;
            case MotionEvent.ACTION_UP:
                lastIndex = -1;
                break;
        }
        invalidate();
        return true;
    }
}
