package autostalker.bananaforscale.com.autostalker.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Emiliano on 01/10/2017.
 */

public class MyVideoView extends VideoView {

    public MyVideoView(Context context) {
        super(context);
    }

    public MyVideoView(Context context,AttributeSet attrs) {
        super(context,attrs);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension( (int)(width * 1) , (int)(height * 0.5));
    }
}
