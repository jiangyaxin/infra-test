package com.jyx.feature.test.jdk.algorithm.ant;

import java.util.*;

/**
 * @author Archforce
 * @since 2022/12/22 16:49
 */
public class Grid {

    private final Map<Coordinate,Color> cellColor;

    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    public Grid(Coordinate initCoordinate) {
        this.cellColor = new HashMap<>();

        int x = initCoordinate.getX();
        int y = initCoordinate.getY();

        this.minX = x;
        this.minY = y;
        this.maxX = x;
        this.maxY = y;
    }

    public void expand(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        this.minX = Math.min(x, this.minX);
        this.minY = Math.min(y, this.minY);
        this.maxX = Math.max(x, this.maxX);
        this.maxY = Math.max(y, this.maxY);
    }

    public Color reverseColor(Coordinate coordinate) {
        Color originColor = getCellColor(coordinate);
        Color reversedColor = originColor.reverse();
        cellColor.put(coordinate,reversedColor);

        return originColor;
    }

    public List<String> print(Coordinate lastCoordinate,Direction lastDirection){
        List<String> result = new ArrayList<>();
        for(int y=maxY ; y>=minY ; y--){
            StringBuilder row = new StringBuilder();
            for(int x=minX ; x<=maxX ; x++){
                Coordinate coordinate = new Coordinate(x,y);
                Color color = getCellColor(coordinate);

                String cell = Objects.equals(coordinate, lastCoordinate) ? lastDirection.getValue() : color.getValue();
                row.append(cell);
            }
            result.add(row.toString());
        }
        return result;
    }

    private Color getCellColor(Coordinate coordinate) {
        return cellColor.getOrDefault(coordinate,Color.WHITE);
    }

}
