package com.agamir.domino;

public class Domino {

    private String src;
    private String top, bottom;
    private String left, right;
    private double rotate;

    public Domino(String src, String top, String bottom) {
        this.src = src;
        this.top = top;
        this.bottom = bottom;

        rotate = 90;

        left = bottom;
        right = top;
    }

    public String getSrc() {
        return src;
    }

    public double getRotate() {
        return rotate;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public boolean hasSame(String l,String r){
        if (left.equals("0")|| right.equals("0")) {
            return true;
        }
        if (left.equals(l) || right.equals(l) || left.equals(r) || right.equals(r)) {
            return true;
        }
        return false;
    }
    public boolean isCanAddLeft(String target) {
        if (right.equals(target) || right.equals("0")) {
            return true;
        }
        if (left.equals(target) || left.equals("0")) {
            rotate = -90;
            this.left = top;
            this.right = bottom;
            return true;
        }
        return false;
    }

    public boolean isCanAddRight(String target) {
        if (left.equals(target)||left.equals("0")) {
            return true;
        }
        if (right.equals(target) || right.equals("0" )) {
            rotate = -90;
            this.left = top;
            this.right = bottom;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Domino{" + "src=" + src + ", top=" + top + ", bottom=" + bottom + ", left=" + left + ", right=" + right + '}';
    }
}
