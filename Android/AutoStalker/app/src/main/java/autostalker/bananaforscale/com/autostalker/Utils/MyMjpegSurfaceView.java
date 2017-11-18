package autostalker.bananaforscale.com.autostalker.Utils;

import android.content.Context;
import android.util.AttributeSet;

import com.github.niqdev.mjpeg.MjpegInputStream;
import com.github.niqdev.mjpeg.MjpegSurfaceView;

import org.w3c.dom.Attr;

/**
 * Created by Emiliano on 16/10/2017.
 */

public class MyMjpegSurfaceView extends MjpegSurfaceView implements Cloneable {
    private AttributeSet atts;
    private MjpegInputStream stream;

    public MyMjpegSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);

        setMeasuredDimension((int) (width * 1), (int) (height * 0.65));
    }

    @Override
    public void setSource(MjpegInputStream stream){
        super.setSource(stream);

    }

    @Override
    public MyMjpegSurfaceView clone() {
        MyMjpegSurfaceView obj = new MyMjpegSurfaceView(this.getContext(), this.atts);
        obj.setSource(this.stream);
        return obj;
    }


}
