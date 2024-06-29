import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase {
  // string => id, Integer => 0: available 1: occupied 2: in process.
  private Map<String, Integer> seatsMap = new LinkedHashMap<>();
  // how many seat does the plane has.
  private int seatAmount;
  // which city does the plane flys
  private String flightName;

  public DataBase(int seatAmount, String flightName) {
    this.seatAmount = seatAmount;
    this.flightName = flightName;
    setTheInitialSeatsMap();
  }

  private void setTheInitialSeatsMap() {
    for(int i = 0; i < seatAmount; i++) {
      seatsMap.put("seat" + i, 0);
    }
  }

  private String seatState(int seatState) {
    if(seatState == 0) {
      return "available";
    } else if(seatState == 1) {
      return "occupied";
    } else if(seatState == 2) {
      return "in process";
    } else {
      return "out of order";
    }
  }

  public void printSeatsAndStates() {
      // printing the seats and their states
      seatsMap.forEach((seatId, seatState) -> {
        System.out.println(seatId + " => sate: " + seatState + " (" + seatState(seatState) + ")");
    });
  }
  public void printWantedSeatAndState(String seatId) {
    // printing the seat and its state
    System.out.println(seatId + " => sate: " + seatsMap.get(seatId) + " (" + seatState(seatsMap.get(seatId)) + ")");
  }
  // seatState => 0: available 1: occupied 2: in process.
  public void changeSeatState(String seatId, Integer seatState) {
    if(seatsMap.containsKey(seatId)) {
      seatsMap.put(seatId, seatState);
    }
  }


  public int getSeatAmount() {
    return this.seatAmount;
  }

  public String getFlightName() {
    return this.flightName;
  }

  public int getSeatStateInInt(String seatId) {
    return (int) seatsMap.get(seatId);
  }
}
