import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class WriterCustomer extends Thread{
  private final  DataBase dataBase;
  private final Queue<String> seatIdQueue = new LinkedList<>();
  private final Queue<String> requestQueue = new LinkedList<>();
  private final Lock lock;

  public WriterCustomer(DataBase dataBase, Lock lock, String threadName) {
    this.dataBase = dataBase;
    this.lock = lock;
    setName(threadName);
  }

  public synchronized void requestMakeReservation(String seatId) {
    this.seatIdQueue.add(seatId);
    requestQueue.add("makeResv");
    notify();
  }

  public synchronized void requestCancelReservation(String seatId) {
    this.seatIdQueue.add(seatId);
    requestQueue.add("cancelResv");
    notify();
  }

  private void makeReservation() {
    try {
      lock.lock();
      // Simulate delay. if this delay is greater than the latch.await time 
      // the reservationTask thread will be interrupted.
      sleep(500);
      String seatId = seatIdQueue.poll();
      if(dataBase.getpassengerName(seatId).equals("empty")) {
        dataBase.changeSeatState(seatId, getName());
        System.out.println(getName() + " made reservation for " + seatId + "\n");
      } else if(dataBase.getpassengerName(seatId).equals(getName())) {
        System.out.println(getName() + " could not made a reservation for " 
        + seatId + " because it is already his seat\n");
      } else {
        System.out.println(getName() + " could not made a reservation for " + seatId 
        + " because it is already taken by " + dataBase.getpassengerName(seatId) + "\n");
      }
    } catch (InterruptedException e) {
    } finally {
      lock.unlock();
    }
  }

  private void cancelReservation() {
    try {
      lock.lock();
      // Simulate delay. if this delay is greater than the latch.await time 
      // the reservationTask thread will be interrupted.
      sleep(500);
      String seatId = seatIdQueue.poll();
      if(dataBase.getpassengerName(seatId).equals(getName())) {
        // Because passenger is canceling the reservation the passengerName will be empty
        dataBase.changeSeatState(seatId, "empty");
        System.out.println(getName() + " canceled reservation for " + seatId + "\n");
      } else {
        System.out.println(getName() + " could not cancele the reservation for " 
        + seatId + " because the writer dose not have a reservation for that seat.\n");
      }
    } catch (InterruptedException e) {
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void run() {
    while (true) {
      synchronized (this) {
        while(requestQueue.isEmpty()) {
          try {
            wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        String request = requestQueue.poll();

        CountDownLatch latch = new CountDownLatch(1);
        Runnable reservationTask = () -> {
          try {
            if (request.equals("makeResv")) {
              makeReservation();
            } else if (request.equals("cancelResv")) {
              cancelReservation();
            }
          } finally {
            latch.countDown();
          }
        };

        Thread thread = new Thread(reservationTask);
        thread.start();

        try {
          boolean completedInTime = latch.await(4, TimeUnit.SECONDS);
          if (!completedInTime) {
            if(request.equals("makeResv")) {
              System.out.println("\nThe reservation by " + getName() + " took too long, interrupting the process...\n");
            } else {
              System.out.println("\nThe cancellation of the reservation by " + getName() + " took too long interrupting the process...\n");
            }
            thread.interrupt();
          }
        } catch (InterruptedException e) {
          System.out.println("Main thread interrupted.");
        }
      }
    }
  }
}
