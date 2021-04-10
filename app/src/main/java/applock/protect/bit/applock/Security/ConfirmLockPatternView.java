package applock.protect.bit.applock.Security;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import applock.protect.bit.applock.Common;
import applock.protect.bit.applock.R;

public class ConfirmLockPatternView extends View
{
    public static final int DEFAULT_LENGTH_PX = 100, DEFAULT_LENGTH_NODES = 3;  //100
    public static final float CELL_NODE_RATIO = 0.90f, NODE_EDGE_RATIO = 0.33f;
    public static final int EDGE_COLOR = 0xffc2d6eb; 
    public static final int LINE_COLOR = 0xff66FFCC; 
    public static final int BACKGROUND_COLOR = 0xff004285;  
    public static final int DEATH_COLOR = 0xffff0000;
    public static final int PRACTICE_RESULT_DISPLAY_MILLIS = 1 * 1000;
    public static final long BUILD_TIMEOUT_MILLIS = 1 * 1000;
    public static final int TACTILE_FEEDBACK_DURATION = 35;
    
    public static final int DEFAULT_PATTERN_GREEN_LINE_COLOR = NodeDrawable1.green;
    public static final int DEFAULT_PATTERN_RED_LINE_COLOR = NodeDrawable1.red;

    protected int mLengthPx;
    protected int mLengthNodes;
    protected int mCellLength;
    protected NodeDrawable1[][] mNodeDrawables;
    protected Paint mEdgePaint;
    protected static Paint mLinePaint;
    protected HighlightMode mHighlightMode;
    protected boolean mPracticeMode;
    protected Point mTouchPoint;
    protected Point mTouchCell;
    protected boolean mDrawTouchExtension;
    protected int mTouchThreshold;
    protected boolean mDisplayingPracticeResult;
    protected HighlightMode mPracticeFailureMode;
    protected HighlightMode mPracticeSuccessMode;
    protected Handler mHandler;
    protected Vibrator mVibrator;
    protected boolean mTactileFeedback;

    protected List<Point> mCurrentPattern;
    protected List<Point> mPracticePattern;
    protected Set<Point> mPracticePool;
    String DecoyPattern;
    
    public static boolean IsUninstallProtectionActivity = false ;
    
    public static boolean IsUninstallFolderLcokActivity = false ;
    
    public Context con;
    
    public ConfirmLockPatternView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        con = context;
        mLengthPx = DEFAULT_LENGTH_PX;
        mLengthNodes = DEFAULT_LENGTH_NODES;
        mNodeDrawables = new NodeDrawable1[0][0];
        mCurrentPattern = Collections.emptyList();
        mHighlightMode = new NoHighlight();
        mTouchPoint = new Point(-1, -1);
        mTouchCell = new Point(-1, -1);
        mDrawTouchExtension = false;
        mDisplayingPracticeResult = false;
        mPracticeFailureMode = new FailureHighlight();
        mPracticeSuccessMode = new SuccessHighlight();
        mHandler = new Handler();
        mVibrator = (Vibrator) getContext()
            .getSystemService(Context.VIBRATOR_SERVICE);

