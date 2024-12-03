package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that calculates the sum of the elements that belongs
 * to a matrix using multithreading features.
 */
public class MultiThreadedSumMatrix implements SumMatrix {

    private final int nThreads;

    public MultiThreadedSumMatrix(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.nThreads = n;
    }

    /**
     * @param matrix
     *            an arbitrary-sized matrix
     * @return the sum of its elements
     */
    @Override
    public double sum(final double[][] matrix) {
        final int rowPerThread = (int) Math.ceil((double) matrix.length / nThreads);
        final List<WorkerMatrix> workers = new ArrayList<>(nThreads);
        for (int i = 0; i < nThreads; i++) {
            final int start = i * rowPerThread;
            final int endRow = Math.min(start + rowPerThread, matrix.length);
            workers.add(new WorkerMatrix(matrix, start, endRow));
        }
        for (final WorkerMatrix worker : workers) {
            worker.start();
        }
        double res = 0.0;
        for (final WorkerMatrix worker : workers) {
            try {
                worker.join();
                res += worker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return res;
    }

    /**
     * It creates threads that semplifies the calculation
     * by summing the result of each row.
     */
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

        /**
         * Run the worker.
         */
        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from row " + startRow + " to row" + endRow);
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    this.result += this.matrix[i][j];
                }
            }
        }

        /**
         * For getting the result.
         * @return the result of each row
         */
        private double getResult() {
            return this.result;
        }
    }
}
