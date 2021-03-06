package com.example.emersontenorio.fredschedulebuilder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by Emerson Tenorio on 12/16/2014.
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {
    private HorizontalScrollViewListener scrollViewListener=null;

    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(HorizontalScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (null!=scrollViewListener) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }
}
