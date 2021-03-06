package org.weyoung.bounceview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
 
public class ScrollListView extends ListView {
 
    public ScrollListView(Context context) {
        super(context);
    }
 
    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public ScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //give children view height as big as you can
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
 
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
 
}