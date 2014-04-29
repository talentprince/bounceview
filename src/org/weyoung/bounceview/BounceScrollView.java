package org.weyoung.bounceview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * Bounce View
 */
public class BounceScrollView extends ScrollView {
    private View inner;// Child View, Like ScrollListView

    private float y;// initial touch point

    private Rect normal = new Rect();// animation control object

    private boolean isCount = false;// counting flag

    private final static int DELTA_RADIO = 2;// bounce move radio

    public BounceScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * Must be override, save innver view object
     */
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0) {
            inner = getChildAt(0);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (inner != null) {
            commOnTouchEvent(ev);
        }

        return super.onTouchEvent(ev);
    }


    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_UP:
            // finger up
            if (isNeedAnimation()) {
                animation();
                isCount = false;
            }
            break;
        /***
         * y is a anchor point initialized when first move
         * first touch event will be blocked by listview's item
         */
        case MotionEvent.ACTION_MOVE:
            final float preY = y;// anchor point
            float nowY = ev.getY();// runtime y coordinate
            int deltaY = (int) (preY - nowY);// move delta distance
            if (!isCount) {
                deltaY = 0; // finger up we clean delta
            }

            y = nowY;
            // when child view meet its boundary, we move it
            if (isNeedMove()) {
                // we save its boundary position
                if (normal.isEmpty()) {
                    normal.set(inner.getLeft(), inner.getTop(),
                            inner.getRight(), inner.getBottom());
                }
                // move child view
                inner.layout(inner.getLeft(), inner.getTop() - deltaY / DELTA_RADIO,
                        inner.getRight(), inner.getBottom() - deltaY / DELTA_RADIO);
            }
            isCount = true;
            break;

        default:
            break;
        }
    }

    /***
     * bounce animation
     */
    public void animation() {
        // build bounce animation
        TranslateAnimation ta = new TranslateAnimation(0, 0, inner.getTop(),
                normal.top);
        ta.setDuration(200);
        inner.startAnimation(ta);
        // set child view to normal place
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);

        normal.setEmpty();
    }

    // need animation?
    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    /***
     * Need to layout child view?
     * Only move when we meet top and bottom
     * 
     */
    public boolean isNeedMove() {
        int offset = inner.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        // 0 means top and offset means bottom
        if (scrollY == 0 || scrollY == offset) {
            return true;
        }
        return false;
    }

}