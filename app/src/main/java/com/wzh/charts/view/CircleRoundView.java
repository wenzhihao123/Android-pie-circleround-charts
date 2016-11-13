package com.wzh.charts.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.wzh.charts.R;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

/**
 * Created by zhihao.wen on 2016/11/8.
 */

public class CircleRoundView extends View implements View.OnTouchListener {
    /**
     * 画大圆环画笔
     */
    private Paint lPaint;
    /**
     * 中间圆的画笔
     */
    private Paint mPaint;
    /**
     * 画圆弧的画笔
     */
    private Paint arcPaint;
    /**
     * 宽度最小是200
     */
    private int width =300;
    /**
     * 高度最小是200
     */
    private int height = 300;
    /**
     * 圆心X
     */
    private int radiusX = 0;
    /**
     * |
     * 圆心Y
     */
    private int radiusY = 0;
    /**
     * 最外面大圆环的半径
     */
    private int radiusLager;
    /**
     * 里面圆环的半径
     */
    private int radiusSmall;
    /**
     * 这个是画扇形区域
     */
    private RectF ref;
    /**
     * 绘制的起始角度
     */
    float startAngel = 0f;

    float ratateAngel = 360f;
//
//    boolean isRotation = false;
    /**
     * 当前选中的位置
     */
    private int position = -1;
    /**
     * 这个是点击的位置与水平方向的夹角 0-360
     */
    private double angel;
    /**
     * 点击中间的圆，不响应事件
     */
    private int CLICK_CENTER_ACTION = -2;//0 :点击的是圆环内容，响应事件  1：点击的是圆外，响应事件  -1：点击中间的圆，不响应事件

    /**
     * 传入的数据
     */
    private double[] data ;
    /**
     * 求data之和
     */
    private double sum = 0 ;
    /**
     * 图例每行的个数
     */
//    private int column = 3;
    private String[] lable;
    private int[] colors ;
    /**
     * 存放得到的角度
     */
    private CircleRound[] angels;

    private Rect textRect ;
//    /**
//     * 中间小圆上的文本
//     */
//    private String title = "总营业额148万" ;
//    /**
//     * 中间小圆上的文本第二行
//     */
//    private String title2 = "各年所占比例" ;

    /**
     * 自定义属性值
     */
    private String title ;
    private int titleTextColor ;
    private float titleTextSize ;

    private String subtitle ;
    private int subtitleTextColor ;
    private float subtitleTextSize ;

    private int centerBackgroundColor ;
    private int legendColumn ;
    private int legendTextColor ;
    private float legendTextSize ;

    private boolean isRotate ;


    public CircleRoundView(Context context) {
        this(context, null);
    }

