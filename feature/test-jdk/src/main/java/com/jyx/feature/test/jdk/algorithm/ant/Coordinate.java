package com.jyx.feature.test.jdk.algorithm.ant;

import java.util.Objects;

/**
 * @author JYX
 * @since 2021/1/29 10:02
 */
public class Coordinate{

    private final int x;
    private final int y;

    public Coordinate(int x,int y){
        this.x = x;
        this.y = y;
    }

    public Coordinate moveOneStep(Direction direction){
        Coordinate coordinate;
        switch (direction){
            case UP:
                coordinate = new Coordinate(x,y+1);
                break;
            case RIGHT:
                coordinate = new Coordinate(x+1,y);
                break;
            case DOWN:
                coordinate = new Coordinate(x,y-1);
                break;
            case LEFT:
                coordinate = new Coordinate(x-1,y);
                break;
            default:
                throw new TypeNotPresentException(direction.getValue(),new RuntimeException("The direction don't has change method"));
        }
        return coordinate;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Coordinate that = (Coordinate) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
