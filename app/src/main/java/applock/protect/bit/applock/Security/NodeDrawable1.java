package applock.protect.bit.applock.Security;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

public class NodeDrawable1 extends Drawable
{
    public static final int STATE_UNSELECTED = 0, STATE_SELECTED = 1,
           STATE_HIGHLIGHTED = 2, STATE_CORRECT = 3, STATE_INCORRECT = 4,
           STATE_CUSTOM = 5;
    
    public static int white = 0xffffffff;
    public static int gray = 0xff999999;
    public static int green = 0xff71bf48;
    public static int seagreen = 0xff00cccc;
    public static int red = 0xffef4b4c;  
    
    public static int text1color = 0x666666;
    
    public static int default_pattern_color = gray ; // = R.color.sidemenu_list_text_color //= white
    
    public static final int[] DEFAULT_STATE_COLORS = { 
    	default_pattern_color, green, seagreen, green, red, default_pattern_color             
    };
    
    public static final int CIRCLE_COUNT = 3;
    public static final int CIRCLE_OUTER = 0, CIRCLE_MIDDLE = 1,
           CIRCLE_INNER = 2;
    public static final float[] CIRCLE_RATIOS = {
          0.60f, 0.59f, 0.11f //0.7f, 0.68f, 0.11f
    };
    public static final int[] DEFAULT_CIRCLE_COLORS = {
        DEFAULT_STATE_COLORS[STATE_UNSELECTED], white, default_pattern_color  
    };
    public static final int[] CIRCLE_ORDER = {
    	CIRCLE_OUTER , CIRCLE_MIDDLE, CIRCLE_INNER
    };

    // For drawing an arrow exit indicator
    protected float mArrowTipRad, mArrowBaseRad, mArrowHalfBase;

    protected ShapeDrawable mCircles[];
    protected Paint mExitPaint;
    protected Path mExitIndicator;
    protected float mExitAngle;
    protected Point mCenter;
    protected float mDiameter;
    protected int mState;
    protected int mCustomColor;
    Context con;

    public NodeDrawable1(float diameter, Point center ,Context context)
    {    	
    	con = context;
        mCircles = new ShapeDrawable[CIRCLE_COUNT];
        mCenter = center;
        mDiameter = diameter;
        mState = STATE_UNSELECTED;
        mExitAngle = Float.NaN;
        setCustomColor(DEFAULT_STATE_COLORS[STATE_CUSTOM]);

        mExitPaint = new Paint();
        mExitPaint.setColor(STATE_CORRECT);
        mExitPaint.setStyle(Paint.Style.FILL); //fill
        mExitPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        buildShapes(diameter, center);
    }

    @Override
    public void draw(Canvas canvas)
    {
        for(int ii = 0; ii < CIRCLE_COUNT; ii++)
        {
            mCircles[CIRCLE_ORDER[ii]].draw(canvas);
        }
        if(!Float.isNaN(mExitAngle))
        {
            canvas.drawPath(mExitIndicator, mExitPaint);
        }
    }

    private void buildShapes(float outerDiameter, Point center)
    {
        for(int ii = 0; ii < CIRCLE_COUNT; ii++)
        {
            mCircles[ii] = new ShapeDrawable(new OvalShape());
            Paint circlePaint = mCircles[ii].getPaint();
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setStrokeWidth(6);
            circlePaint.setColor(DEFAULT_CIRCLE_COLORS[ii]);
            circlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

            float diameter = outerDiameter * CIRCLE_RATIOS[ii];
            int offset = (int) (diameter / 2.0f);

            mCircles[ii].setBounds(center.x - offset, center.y - offset,
                    center.x + offset, center.y + offset);
                     
        }
        
        Paint circlePaint = mCircles[CIRCLE_OUTER].getPaint();
        circlePaint.setColor(default_pattern_color);
        circlePaint.setStyle(Paint.Style.STROKE);
        /*if(Common.isTablet10Inch(con)){
        	circlePaint.setStrokeWidth(3);
        }
        else if(Common.isTablet7Inch(con)){
        	circlePaint.setStrokeWidth(2);
        }
        else{*/
        	circlePaint.setStrokeWidth(2);
        //} 
        
        Paint circlePaint1 = mCircles[CIRCLE_MIDDLE].getPaint();
        circlePaint1.setColor(default_pattern_color);
        mCircles[CIRCLE_MIDDLE].setAlpha(50);
        
        Paint circlePaint2 = mCircles[CIRCLE_INNER].getPaint();
        //circlePaint2.setStyle(Paint.Style.STROKE);
        //circlePaint2.setStrokeWidth(2);
        circlePaint2.setColor(default_pattern_color);

        // crunch variables for exit arrows independent of angle
        float middleDiameter = outerDiameter * CIRCLE_RATIOS[CIRCLE_MIDDLE];

        mArrowTipRad = middleDiameter / 2.0f * 0.9f;
        mArrowBaseRad = middleDiameter / 2.0f * 0.6f;
        mArrowHalfBase = middleDiameter / 2.0f * 0.3f;
    }

    //
    // Accessors / mutators
    //

