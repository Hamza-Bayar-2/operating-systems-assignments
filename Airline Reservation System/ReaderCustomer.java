import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;

public class ReaderCustomer extends Thread{
  private DataBase dataBase;
  private final Queue<String> requestQueue = new LinkedList<>();
  private final Lock lock;


  public ReaderCustomer(DataBase dataBase, Lock lock, String threadName) {
    this.dataBase = dataBase;
    this.lock = lock;
    setName(threadName);
  }

  public synchronized void requestQueryReservation() {
      requestQueue.add("query");
      notify();
  }

  private void queryReservation() {
      try {
        lock.lock();
        System.out.println(getName()
        + " is checking the seats:");
        dataBase.printSeatsAndStates();
        System.out.println("");
      } finally {
        lock.unlock();
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