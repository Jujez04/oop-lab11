package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

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
        final int rowPerThread = matrix.length > nThreads ? matrix.length / nThreads : 1;
        final List<WorkerMatrix> workers = new ArrayList<>(nThreads);
        for(int i = 0; i < nThreads; i++) {
            final int start = i * rowPerThread;
            workers.add(new WorkerMatrix(matrix, start, start + rowPerThread));
        }
        for(final WorkerMatrix worker : workers) {
            worker.start();
        }
        double res = 0.0;
        for(final WorkerMatrix worker : workers) {
            try {
                worker.join();
                res += worker.getResult();
            } catch (Exception e) {
                throw new IllegalStateException();
            }
        }

        return res;
    }
    
    private class WorkerMatrix extends Thread {

        private final double[][] matrix;
        private final int startRow;
        private final int endRow;
        private double result;

        WorkerMatrix(final double[][] matrix, final int startRow, final int endRow) {
            super();
            this.matrix = matrix;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            for(int i = startRow; i < matrix.length && i < endRow; i++) {
                for(int j = 0; j < matrix[i].length; j++) {
                    this.result += this.matrix[i][j];
                }
            }
        }

        private double getResult() {
            return this.result;
        }
    }
}