    public void setNodeState(int state)
    {
        int color = mCustomColor;
        if(state != STATE_CUSTOM)
        {
            color = DEFAULT_STATE_COLORS[state];
        }
         
        Paint circlePaint = mCircles[CIRCLE_OUTER].getPaint();
        circlePaint.setColor(color);
        circlePaint.setStyle(Paint.Style.STROKE);
        /*if(Common.isTablet10Inch(con)){
        	circlePaint.setStrokeWidth(12);
        }
        else if(Common.isTablet7Inch(con)){
        	circlePaint.setStrokeWidth(7);
        }
        else{*/
        	circlePaint.setStrokeWidth(3);
        //}
        
        
        Paint circlePaint1 = mCircles[CIRCLE_MIDDLE].getPaint();
        circlePaint1.setColor(color);
        mCircles[CIRCLE_MIDDLE].setAlpha(50);
        
        Paint circlePaint2 = mCircles[CIRCLE_INNER].getPaint();
        circlePaint2.setColor(color);
         
        mExitPaint.setColor(color);
        
        if(state == STATE_INCORRECT)
        {
        	Paint UselectedOuterCircle = mCircles[CIRCLE_OUTER].getPaint();
        	UselectedOuterCircle.setColor(color);
        	UselectedOuterCircle.setStyle(Paint.Style.STROKE);
        	/*if(Common.isTablet10Inch(con)){
        		UselectedOuterCircle.setStrokeWidth(12);
            }
            else if(Common.isTablet7Inch(con)){
            	UselectedOuterCircle.setStrokeWidth(7);
            }
            else{*/
            	UselectedOuterCircle.setStrokeWidth(3);
            //}   
            	
        	Paint UselectedMiddleCircle = mCircles[CIRCLE_MIDDLE].getPaint();
        	UselectedMiddleCircle.setColor(color);
            mCircles[CIRCLE_MIDDLE].setAlpha(50); 	
            
            Paint UselectedInnerCircle = mCircles[CIRCLE_INNER].getPaint();
            UselectedInnerCircle.setColor(color);
             
            mExitPaint.setColor(color);
                     
        }
        
        if(state == STATE_UNSELECTED)
        {
        	Paint UselectedOuterCircle = mCircles[CIRCLE_OUTER].getPaint();
        	UselectedOuterCircle.setColor(default_pattern_color);
        	UselectedOuterCircle.setStyle(Paint.Style.STROKE);
        	/*if(Common.isTablet10Inch(con)){
        		UselectedOuterCircle.setStrokeWidth(3);
            }
            else if(Common.isTablet7Inch(con)){
            	UselectedOuterCircle.setStrokeWidth(2);
            }
            else{*/
            	UselectedOuterCircle.setStrokeWidth(2);
            //}   
            	
        	Paint UselectedMiddleCircle = mCircles[CIRCLE_MIDDLE].getPaint();
        	UselectedMiddleCircle.setColor(default_pattern_color);
            mCircles[CIRCLE_MIDDLE].setAlpha(50);
            
            Paint UselectedInnerCircle = mCircles[CIRCLE_INNER].getPaint();
            UselectedInnerCircle.setColor(color);
            
            
            mExitPaint.setColor(color);
            
            setExitAngle(Float.NaN);
        }
        mState = state;
    }
    
    public int getNodeState()
    {
        return mState;
    }

    public void setExitAngle(float angle)
    {
        // construct exit indicator arrow
        if(!Float.isNaN(angle))
        {
            float tipX = mCenter.x - ((float) Math.cos(angle)) * mArrowTipRad;
            float tipY = mCenter.y - ((float) Math.sin(angle)) * mArrowTipRad;

            float baseCenterX = mCenter.x
                - ((float) Math.cos(angle)) * mArrowBaseRad;
            float baseCenterY = mCenter.y
                - ((float) Math.sin(angle)) * mArrowBaseRad;

            // first base vertex of arrow
            float baseVertAX = baseCenterX
                - mArrowHalfBase * ((float) Math.cos(angle + Math.PI / 2));
            float baseVertAY = baseCenterY
                - mArrowHalfBase * ((float) Math.sin(angle + Math.PI / 2));
            // second base vertex of arrow
            float baseVertBX = baseCenterX
                - mArrowHalfBase * ((float) Math.cos(angle - Math.PI / 2));
            float baseVertBY = baseCenterY
                - mArrowHalfBase * ((float) Math.sin(angle - Math.PI / 2));

            Path arrow = new Path();
            arrow.moveTo(tipX, tipY);
            arrow.lineTo(baseVertAX, baseVertAY);
            arrow.lineTo(baseVertBX, baseVertBY);
            arrow.lineTo(tipX, tipY);

            mExitIndicator = arrow;
        }
        mExitAngle = angle;
    }
    public float getExitAngle()
    {
        return mExitAngle;
    }

    public Point getCenter()
    {
        return mCenter;
    }

    public void setCustomColor(int color)
    {
        mCustomColor = color;
    }
    public int getCustomColor()
    {
        return mCustomColor;
    }

    //
    // Required methods for a Drawable, generally just phoning it in to the
    // child drawables
    //

    @Override
    public int getOpacity()
    {
        return mCircles[CIRCLE_MIDDLE].getOpacity();
    }
    
    @Override
    public void setAlpha(int alpha)
    {
            mCircles[0].setAlpha(alpha);
    }
    
//    @Override
//    public void setAlpha(int alpha)
//    {
//        for(int ii = 0; ii < CIRCLE_COUNT; ii++)
//        {
//            mCircles[ii].setAlpha(alpha);
//        }
//    }

    @Override
    public void setColorFilter(android.graphics.ColorFilter cf)
    {
        for(int ii = 0; ii < CIRCLE_COUNT; ii++)
        {
            mCircles[ii].setColorFilter(cf);
        }
    }
}