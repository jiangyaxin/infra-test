package com.jyx.feature.test.jdk.algorithm.ant;

/**
 * @author JYX
 * @since 2021/1/29 9:27
 */
public enum Color {

    BLACK("X"),
    WHITE("_");

    private final String value;

    Color(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Color reverse(){
        return Color.values()[(this.ordinal()+1)%2];
    }

    public boolean isBlack(){
        return this == BLACK;
    }

    public boolean isWhite(){
        return this == WHITE;
    }
}
