package com.shyz.clean.view.shortvideo;

public class CleanRipple {

    public int innerHalfHeight;
    public int innerHalfWidth;

    public int outerHalfHeight;
    public int outerHalfWidth;

    public int alpha;

    public int verticalDecrement;
    public int horizotalDecrement;


    public void nextFrame() {
        innerHalfWidth += horizotalDecrement;
        innerHalfHeight += verticalDecrement;
        outerHalfWidth +=horizotalDecrement;
        outerHalfHeight += verticalDecrement;
        alpha -= 4;
    }

    public void nextAlpha(int maxAlpha,int maxHalWidth,int defaultWidth){
        int deltaX = maxHalWidth - defaultWidth;
        int currentX = outerHalfWidth - defaultWidth;
        float p = currentX / Float.valueOf(deltaX);
        alpha = (int)(maxAlpha * (1- p));
    }

}
