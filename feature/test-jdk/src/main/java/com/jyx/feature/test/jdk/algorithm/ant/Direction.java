package com.jyx.feature.test.jdk.algorithm.ant;

/**
 * @author jiangyaxin
 * @since 2021/1/29 9:33
 */
public enum Direction {

    UP("U"),
    RIGHT("R"),
    DOWN("D"),
    LEFT("L");

    private final String value;

    Direction(String value){
        this.value = value;
    }

    public Direction rotate(Color color){
        if(color.isBlack()){
            return Direction.values()[(this.ordinal()+4-1)%4];
        }
        if(color.isWhite()){
            return Direction.values()[(this.ordinal()+1)%4];
        }
        throw new TypeNotPresentException(Color.BLACK.getValue(),new RuntimeException("The color don't has rotate method"));
    }

    public String getValue() {
        return value;
    }
}
