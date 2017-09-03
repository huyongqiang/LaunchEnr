package com.enrico.launcher3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.enrico.launcher3.allapps.AllAppsContainerView;

/**
 * A base container view, which supports resizing.
 */
public abstract class BaseContainerView extends FrameLayout
        implements DeviceProfile.LauncherLayoutChangeListener {

    protected final Drawable mBaseDrawable;
    protected int mContainerPaddingLeft;
    protected int mContainerPaddingRight;
    protected int mContainerPaddingTop;
    protected int mContainerPaddingBottom;
    private InsetDrawable mRevealDrawable;
    private View mRevealView;
    private View mContent;

    public BaseContainerView(Context context) {
        this(context, null);
    }

    public BaseContainerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (this instanceof AllAppsContainerView) {
            mBaseDrawable = new ColorDrawable();
        } else {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.BaseContainerView, defStyleAttr, 0);
            mBaseDrawable = a.getDrawable(R.styleable.BaseContainerView_revealBackground);
            a.recycle();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        DeviceProfile grid = Launcher.getLauncher(getContext()).getDeviceProfile();
        grid.addLauncherLayoutChangedListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        DeviceProfile grid = Launcher.getLauncher(getContext()).getDeviceProfile();
        grid.removeLauncherLayoutChangedListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mContent = findViewById(R.id.main_content);
        mRevealView = findViewById(R.id.reveal_view);

        updatePaddings();
    }

    @Override
    public void onLauncherLayoutChanged() {
        updatePaddings();
    }

    public void setRevealDrawableColor(int color) {
        ((ColorDrawable) mBaseDrawable).setColor(color);
    }

    public final View getContentView() {
        return mContent;
    }

    public final View getRevealView() {
        return mRevealView;
    }

    private void updatePaddings() {
        Context context = getContext();
        Launcher launcher = Launcher.getLauncher(context);

        if (this instanceof AllAppsContainerView &&
                !launcher.getDeviceProfile().isVerticalBarLayout()) {
            mContainerPaddingLeft = mContainerPaddingRight = 0;
            mContainerPaddingTop = mContainerPaddingBottom = 0;
        } else {
            DeviceProfile grid = launcher.getDeviceProfile();
            int[] padding = grid.getContainerPadding(context);
            mContainerPaddingLeft = padding[0] + grid.edgeMarginPx;
            mContainerPaddingRight = padding[1] + grid.edgeMarginPx;
            if (!launcher.getDeviceProfile().isVerticalBarLayout()) {
                mContainerPaddingTop = mContainerPaddingBottom = grid.edgeMarginPx;
            } else {
                mContainerPaddingTop = mContainerPaddingBottom = 0;
            }
        }

        mRevealDrawable = new InsetDrawable(mBaseDrawable,
                mContainerPaddingLeft, mContainerPaddingTop, mContainerPaddingRight,
                mContainerPaddingBottom);
        mRevealView.setBackground(mRevealDrawable);
        if (this instanceof AllAppsContainerView) {
            // Skip updating the content background
        } else {
            mContent.setBackground(mRevealDrawable);
        }
    }
}