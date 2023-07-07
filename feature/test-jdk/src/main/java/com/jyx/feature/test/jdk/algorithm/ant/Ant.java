package com.jyx.feature.test.jdk.algorithm.ant;

import java.util.List;

/**
 * @author JYX
 * @since 2021/1/29 9:20
 */
public class Ant {

    private Coordinate coordinate;
    private Direction direction;
    private final MoveCount moveCount;
    private final Grid grid;

    public Ant(int initX,int initY){
        this.coordinate = new Coordinate(initX,initY);
        this.direction = Direction.RIGHT;
        this.moveCount = new MoveCount();

        this.grid = new Grid(coordinate);
    }

    public void move(){
        Color originColor = grid.reverseColor(coordinate);
        Direction nextDirection = direction.rotate(originColor);
        Coordinate nextCoordinate = coordinate.moveOneStep(nextDirection);
        grid.expand(nextCoordinate);

        this.coordinate = nextCoordinate;
        this.direction = nextDirection;

        moveCount.addStep();
    }

    public boolean achieveGoal(int K){
        return this.moveCount.reach(K);
    }

    public List<String> printPath(){
        return this.grid.print(coordinate,direction);
    }
}
