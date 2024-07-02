import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReservationSystem {
  public static void main(String[] args) {
    int readerAmount = 5;
    DataBase dataBase = new DataBase(6, "istanbul");
    ReaderCustomer[] readerCustomersArray = new ReaderCustomer[readerAmount];
    final Lock lock = new ReentrantLock();

    readerCustomersArray = readerCustomerCreater(readerAmount, lock, dataBase);

    for (ReaderCustomer readerCustomer : readerCustomersArray) {
      readerCustomer.start();
    }

    WriterCustomer writerCustomer0 = new WriterCustomer(dataBase, lock,  "writer-0");
    WriterCustomer writerCustomer1 = new WriterCustomer(dataBase, lock,  "writer-1");
    WriterCustomer writerCustomer2 = new WriterCustomer(dataBase, lock,  "writer-2");
    writerCustomer0.start();
    writerCustomer1.start();
    writerCustomer2.start();

    System.out.println("\n===============> Flight Name: " + dataBase.getFlightName().toUpperCase() + " <===============\n\n");

    writerCustomer0.requestMakeReservation("seat4");
    writerCustomer1.requestMakeReservation("seat1");
    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestMakeReservation("seat2");
    writerCustomer1.requestMakeReservation("seat3");

    readerCustomersArray[0].requestQueryReservation();
    readerCustomersArray[2].requestQueryReservation();

    writerCustomer0.requestCancelReservation("seat3");
    writerCustomer2.requestMakeReservation("seat3");
    writerCustomer1.requestMakeReservation("seat2");
    writerCustomer2.requestMakeReservation("seat3");
    writerCustomer2.requestMakeReservation("seat3");
    writerCustomer1.requestCancelReservation("seat3");

    readerCustomersArray[3].requestQueryReservation();
    readerCustomersArray[4].requestQueryReservation();

    writerCustomer0.requestMakeReservation("seat2");
    writerCustomer0.requestMakeReservation("seat2");
    writerCustomer1.requestMakeReservation("seat2");
    writerCustomer1.requestCancelReservation("seat2");

    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestCancelReservation("seat3");
    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestMakeReservation("seat3");
    readerCustomersArray[4].requestQueryReservation();
  }

  static ReaderCustomer[] readerCustomerCreater(int howMany, Lock lock, DataBase dataBase) {
    ReaderCustomer[] readerArray = new ReaderCustomer[howMany];
    for(int i = 0; i < howMany; i++){
      readerArray[i] = new ReaderCustomer(dataBase, lock, "reader-" + i);
    }

    return readerArray;
  } 
}