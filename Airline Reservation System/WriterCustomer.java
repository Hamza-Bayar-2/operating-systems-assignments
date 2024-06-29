import java.util.LinkedList;
import java.util.Queue;

public class WriterCustomer extends Thread{
  private final  DataBase dataBase;
  private String seatId;
  private final Queue<String> requestQueue = new LinkedList<>();

  public WriterCustomer(DataBase dataBase, String threadName) {
    this.dataBase = dataBase;
    setName(threadName);
  }

  public synchronized void requestMakeReservation(String seatId) {
    this.seatId = seatId;
    requestQueue.add("makeResv");
    notify();
  }

  public synchronized void requestCancelReservation(String seatId) {
    this.seatId = seatId;
    requestQueue.add("cancelResv");
    notify();
  }

  private void makeReservation() {
    synchronized (dataBase) {
      if(dataBase.getSeatStateInInt(seatId) != 1) {
        dataBase.changeSeatState(seatId, 1);
        System.out.println(getName() + " made reservation for " + seatId + "\n");
      } else {
        System.out.println(getName() + " could not made a reservation for " + seatId + " because it is already taken\n");
      }
    }
  }

  private void cancelReservation() {
    synchronized (dataBase) {
      if(dataBase.getSeatStateInInt(seatId) != 0) {
        dataBase.changeSeatState(seatId, 0);
        System.out.println(getName() + " canceled reservation for " + seatId + "\n");
      } else {
        System.out.println(getName() + " could not cancele the reservation for " + seatId + " because the writer dose not have a reservation for that seat.\n");
      }

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
        if(request.equals("makeResv")) {
          makeReservation();
        } else if(request.equals("cancelResv")) {
          cancelReservation();
        }
      }
    }
  }
}
