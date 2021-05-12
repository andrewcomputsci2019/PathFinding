package com.company;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class PathFindingAlgo {
    public boolean[][] visitedArray;
    ArrayList<boolean[][]> timeStamps;

    /**
     * BFS algo does not need an endpoint like a star but less efficient since it does take advantage of a priority
     * @param source 2d array
     * @param initialPosition the source in which you want to start at
     * @return the distance it took to get to the end goal
     */
        public int pathFinding(int[][] source, AbstractMap.SimpleEntry<Integer,Integer> initialPosition){
            timeStamps=new ArrayList<>();
            CurrentPostion start = new CurrentPostion(initialPosition.getKey(), initialPosition.getValue(), 0);
            Queue<CurrentPostion> queue = new LinkedList<>();
            queue.add(new CurrentPostion(start.row, start.col, 0));
            visitedArray = new boolean[source.length][source[0].length];
            visitedArray[start.row][start.col] = true;
            while (!queue.isEmpty()){
                CurrentPostion position = queue.remove();
                if (source[position.row][position.col]==2){
                    return position.dist;
                }
                if (validMove(position.row - 1, position.col, source)) {
                    queue.add(new CurrentPostion(position.row - 1, position.col,
                            position.dist + 1));
                    visitedArray[position.row - 1][position.col] = true;
                }

                // moving down
                if (validMove(position.row + 1, position.col, source)) {
                    queue.add(new CurrentPostion(position.row + 1, position.col,
                            position.dist + 1));
                    visitedArray[position.row + 1][position.col] = true;
                }

                // moving left
                if (validMove(position.row, position.col - 1, source)) {
                    queue.add(new CurrentPostion(position.row, position.col - 1,
                            position.dist + 1));
                    visitedArray[position.row][position.col - 1] = true;
                }

                // moving right
                if (validMove(position.row, position.col + 1, source)) {
                    queue.add(new CurrentPostion(position.row, position.col + 1,
                            position.dist + 1));
                    visitedArray[position.row][position.col + 1] = true;
                }
                boolean[][] timestamp = new boolean[visitedArray.length][visitedArray[0].length];
                for (int i=0; i<timestamp.length; i++){
                    for (int j=0; j<timestamp[i].length; j++){
                        timestamp[i][j] = visitedArray[i][j];
                    }
                }
                timeStamps.add(timestamp);//space inefficient
            }
                return -1;
        }
        private boolean validMove(int x, int y, int[][] source){
            return x >= 0 && y >= 0 && x < source.length && y < source[0].length && source[x][y] != -1 && !visitedArray[x][y];
        }

}