    public CircleRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircleRound);
        title = typedArray.getString(R.styleable.CircleRound_mtitle);
        titleTextColor = typedArray.getColor(R.styleable.CircleRound_mtitleTextColor,ContextCompat.getColor(getContext(),R.color.font_color1));
        titleTextSize = typedArray.getDimension(R.styleable.CircleRound_mtitleTextSize,getResources().getDimension(R.dimen.font_super_big));

        subtitle = typedArray.getString(R.styleable.CircleRound_stitle);
        subtitleTextColor = typedArray.getColor(R.styleable.CircleRound_stitleTextColor,ContextCompat.getColor(getContext(),R.color.font_color1));
        subtitleTextSize = typedArray.getDimension(R.styleable.CircleRound_stitleTextSize,getResources().getDimension(R.dimen.font_super_big));

        centerBackgroundColor = typedArray.getColor(R.styleable.CircleRound_centerBackgroundColor,ContextCompat.getColor(getContext(),R.color.white));
        legendColumn = typedArray.getInt(R.styleable.CircleRound_legendColumn,3);
        legendTextColor = typedArray.getColor(R.styleable.CircleRound_legendTextColor,ContextCompat.getColor(getContext(),R.color.font_color1));
        legendTextSize = typedArray.getDimension(R.styleable.CircleRound_legendTextSize,getResources().getDimension(R.dimen.font_smalll));

        isRotate = typedArray.getBoolean(R.styleable.CircleRound_isRotate,true);

        typedArray.recycle();
        init();
    }

    private void init() {
        setLayerType( LAYER_TYPE_SOFTWARE , null);

        lPaint = new Paint();
        lPaint.setAntiAlias(true);
        lPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(centerBackgroundColor);

        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);

        ref = new RectF();
        textRect = new Rect();

        setOnTouchListener(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator va = ValueAnimator.ofFloat(359,0);
                va.setInterpolator(new LinearInterpolator());
                va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ratateAngel = (float) animation.getAnimatedValue();
                        invalidate();
                    }
                });
                va.setDuration(1000);
                va.start();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sum = getSum(data);
        canvas.save();
        if (isRotate){
            canvas.rotate(ratateAngel,radiusX,radiusY);
        }
        /**
         * 画大圆环
         */
        canvas.drawCircle(radiusX, radiusY, radiusLager, lPaint);
        /**
         * 开始画弧度
         */
        ref.set(getPaddingLeft(), (height - 2 * radiusLager) / 2, width - getPaddingLeft(), 2 * radiusLager + (height - 2 * radiusLager) / 2);
        for (int i = 0; i < data.length; i++) {
            arcPaint.setColor(colors[i]);
            if (i == position) {
                canvas.drawArc(ref, startAngel + 2, (float) (data[i] / sum * 360) - 4, true, arcPaint);
            } else {
                canvas.drawArc(ref, startAngel, (float) (data[i] / sum * 360), true, arcPaint);
            }
            if (angels[i] == null) {
                CircleRound cr = new CircleRound();
                cr.setStartAngel(startAngel);
                cr.setEndAngel(startAngel + (float) (data[i] / sum* 360));
                angels[i] = cr;
            }
            startAngel += (float) (data[i] / sum * 360);

        }
        canvas.restore();
        /**
         * 图例
         */
        for (int i=0;i<angels.length;i++){
            int j = i/legendColumn ;//这个是得到的行数'
            arcPaint.setColor(colors[i]);
            arcPaint.getTextBounds(lable[i],0,lable[i].length(),textRect);
            if (i%legendColumn==0){
                canvas.drawCircle(((width-getPaddingLeft()-getPaddingRight())/legendColumn)*(i-legendColumn*j)+80 ,height-(height-2*radiusLager)/2+60*(j+1), 20, arcPaint);
                arcPaint.setTextSize(legendTextSize);
                arcPaint.setColor(legendTextColor);
                arcPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(lable[i]+"("+new DecimalFormat("#.0").format(angels[i].getAngel()/360f*100)+"%)",((width-getPaddingLeft()-getPaddingRight())/legendColumn)*(i-legendColumn*j)+110, height-(height-2*radiusLager)/2+70*(j+1),arcPaint);
            }else {
                canvas.drawCircle(((width-getPaddingLeft()-getPaddingRight())/legendColumn)*(i-legendColumn*j)+80 ,height-(height-2*radiusLager)/2+60*(j+1), 20, arcPaint);
                arcPaint.setTextSize(legendTextSize);
                arcPaint.setColor(legendTextColor);
                arcPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(lable[i]+"("+new DecimalFormat("#.0").format(angels[i].getAngel()/360f*100)+"%)",((width-getPaddingLeft()-getPaddingRight())/legendColumn)*(i-legendColumn*j)+110, height-(height-2*radiusLager)/2+70*(j+1),arcPaint);
            }

        }
        /**
         * 画小圆环
         */
        mPaint.setShadowLayer(30, 5, 2, Color.GRAY);
        canvas.drawCircle(radiusX, radiusY, radiusSmall, mPaint);
        arcPaint.setTextSize(titleTextSize);
        arcPaint.getTextBounds(title,0,title.length(),textRect);
        arcPaint.setTextAlign(Paint.Align.CENTER);
        /**
         * 画中间圆的文本
         */
        arcPaint.setColor(titleTextColor);
        canvas.drawText(title,width/2, height/2,arcPaint);
        /**
         * 画中间圆的第二行文本
         */
        arcPaint.setColor(subtitleTextColor);
        arcPaint.setTextSize(subtitleTextSize);
        canvas.drawText(subtitle,width/2, height/2+textRect.height()*1.5f,arcPaint);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        if (modeWidth == MeasureSpec.EXACTLY) {
            width = sizeWidth;
        }
        if (modeHeight == MeasureSpec.EXACTLY) {
            height = sizeHeight;
        }

        radiusX = width / 2;
        radiusY = height / 2;
        radiusSmall = Math.min((width - getPaddingLeft() - getPaddingRight()) / 4, (height - getPaddingTop() - getPaddingBottom()) / 4);
        radiusLager = Math.min((width - getPaddingLeft() - getPaddingRight()) / 2, (height - getPaddingTop() - getPaddingBottom()) / 2);
        setMeasuredDimension(width, height);
    }

    /**
     * 计算点击点与横坐标的夹角
     *
     * @param x
     * @param y
     * @return
     */
    public void computeDistance(double x, double y) {
        double centerX = radiusX;
        double centerY = radiusY;
        double rightX = width;
        double rightY = height / 2;
        /**
         * 求出边长
         */
        double a = radiusLager;
        double b = Math.sqrt((x - centerX) * (x - centerX) + (y - centerY) * (y - centerY));
        double c = Math.sqrt((x - rightX) * (x - rightX) + (y - rightY) * (y - rightY));
        /**
         * 这个是点击点到中心圆
         */
        if (b <= radiusSmall) {
            CLICK_CENTER_ACTION = -1 ;
            return ;
        }
        /**
         * 这个是点击点到大圆之外的区域
         */
        if (b>=radiusLager){
            CLICK_CENTER_ACTION = 1 ;
            return ;
        }
        if (a + b <= c || a + c <= b || b + c <= a) {
            if (x > radiusLager) {
                angel = 0;
            } else {
                angel = 180;
            }
            CLICK_CENTER_ACTION = 0 ;
            return ;
        }
        /**
         * 算出是哪个角度
         */
        double r = Math.acos((a * a + b * b - c * c) / (2 * a * b));
        if (y <= height / 2) {
            angel = 360 - Math.toDegrees(r);
        } else {
            angel = Math.toDegrees(r);
        }
        CLICK_CENTER_ACTION = 0 ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAngel = 0;
            computeDistance(event.getX(), event.getY());
            for (int i = 0; i < angels.length; i++) {
                if (angel > angels[i].getStartAngel() && angel < angels[i].getEndAngel() && CLICK_CENTER_ACTION == 0) {
                    position = i;
                    Log.e(TAG, "onTouch: avg="+angels[i].getAvgAngel()+",start="+angels[i].getStartAngel() +",end="+angels[i].getEndAngel()   );
                    postInvalidate();
                }
            }
        }
        return false;
    }

    /**
     * 求和
     */
    public double getSum(double[] arr){
        double result = 0;
        for (int i=0;i<arr.length;i++){
            result=result+arr[i];
        }
        return result ;
    }


    public void setLable(String[] lable) {
        this.lable = lable;
        invalidate();
    }


    public void setColors(int[] colors) {
        this.colors = colors;
        invalidate();
    }

    public double[] getData() {
        return data;
    }

    public void setData(double[] data) {
        this.data = data;
        angels = new CircleRound[data.length];
        invalidate();
    }


    public void setLegendColumn(int legendColumn) {
        this.legendColumn = legendColumn;
        invalidate();
    }
}
