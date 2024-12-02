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
        private double result;

        WorkerMatrix(final double[][] matrix, final int y, final int x) {
            super();
            this.matrix = matrix;
            this.x = x;
            this.y = y;
        }

        @Override
        public void run() {

        }
    }
}
