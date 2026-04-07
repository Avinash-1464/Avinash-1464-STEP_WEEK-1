import java.util.*;

class Problem_2 {

    static class Client {
        String name;
        int riskScore;
        double accountBalance;

        Client(String name, int riskScore, double accountBalance) {
            this.name = name;
            this.riskScore = riskScore;
            this.accountBalance = accountBalance;
        }

        public String toString() {
            return name + ":" + riskScore;
        }

        public String fullString() {
            return name + ":" + riskScore + "(" + accountBalance + ")";
        }
    }

    public static String bubbleSort(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Bubble (asc): [");
        for (int i = 0; i < n; i++) {
            sb.append(arr[i].toString());
            if (i != n - 1) sb.append(", ");
        }
        sb.append("] // Swaps: ").append(swaps);
        return sb.toString();
    }

    public static String insertionSort(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Insertion (desc): [");
        for (int i = 0; i < n; i++) {
            sb.append(arr[i].toString());
            if (i != n - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private static int compare(Client a, Client b) {
        if (a.riskScore != b.riskScore) {
            return Integer.compare(a.riskScore, b.riskScore);
        }
        return Double.compare(a.accountBalance, b.accountBalance);
    }

    public static String topRisks(Client[] arr, int k) {
        StringBuilder sb = new StringBuilder();
        sb.append("Top ").append(k).append(" risks: ");
        for (int i = 0; i < Math.min(k, arr.length); i++) {
            sb.append(arr[i].name).append("(").append(arr[i].riskScore).append(")");
            if (i != Math.min(k, arr.length) - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Client[] clients = {
                new Client("clientC", 80, 5000),
                new Client("clientA", 20, 3000),
                new Client("clientB", 50, 4000)
        };

        System.out.print("Input: [");
        for (int i = 0; i < clients.length; i++) {
            System.out.print(clients[i].toString());
            if (i != clients.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        Client[] bubbleArr = clients.clone();
        Client[] insertionArr = clients.clone();

        System.out.println(bubbleSort(bubbleArr));
        System.out.println(insertionSort(insertionArr));
        System.out.println(topRisks(insertionArr, 3));
    }
}