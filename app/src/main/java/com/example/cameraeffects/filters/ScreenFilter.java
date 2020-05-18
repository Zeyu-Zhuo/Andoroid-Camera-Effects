package com.example.cameraeffects.filters;

import android.content.Context;

public class ScreenFilter extends AbstractFilter {
    public ScreenFilter(Context context) {
        super(context,"vertex.vert","fragment.frag");
    }

    @Override
    protected void initCoordinate() {
    }
}
