import java.util.LinkedList;
import java.util.Queue;

public class ReaderCustomer extends Thread{
  private DataBase dataBase;
  private final Queue<String> requestQueue = new LinkedList<>();

  public ReaderCustomer(DataBase dataBase, String threadName) {
    this.dataBase = dataBase;
    setName(threadName);
  }

  public void requestQueryReservation() {
    synchronized (this) {
      requestQueue.add("query");
      notify();
    }
  }

  private void queryReservation() {
    synchronized(dataBase) {
      System.out.println(getName() + " is checking the seats:");
      dataBase.printSeatsAndStates();
      System.out.println("");
    }
  }

  @Override
  public void run() {
    while (true) {
      synchronized (this) {
        while (requestQueue.isEmpty()) {
          try {
            wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        requestQueue.poll();
      }
      queryReservation();
    }
  }
}