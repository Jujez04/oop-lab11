package it.unibo.oop.workers02;

public class MultiThreadedSumMatrix implements SumMatrix{

    private final int nThreads;

    public MultiThreadedSumMatrix(final int n) {
        if(n < 0) {
            throw new IllegalArgumentException();
        } 
        this.nThreads = n;
    }

    @Override
    public double sum(double[][] matrix) {

        
        return 0.0;
    }
    
    private class WorkerMatrix extends Thread {

        private final double[][] matrix;
        private final int y;
        private final int x;
        private final int xElem;
        private final int yElem;
        private double result;

        WorkerMatrix(
            final double[][] matrix, 
            final int y, 
            final int x,
            final int xElem,
            final int yElem
            ) {
            super();
            this.matrix = matrix;
            this.x = x;
            this.y = y;
            this.xElem = xElem;
            this.yElem = yElem;
        }

        @Override
        public void run() {
            for(int i = x; i < matrix.length && i < x + xElem; i++) {
                for(int j = y; j < matrix[i].length && j < y + yElem; j++) {
                    this.result += this.matrix[i][j];
                }
            }
        }

        private double getResult() {
            return this.result;
        }
    }
}
