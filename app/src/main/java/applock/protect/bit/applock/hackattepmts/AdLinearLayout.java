package applock.protect.bit.applock.hackattepmts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import applock.protect.bit.applock.Security.SecurityLocksCommon;

public class AdLinearLayout extends FrameLayout{

    public AdLinearLayout(Context context) {
        super(context);
    }

    public AdLinearLayout(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        SecurityLocksCommon.IsAppDeactive = false;
        return super.dispatchTouchEvent(ev);
    }
}
