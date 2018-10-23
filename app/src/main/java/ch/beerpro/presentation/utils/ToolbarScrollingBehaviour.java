package ch.beerpro.presentation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;
import ch.beerpro.R;

import static android.content.Context.MODE_PRIVATE;

public class ToolbarScrollingBehaviour<V extends View> extends CoordinatorLayout.Behavior<V> {

    private static final String TAG = "ToolbarScrollingBehavio";
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";

    public ToolbarScrollingBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target,
                                  int dx, int dy, @NonNull int[] consumed, int type) {
        SharedPreferences preferences = coordinatorLayout.getContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        LinearLayout toolbarLayout = (LinearLayout) child;
        View statusBar = toolbarLayout.getChildAt(0);
        Toolbar toolbar = (Toolbar) toolbarLayout.getChildAt(1);

        int toolbarHeight = toolbarLayout.getHeight();

        int scrollY = target.getScrollY() + dy;
        float alpha = (scrollY - (toolbarHeight / 2f)) / (1.0f * toolbarHeight);

        Log.i(TAG, "target.scrollY: " + target.getScrollY());
        Log.i(TAG, "dy: " + dy);
        Log.i(TAG, "ScrollY: " + scrollY);
        Log.i(TAG, "Alpha: " + alpha);

        Resources resources = child.getResources();

        int newToolbarColor;
        int newToolbarTextColor;
        int newStatusbarColor;
        if (useDarkTheme) {
            newToolbarColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.darkColorPrimary),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
            newToolbarTextColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.darkColorAccent),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
            newStatusbarColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.darkColorPrimaryDark),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
        } else {
            newToolbarColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.colorPrimary),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
            newToolbarTextColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.colorAccent),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
            newStatusbarColor = ColorUtils.setAlphaComponent(resources.getColor(R.color.colorPrimaryDark),
                    Math.max(Math.min((int) (255 * alpha), 255), 0));
        }
        toolbarLayout.setBackgroundColor(newToolbarColor);
        toolbar.setTitleTextColor(newToolbarTextColor);
        statusBar.setBackgroundColor(newStatusbarColor);
    }
}