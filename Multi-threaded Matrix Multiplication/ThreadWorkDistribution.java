public class ThreadWorkDistribution extends Thread{
  private int threadNumber, dimenison, workPerThread, threadsAmount;
  int[][] matrix1, matrix2, matrixConclusion;

  // The matrixs are created here so there is no need to fetch the matrixs everytime for each thread 
  public ThreadWorkDistribution(int[][] matrix1, int[][] matrix2, int[][] matrixConclusion, int workPerThread, int threadNumber, int dimension, int threadsAmount) {
    this.matrix1 = matrix1;
    this.matrix2 = matrix2;
    this.matrixConclusion = matrixConclusion;
    this.workPerThread = workPerThread;
    this.threadNumber = threadNumber;
    this.dimenison = dimension;
    this.threadsAmount = threadsAmount;
  }
  
  public void run(){
    int startRow, endRow;

    if(threadNumber < threadsAmount - 1) {
      startRow = workPerThread * threadNumber;
      // start is included, so we sub 1 (-1)
      endRow = startRow + workPerThread - 1;
    } 
    // else is the last thread.
    // After the work is distributed equally to the threads, 
    // the remaining work will be assigned to the last thread separately, 
    // as the remaining work may not be the same as the workPerThread.
    else { 
      startRow = workPerThread * threadNumber;
      endRow = startRow + remainWork(threadsAmount, workPerThread, dimenison) - 1;
    }

    // This loop decide how many rows will be given to the thread to fill it with the result of the multiplication
    // The endRow is in the range (included) so we wrote "<="
    for (int i = startRow; i <= endRow; i++) {
      fillMatrixRow(matrixConclusion, i);
    }
  }

  // This method fills the matrixConclusion's rows with the result of the matrixs multiplication
  // Synchronized is used here to avoid conflict when the threads try to change matrixConclusion
  // becouse matrixConclusion is reachable by all threads
  private void fillMatrixRow(int[][] matrixConc, int row) {
    synchronized (matrixConc[row]) {
      for (int col = 0; col < dimenison; col++) {
        matrixConc[row][col] = rowAndColMultiplication(row, col);
      }
    }
  }

  // This method multiplies the given row's values and the col's values
  // Afterword the method sums all the values and returns it
  // This method returns just one value to the matrixConclusion. 
  // For instance the method will return the value of matrixConclusion[0][0]
  private int rowAndColMultiplication(int row, int col) {
    int conclusion = 0;
    for (int i = 0; i < dimenison; i++) {
      conclusion += (matrix1[row][i] * matrix2[i][col]);
    }
    return conclusion;
  }

  // This method calculate the remain work by multiply the threads (except the last thread) by the workPerThread
  // and subtruct it from the totalWork (dimension)
  // total work is equel to the dimenison
  private static int remainWork(int threadsAmount, int workPerThread, int totalWork) {
    return (totalWork - ((threadsAmount - 1) * workPerThread));
  }
}
