import java.util.*;

class Vehicle {
    String licensePlate;
    long entryTime;

    Vehicle(String licensePlate) {
        this.licensePlate = licensePlate;
        this.entryTime = System.currentTimeMillis();
    }
}

class ParkingSpot {
    Vehicle vehicle;
    boolean occupied;
    ParkingSpot() { this.vehicle = null; this.occupied = false; }
}

public class Problem_8 {

    private ParkingSpot[] spots;
    private int capacity;
    private int totalProbes = 0;
    private int parkedVehicles = 0;
    private HashMap<String, Integer> licenseToSpot = new HashMap<>();

    public Problem_8(int capacity) {
        this.capacity = capacity;
        spots = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) spots[i] = new ParkingSpot();
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    public synchronized void parkVehicle(String licensePlate) {
        int preferred = hash(licensePlate);
        int probe = 0;
        int spotIndex = preferred;
        while (spots[spotIndex].occupied) {
            probe++;
            spotIndex = (preferred + probe) % capacity;
        }
        spots[spotIndex].vehicle = new Vehicle(licensePlate);
        spots[spotIndex].occupied = true;
        licenseToSpot.put(licensePlate, spotIndex);
        totalProbes += probe;
        parkedVehicles++;
        System.out.println("Assigned spot #" + spotIndex + " (" + probe + " probes)");
    }

    public synchronized void exitVehicle(String licensePlate) {
        if (!licenseToSpot.containsKey(licensePlate)) {
            System.out.println("Vehicle not found");
            return;
        }
        int spotIndex = licenseToSpot.get(licensePlate);
        Vehicle v = spots[spotIndex].vehicle;
        long durationMs = System.currentTimeMillis() - v.entryTime;
        double hours = durationMs / 3600000.0;
        double fee = hours * 5.5;
        spots[spotIndex].vehicle = null;
        spots[spotIndex].occupied = false;
        licenseToSpot.remove(licensePlate);
        parkedVehicles--;
        System.out.printf("Spot #%d freed, Duration: %.2f h, Fee: $%.2f\n", spotIndex, hours, fee);
    }

    public void getStatistics() {
        double occupancy = (parkedVehicles * 100.0) / capacity;
        double avgProbes = (parkedVehicles == 0) ? 0 : (totalProbes * 1.0) / parkedVehicles;
        System.out.printf("Occupancy: %.2f%%, Avg Probes: %.2f\n", occupancy, avgProbes);
    }

    public static void main(String[] args) throws Exception {
        Problem_8 lot = new Problem_8(500);

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        Thread.sleep(2000);

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}