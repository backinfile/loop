package com.backinfile.loop.support;

import com.badlogic.gdx.graphics.Pixmap;

public class DrawUtils {
    public static void drawX(Pixmap pixmap, int width, int height, float persent) {
        int startX = Math.round(width * persent);
        int startY = Math.round(height * persent);
        pixmap.drawLine(startX, startY, width - startX, height - startY);
        pixmap.drawLine(width - startX, startY, startX, height - startY);
    }

    public static void drawLine(Pixmap pixmap, int startX, int startY, int endX, int endY, int width) {
        int halfWidth = Math.round(width / 2f);
        if (startX == endX) {
            pixmap.fillRectangle(startX - halfWidth, startY, endX + halfWidth, endY);
        } else {
            pixmap.fillRectangle(startX, startY - halfWidth, endX, endY + halfWidth);
        }
    }

    public static void drawBorder(Pixmap pixmap, int lineWidth) {
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        pixmap.fillRectangle(0, 0, lineWidth, height);
        pixmap.fillRectangle(0, 0, width, lineWidth);
        pixmap.fillRectangle(0, height - lineWidth, width, height);
        pixmap.fillRectangle(width - lineWidth, 0, width, height);
    }
}