        mEdgePaint = new Paint();
        mEdgePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
        mEdgePaint.setStrokeCap(Paint.Cap.ROUND);
        mEdgePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        
        mLinePaint = new Paint();
        mLinePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);
        mLinePaint.setAlpha(100);
        mLinePaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        SecurityLocksSharedPreferences securityCredentialsSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);
    	DecoyPattern = SecurityLocksCommon.GetDecoyPassword(getContext());
    }

    // called whenever either the actual drawn length or the nodewise length
    // changes
    private void buildDrawables()
    {
        mNodeDrawables = new NodeDrawable1[mLengthNodes][mLengthNodes];

        mCellLength = mLengthPx / mLengthNodes;

        float nodeDiameter = ((float) mCellLength) * CELL_NODE_RATIO;
        mEdgePaint.setStrokeWidth(nodeDiameter * NODE_EDGE_RATIO);
        mTouchThreshold = (int) (nodeDiameter / 2);
        int cellHalf = mCellLength / 2;

        long buildStart = System.currentTimeMillis();
        for(int y = 0; y < mLengthNodes; y++)
        {
            for(int x = 0; x < mLengthNodes; x++)
            {
                // if just building the drawables is taking too long, bail!
                if(System.currentTimeMillis() - buildStart
                        >= BUILD_TIMEOUT_MILLIS)
                {
                	PatternActivityMethods.clearAndBail(getContext());
                }
                Point center = new Point(x * mCellLength + cellHalf,
                        y * mCellLength + cellHalf);
                mNodeDrawables[x][y] = new NodeDrawable1(nodeDiameter, center ,con);
            }
        }

        // re-highlight nodes if not in practice
        if(!mPracticeMode)
        {
            loadPattern(mCurrentPattern, mHighlightMode);
        }
    }

    private void clearPattern(List<Point> pattern)
    {
        for(Point e : pattern)
        {
            mNodeDrawables[e.x][e.y]
                .setNodeState(NodeDrawable1.STATE_UNSELECTED);
            mLinePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
            mLinePaint.setAlpha(100);
        }
    }

    private void loadPattern(List<Point> pattern, HighlightMode highlightMode)
    {
        for(int ii = 0; ii < pattern.size(); ii++)
        {
            Point e = pattern.get(ii);
            NodeDrawable1 node = mNodeDrawables[e.x][e.y];
            int state = highlightMode.select(node, ii, pattern.size(),
                    e.x, e.y, mLengthNodes);
            node.setNodeState(state); // rolls off the tongue
            // if another node follows, then tell the current node which way
            // to point
            if(ii < pattern.size() - 1)
            {
                Point f = pattern.get(ii+1);
                Point centerE = mNodeDrawables[e.x][e.y].getCenter();
                Point centerF = mNodeDrawables[f.x][f.y].getCenter();

                mNodeDrawables[e.x][e.y].setExitAngle((float)
                        Math.atan2(centerE.y - centerF.y,
                            centerE.x - centerF.x));
            }
        }
    }

    // only works properly with practice mode due to highlighting, should
    // probably be generalized and used to replace the bulk of loadPattern()
    private void appendPattern(List<Point> pattern, Point node)
    {
        NodeDrawable1 nodeDraw = mNodeDrawables[node.x][node.y];
        nodeDraw.setNodeState(NodeDrawable1.STATE_SELECTED);
        if(pattern.size() > 0)
        {
            Point tailNode = pattern.get(pattern.size() - 1);
            NodeDrawable1 tailDraw = mNodeDrawables[tailNode.x][tailNode.y];

            Point tailCenter = tailDraw.getCenter();
            Point nodeCenter = nodeDraw.getCenter();

            tailDraw.setExitAngle((float)
                    Math.atan2(tailCenter.y - nodeCenter.y,
                        tailCenter.x - nodeCenter.x));
        }
        pattern.add(node);
    }


    private void testPracticePattern()
    {
        mDisplayingPracticeResult = true;
        HighlightMode mode = mPracticeFailureMode;
        if(PatternActivityMethods.ConvertPatternToNo(mPracticePattern).equals(SecurityLocksCommon.PatternPassword))
        {
            mode = mPracticeSuccessMode;
            Log.v(" Success", "Great ");

        }
        else if(PatternActivityMethods.ConvertPatternToNo(mPracticePattern).equals(DecoyPattern) && !SecurityLocksCommon.IsConfirmPatternActivity)
        {
            mode = mPracticeSuccessMode;
            Log.v(" Success", "Great ");

        }
        loadPattern(mPracticePattern, mode);

        // clear the result display after a delay
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mDisplayingPracticeResult) {
                	//testPattern(mSiginPattern,mPracticePattern);
                	testPasswordPattern(PatternActivityMethods.ConvertPatternToNo(mPracticePattern));

                	resetPractice();
                    invalidate();

                }
            }
        }, PRACTICE_RESULT_DISPLAY_MILLIS);
    }

    public void testPattern(List<Point> mPracticePattern, List<Point> mCurrentPattern)
    {

		if(mPracticePattern.equals(mCurrentPattern))
        {
			if(SecurityLocksCommon.IsConfirmPatternActivity){

	    		Intent intentpattern = new  Intent(con, SecurityLocksActivity.class);
	    		con.startActivity(intentpattern);
				((Activity) getContext()).finish();
	    	}
            Log.v(" Success", "Great ");
        }
		else{
        	Log.v(" Fail", "Fail ");
        }

     // clear the result display after a delay
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                    resetPractice();
                    invalidate();
            }
        }, PRACTICE_RESULT_DISPLAY_MILLIS);

    }

    public void testPasswordPattern(String PasswordPattern)
    {
    	SecurityLocksSharedPreferences securityLocksSharedPreferences = SecurityLocksSharedPreferences.GetObject(con);
		if(PasswordPattern.equals(SecurityLocksCommon.PatternPassword))
        {   
			SecurityLocksCommon.IsFakeAccount = 0;
			if(SecurityLocksCommon.IsConfirmPatternActivity){
				SecurityLocksCommon.IsAppDeactive = false;
				SecurityLocksCommon.isBackupPattern = false;
				SecurityLocksCommon.isSettingDecoy = false;
				SecurityLocksCommon.IsConfirmPatternActivity = false;				
	    		Intent intentpattern = new  Intent(con, SecurityLocksActivity.class);
	    		intentpattern.putExtra("isSettingDecoy", false);
	    		con.startActivity(intentpattern);
				((Activity) getContext()).finish();
			
	    	}
			else if(SecurityLocksCommon.isSettingDecoy) {
				SecurityLocksCommon.IsAppDeactive = false;
				SecurityLocksCommon.isSettingDecoy = false;
				SecurityLocksCommon.IsConfirmPatternActivity = false;	
				SecurityLocksCommon.isBackupPattern = false;				
	    		Intent intentpattern = new  Intent(con, SetPatternActivity.class);
	    		intentpattern.putExtra("isSettingDecoy", true);
	    		con.startActivity(intentpattern);
				((Activity) getContext()).finish();
			 }
			else if(SecurityLocksCommon.isBackupPattern) {
				SecurityLocksCommon.IsAppDeactive = false;
				SecurityLocksCommon.IsConfirmPatternActivity = false;	
				SecurityLocksCommon.isSettingDecoy = false;
				SecurityLocksCommon.isBackupPattern = false;				
	    		//Intent intentpattern = new  Intent(con, RecoveryOfCredentialsActivity.class);
	    		//con.startActivity(intentpattern);
				((Activity) getContext()).finish();
			 }			
			else{
				Common.loginCount = securityLocksSharedPreferences.GetRateCount();
				Common.loginCount ++;
				securityLocksSharedPreferences.SetRateCount(Common.loginCount);
				SecurityLocksCommon.IsFakeAccount = 0;
				SecurityLocksCommon.IsnewloginforAd = true;
                SecurityLocksCommon.Isfreshlogin = true;
				SecurityLocksCommon.IsAppDeactive = true;
				if(SecurityLocksCommon.IsAppDeactive && SecurityLocksCommon.CurrentActivity != null){
					Intent intlogin = new Intent(con, SecurityLocksCommon.CurrentActivity.getClass());
					con.startActivity(intlogin);
					((Activity) getContext()).finish();
    			}
    			else{  
    				Common.loginCount = securityLocksSharedPreferences.GetRateCount();
    				Common.loginCount ++;
    				securityLocksSharedPreferences.SetRateCount(Common.loginCount);
    				Intent intlogin = new Intent(con, SecurityLocksActivity.class);
    				con.startActivity(intlogin);	
    				((Activity) getContext()).finish();
    			}
	    	}
            Log.v(" Success", "Great ");
            
        }
/*		else if(PasswordPattern.equals(DecoyPattern)){
			
			if(SecurityLocksCommon.IsConfirmPatternActivity){
                loadPattern(mPracticePattern, mPracticeFailureMode);
                ConfirmPatternActivity.lblConfirmpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Tryagain);
	    	}
			else{
				SecurityLocksCommon.IsFakeAccount = 1;
				Intent intentMainActivity = new Intent(con, SecurityLocksActivity.class);
				con.startActivity(intentMainActivity);
				((Activity) getContext()).finish();
			}
		}*/
		else{
        	if(SecurityLocksCommon.IsConfirmPatternActivity){
        		loadPattern(mPracticePattern, mPracticeFailureMode);
        		ConfirmPatternActivity.lblConfirmpattern.setText(R.string.lblsetting_SecurityCredentials_Setpattern_Tryagain);
        	}else{
        		loadPattern(mPracticePattern, mPracticeFailureMode);
        		
        		
        	}        	
        	Log.v(" Fail", "Fail ");
        }  
    }
    
    /*private void StopService() {
		Intent intent = new Intent(con, HTTPService.class);
		con.stopService(intent);
		
		 SharedPreferences serverPerfer = con.getSharedPreferences("LoginPerfer", con.MODE_PRIVATE);
		 SharedPreferences.Editor prefsEditor =  serverPerfer.edit();
		 prefsEditor.putBoolean("IsServerStart", false);
		 prefsEditor.commit(); 
			
	}
*/
    public void resetPractice()
    {
        clearPattern(mPracticePattern);
        mPracticePattern.clear();
        mPracticePool.clear();
        mDisplayingPracticeResult = false;
    }

    //
    // android.view.View overrides
    //

    @Override
    protected void onDraw(Canvas canvas)
    {
        // draw pattern edges first
        Point edgeStart, edgeEnd;
        List<Point> pattern = mCurrentPattern;
        if(mPracticeMode)
        {
            pattern = mPracticePattern;
        }
        CenterIterator patternPx = new CenterIterator(pattern.iterator());

        if(patternPx.hasNext())
        {
        	/*if(Common.isTablet10Inch(con)){
       		 mLinePaint.setStrokeWidth(33);
            }
            else if(Common.isTablet7Inch(con)){
           	 mLinePaint.setStrokeWidth(23);
            }
            else{*/
           	 mLinePaint.setStrokeWidth(9);
           	 mLinePaint.setAlpha(100);
           // } 
        	
            edgeStart = patternPx.next();
            while(patternPx.hasNext())
            {
                edgeEnd = patternPx.next();
                canvas.drawLine(edgeStart.x, edgeStart.y, edgeEnd.x, edgeEnd.y,
                        mLinePaint);

                edgeStart = edgeEnd;
            }
            if(mDrawTouchExtension)
            {
                canvas.drawLine(edgeStart.x, edgeStart.y, mTouchPoint.x,
                        mTouchPoint.y, mLinePaint);
            }
        }

        // then draw nodes
        for(int y = 0; y < mLengthNodes; y++)
        {
            for(int x = 0; x < mLengthNodes; x++)
            {
                mNodeDrawables[x][y].draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(!mPracticeMode)
        {
            return super.onTouchEvent(event);
        }
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if(mDisplayingPracticeResult)
                {
                    resetPractice();
                }
                mDrawTouchExtension = true;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX(), y = event.getY();
                mTouchPoint.x = (int) x;
                mTouchPoint.y = (int) y;
                mTouchCell.x = (int) x / mCellLength;
                mTouchCell.y = (int) y / mCellLength;
                if(mTouchCell.x < 0 || mTouchCell.x >= mLengthNodes
                        || mTouchCell.y < 0 || mTouchCell.y >= mLengthNodes)
                {
                    break;
                }
                Point nearestCenter =
                    mNodeDrawables[mTouchCell.x][mTouchCell.y].getCenter();
                int dist = (int) Math.sqrt(Math.pow(x - nearestCenter.x, 2)
                        + Math.pow(y - nearestCenter.y, 2));
                if(dist < mTouchThreshold
                        && !mPracticePool.contains(mTouchCell))
                {
                    if(mTactileFeedback)
                    {
                        mVibrator.vibrate(TACTILE_FEEDBACK_DURATION);
                    }
                    Point newPoint = new Point(mTouchCell);
                    appendPattern(mPracticePattern, newPoint);
                    mPracticePool.add(newPoint);
                }
                break;
            case MotionEvent.ACTION_UP:
                mDrawTouchExtension = false;
                testPracticePattern();
                break;
            default:
                return super.onTouchEvent(event);
        }
        invalidate();
        return true;
    }

    // expand to be as large as the smallest dictated size, or to the default
    // length if both dimensions are unspecified
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int length = 0;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if(wMode == MeasureSpec.UNSPECIFIED
                && hMode == MeasureSpec.UNSPECIFIED)
        {
            length = DEFAULT_LENGTH_PX;
            setMeasuredDimension(length, length);
        }
        else if(wMode == MeasureSpec.UNSPECIFIED)
        {
            length = height;
        }
        else if(hMode == MeasureSpec.UNSPECIFIED)
        {
            length = width;
        }
        else
        {
            length = Math.min(width,height);
        }

        setMeasuredDimension(length,length);
    }

    // update draw values dependent on view size so it doesn't have to happen
    // in every onDraw()
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        mLengthPx = Math.min(w,h);
        buildDrawables();
        if(!mPracticeMode)
        {
            loadPattern(mCurrentPattern, mHighlightMode);
        }
    }

    //
    // Accessors / Mutators
    //

    public void setPattern(List<Point> pattern)
    {
        clearPattern(mCurrentPattern);
        loadPattern(pattern, mHighlightMode);

        mCurrentPattern = pattern;
    }
    public List<Point> getPattern()
    {  
    	return mCurrentPattern;
    }

    public void setGridLength(int length)
    {
        mLengthNodes = length;
        mCurrentPattern = Collections.emptyList();
        buildDrawables();
    }
    public int getGridLength()
    {
        return mLengthNodes;
    }

    public void setHighlightMode(HighlightMode mode)
    {
        setHighlightMode(mode, mPracticeMode);
    }
    public void setHighlightMode(HighlightMode mode, boolean suppressRepaint)
    {
        mHighlightMode = mode;
        if(!suppressRepaint)
        {
            loadPattern(mCurrentPattern, mHighlightMode);
        }
    }
    public HighlightMode getHighlightMode()
    {
        return mHighlightMode;
    }

    public void setPracticeMode(boolean mode)
    {
        mDisplayingPracticeResult = false;
        mPracticeMode = mode;
        if(mode)
        {
            mPracticePattern = new ArrayList<Point>();
            mPracticePool = new HashSet<Point>();
            clearPattern(mCurrentPattern);
        }
        else
        {
            clearPattern(mPracticePattern);
            loadPattern(mCurrentPattern, mHighlightMode);
           
        }
    }
    public boolean getPracticeMode()
    {
        return mPracticeMode;
    }

    public void setTactileFeedbackEnabled(boolean enabled)
    {
        mTactileFeedback = enabled;
    }
    public boolean getTactileFeedbackEnabled()
    {
        return mTactileFeedback;
    }

    //
    // Inner classes
    //

    private class CenterIterator implements Iterator<Point>
    {
        private Iterator<Point> nodeIterator;

        public CenterIterator(Iterator<Point> iterator)
        {
            nodeIterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public Point next() {
            Point node = nodeIterator.next();
            return mNodeDrawables[node.x][node.y].getCenter();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

 // Interface for choosing what state to put a node in based on its position
    // in the pattern, allowing for things like highlighting the first node etc.
    public interface HighlightMode
    {
        int select(NodeDrawable1 node, int patternIndex, int patternLength,
                   int nodeX, int nodeY, int gridLength);
    }

    public static class NoHighlight implements HighlightMode
    {
        @Override
        public int select(NodeDrawable1 node, int patternIndex,
                int patternLength, int nodeX, int nodeY, int gridLength)
        {
            return NodeDrawable1.STATE_SELECTED;
        }
    }
    public static class FirstHighlight implements HighlightMode
    {
        @Override
        public int select(NodeDrawable1 node, int patternIndex,
                int patternLength, int nodeX, int nodeY, int gridLength)
        {
            if(patternIndex == 0)
            {
                return NodeDrawable1.STATE_HIGHLIGHTED;
            }
            return NodeDrawable1.STATE_SELECTED;
        }
    }
    public static class RainbowHighlight implements HighlightMode
    {
        @Override
        public int select(NodeDrawable1 node, int patternIndex,
                int patternLength, int nodeX, int nodeY, int gridLength)
        {
            float wheelPosition = ((float) patternIndex / (float) patternLength)
                * 360.0f;
            int color = Color.HSVToColor(
                    new float[] { wheelPosition, 1.0f, 1.0f });
            node.setCustomColor(color);

            return NodeDrawable1.STATE_CUSTOM;
        }
    }
   
    public static class SuccessHighlight implements HighlightMode
    {
        @Override
        public int select(NodeDrawable1 node, int patternIndex,
                int patternLength, int nodeX, int nodeY, int gridLength)
        {
        	mLinePaint.setColor(DEFAULT_PATTERN_GREEN_LINE_COLOR);
        	mLinePaint.setAlpha(100);
            return NodeDrawable1.STATE_CORRECT;
        }
    }
    
    public static class FailureHighlight implements HighlightMode
    {
        @Override
        public int select(NodeDrawable1 node, int patternIndex,
                int patternLength, int nodeX, int nodeY, int gridLength)
        {
        	mLinePaint.setColor(DEFAULT_PATTERN_RED_LINE_COLOR);
        	mLinePaint.setAlpha(100);
            return NodeDrawable1.STATE_INCORRECT;           
        }
    }
}