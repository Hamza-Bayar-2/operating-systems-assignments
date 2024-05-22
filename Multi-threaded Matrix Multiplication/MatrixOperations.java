import java.util.Random;

public class MatrixOperations {
  private int dimension, firstRange, secondRange;
  private Random random = new Random();

  // Construction to get the value from the user
  MatrixOperations(int dimension, int firstRange, int secondRange){
    this.dimension = dimension;
    this.firstRange = firstRange;
    this.secondRange = secondRange;
  }

  // Method to generate a random number in the given range
  private int randomNumber(int firstRange, int secondRange) {
    return random.nextInt(Math.abs(secondRange - firstRange) + 1) + firstRange;
  }

  // method to print the given matrix
  public void matrixPrinter(int[][] matrix, int dimension){
    System.out.println();
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        System.out.print(matrix[i][j] + " ");
      }
      System.out.println();
    }
  }
  
  // Method to fill the matrix with random numbers
  private int[][] matrixFiller() {
    int[][] matrix = new int[dimension][dimension];
    for (int i = 0; i < dimension; i++) {
      for (int j = 0; j < dimension; j++) {
        matrix[i][j] = randomNumber(firstRange, secondRange);
      }
    }
    return matrix;
  }

  // Method to return the filled matrix
  public int[][] creat() {
    return matrixFiller();
  }

}