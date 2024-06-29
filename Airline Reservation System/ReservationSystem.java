
public class ReservationSystem {
  public static void main(String[] args) {
    DataBase dataBase = new DataBase(6, "istanbul");
    ReaderCustomer readerCustomer0 = new ReaderCustomer(dataBase, "reader-0");
    ReaderCustomer readerCustomer1 = new ReaderCustomer(dataBase, "reader-1");
    ReaderCustomer readerCustomer2 = new ReaderCustomer(dataBase, "reader-2");
    ReaderCustomer readerCustomer3 = new ReaderCustomer(dataBase, "reader-3");
    WriterCustomer writerCustomer0 = new WriterCustomer(dataBase, "writer-0");


    readerCustomer0.start();
    readerCustomer1.start();
    readerCustomer2.start();
    readerCustomer3.start();
    writerCustomer0.start();

    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestMakeReservation("seat3");
    writerCustomer0.requestCancelReservation("seat3");

    readerCustomer3.requestQueryReservation();
    readerCustomer3.requestQueryReservation();
    readerCustomer2.requestQueryReservation();
    readerCustomer1.requestQueryReservation();


  }
}