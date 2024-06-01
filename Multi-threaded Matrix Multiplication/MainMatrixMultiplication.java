import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainMatrixMultiplication {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    List<Thread> threadsList = new ArrayList<>();
     
    int dimension = getValidIntInput(scanner, "Enter the dimension of the matrix: ", false);
    int threadsAmount = getValidIntInput(scanner, "Enter the thread amount: ", false);
    int firstRange = getValidIntInput(scanner, "Enter the first range value: ", true);
    int secondRange = getValidIntInput(scanner, "Enter the second range value: ", true);

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

    long startTime = System.nanoTime();

    // This loop creats the threads and add them to the threadList.
    for(int threadNumber = 0; threadNumber < threadsAmount; threadNumber++) {
      ThreadWorkDistribution thread = new ThreadWorkDistribution(matrix1, matrix2, matrixConclusion, workPerThread, threadNumber, dimension, threadsAmount);
      thread.start();
      threadsList.add(thread);
    }
    // This mehtod joins the thread
    threadJoiner(threadsList);

    long endTime = System.nanoTime();
    long duration = endTime - startTime;
    double durationInMilliseconds = (double) duration / 1_000_000.0;
    double durationInSeconds = (double) durationInMilliseconds / 1000.0;
    System.out.println("İşlem süresi (saniye saniye): " + durationInSeconds);

    // matrixOperations.matrixPrinter(matrix1, dimension);
    // matrixOperations.matrixPrinter(matrix2, dimension);
    // matrixOperations.matrixPrinter(matrixConclusion, dimension);

    scanner.close();
  }

  // Method to check if the entry is valid or not
  // and scannes the value until it is valid.
  // couldBeSmallerThanZero, if this is true that means the number could be less than zero
  // but if it is false the number can not be less than zero and if the number is less than zero
  // a new number will be asked agin.
  private static int getValidIntInput(Scanner scanner, String entry, boolean couldBeSmallerThanZero) {
    System.out.print(entry);
    while (true) {
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.next(); // Clears the invalid input
            System.out.print(entry);
        } else {
            int input = scanner.nextInt();
            if (!couldBeSmallerThanZero && input < 0) {
                System.out.println("Invalid input. The number cannot be less than zero.");
                System.out.print(entry);
            } else {
                return input;
            }
        }
    }
}

  // By looping the threadsList, all threads are joined. 
  // This loop helps the main thread to wait all the threads to finish their work.
  private static void threadJoiner(List<Thread> threadsList) {
    for (Thread th : threadsList) {
      try {
          th.join();
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
    }
  }


}