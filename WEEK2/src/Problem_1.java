import java.util.*;

class Problem_1 {

    static class Transaction {
        String id;
        double fee;
        String timestamp;

        Transaction(String id, double fee, String timestamp) {
            this.id = id;
            this.fee = fee;
            this.timestamp = timestamp;
        }

        public String toString() {
            return id + ":" + fee;
        }

        public String fullString() {
            return id + ":" + fee + "@" + timestamp;
        }
    }

    public static String bubbleSort(List<Transaction> list) {
        int n = list.size();
        int passes = 0, swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            passes++;
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Transaction temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("BubbleSort (fees): [");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).toString());
            if (i != list.size() - 1) sb.append(", ");
        }
        sb.append("] // ").append(passes).append(" passes, ").append(swaps).append(" swaps");

        return sb.toString();
    }

    public static String insertionSort(List<Transaction> list) {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("InsertionSort (fee+ts): [");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).fullString());
            if (i != list.size() - 1) sb.append(", ");
        }
        sb.append("]");

        return sb.toString();
    }

    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) {
            return Double.compare(t1.fee, t2.fee);
        }
        return t1.timestamp.compareTo(t2.timestamp);
    }

    public static String findOutliers(List<Transaction> list) {
        List<Transaction> outliers = new ArrayList<>();

        for (Transaction t : list) {
            if (t.fee > 50) {
                outliers.add(t);
            }
        }

        if (outliers.isEmpty()) {
            return "High-fee outliers: none";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("High-fee outliers: ");
        for (int i = 0; i < outliers.size(); i++) {
            sb.append(outliers.get(i).fullString());
            if (i != outliers.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();

        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        System.out.println("Input transactions:");
        for (Transaction t : transactions) {
            System.out.println(t.id + ", fee=" + t.fee + ", ts=" + t.timestamp);
        }

        List<Transaction> bubbleList = new ArrayList<>(transactions);
        List<Transaction> insertionList = new ArrayList<>(transactions);

        System.out.println(bubbleSort(bubbleList));
        System.out.println(insertionSort(insertionList));
        System.out.println(findOutliers(transactions));
    }
}