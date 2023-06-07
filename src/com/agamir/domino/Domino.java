package com.agamir.domino;

public class Domino {
    private String imageSrc;
    private String topValue, bottomValue; //cached rotate 0 domino values
    private String leftValue, rightValue; //new values for rotated domino
    private double rotate;

    public Domino(String src, String topValue, String bottomValue) {
        this.imageSrc = src;
        this.topValue = topValue;
        this.bottomValue = bottomValue;

        rotate = 90;

        leftValue = bottomValue;
        rightValue = topValue;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public double getRotate() {
        return rotate;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public boolean hasSameValue(String l, String r) {
        if (leftValue.equals("0")|| rightValue.equals("0")) {
            return true;
        }
        if (leftValue.equals(l) || rightValue.equals(l) || leftValue.equals(r) || rightValue.equals(r)) {
            return true;
        }
        return false;
    }

    public boolean isCanAddLeft(String target) {
        if (rightValue.equals(target) || rightValue.equals("0")) {
            return true;
        }

        if (leftValue.equals(target) || leftValue.equals("0")) {
            rotate = -90;
            leftValue = topValue;
            rightValue = bottomValue;
            return true;
        }
        return false;
    }

    public boolean isCanAddRight(String target) {
        if (leftValue.equals(target)|| leftValue.equals("0")) {
            return true;
        }
        if (rightValue.equals(target) || rightValue.equals("0" )) {
            rotate = -90;
            leftValue = topValue;
            rightValue = bottomValue;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Domino{" + "src=" + imageSrc + ", top=" + topValue + ", bottom=" + bottomValue + ", left=" + leftValue + ", right=" + rightValue + '}';
    }
}
