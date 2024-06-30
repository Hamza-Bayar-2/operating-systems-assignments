import java.util.LinkedHashMap;
import java.util.Map;

public class DataBase {
  // string => id, String => name of the passenger
  private Map<String, String> seatsMap = new LinkedHashMap<>();
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
      seatsMap.put("seat" + i, "empty");
    }
  }

  private String seatState(String passengerName) {
    if(passengerName.equals("empty")) {
      return "available";
    } else if(passengerName.contains("writer")) {
      return "occupied";
    } else {
      return "out of order";
    }
  }

  public void printSeatsAndStates() {
    // printing the seats and their states
    seatsMap.forEach((seatId, passengerName) -> {
      if(seatState(passengerName).equals("available")) {
        System.out.println(seatId + " => sate:" + " (" + seatState(passengerName) + ")");
      } else if(seatState(passengerName).equals("occupied")) {
        System.out.println(seatId + " => sate:" + " (" + seatState(passengerName) + ")" 
        + " / Passenger: " + passengerName);
      }
    });
  }

  public void changeSeatState(String seatId, String passengerName) {
    if(seatsMap.containsKey(seatId)) {
      seatsMap.put(seatId, passengerName);
    }
  }

  public String getFlightName() {
    return this.flightName;
  }

  public String getpassengerName(String seatId) {
    return seatsMap.get(seatId);
  }
}
