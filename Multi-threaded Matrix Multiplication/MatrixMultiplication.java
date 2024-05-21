import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixMultiplication {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    List<Thread> threads = new ArrayList<>();
     
    int dimension = getValidIntInput(scanner, "Enter the dimension of the matrix: ");
    int firstRange = getValidIntInput(scanner, "Enter the first range value: ");
    int secondRange = getValidIntInput(scanner, "Enter the second range value: ");
    int threadsAmount = getValidIntInput(scanner, "Enter the thread amount: ");

    // This control is made for perevent creating unused threads.
    // Becouse the max threads we need for the multiplication is equel to the dimenison.
    if(threadsAmount > dimension){
      threadsAmount = dimension;
    }

    // This formula gives approximately the work per thread.
    int workPerThread = (int) Math.round(dimension / (double)threadsAmount);

    int[][] matrix1 = new int[dimension][dimension];
    int[][] matrix2 = new int[dimension][dimension];
    int[][] matrixConclusion = new int[dimension][dimension];
    MatrixOperations matrixOperations = new MatrixOperations(dimension, firstRange, secondRange);
    matrix1 = matrixOperations.creat();
    matrix2 = matrixOperations.creat();

    // The last thread is created after the loop
    for(int threadNumber = 0; threadNumber < threadsAmount; threadNumber++) {
      ThreadWorkDistribution thread = new ThreadWorkDistribution(matrix1, matrix2, matrixConclusion, workPerThread, threadNumber, dimension, threadsAmount);
      thread.start();
      threads.add(thread);
    }

    // This loop helps the main thread to wait all the threads to finish their work
    for (Thread th : threads) {
      try {
          th.join();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }

    matrixOperations.matrixPrinter(matrix1, dimension);
    System.out.println();
    matrixOperations.matrixPrinter(matrix2, dimension);
    System.out.println();
    matrixOperations.matrixPrinter(matrixConclusion, dimension);

    scanner.close();
  }

  // Method to check if the entry is valid or not
  // And scannes the value until it is valid
  private static int getValidIntInput(Scanner scanner, String entry) {
    System.out.print(entry);
    while (!scanner.hasNextInt()) {
      System.out.println("Invalid input. Please enter a valid number.");
      scanner.next(); // Clears the invalid input
      System.out.print(entry);
    }
    return scanner.nextInt();
  }
}