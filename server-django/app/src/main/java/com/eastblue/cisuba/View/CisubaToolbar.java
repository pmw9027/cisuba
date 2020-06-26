package com.eastblue.cisuba.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

/**
 * Created by PJC on 2017-02-05.
 */

public class CisubaToolbar extends Toolbar {

    public CisubaToolbar(Context context) {
        super(context);
    }

    public CisubaToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        setTitle("");
    }
}
