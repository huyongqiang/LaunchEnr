package com.enrico.launcher3;

interface CustomAppWidget {
    String getLabel();
    int getPreviewImage();
    int getIcon();
    int getWidgetLayout();

    int getSpanX();
    int getSpanY();
    int getMinSpanX();
    int getMinSpanY();
    int getResizeMode();
}
