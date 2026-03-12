import java.util.*;

class Transaction {
    int id;
    int amount;
    String merchant;
    long timestamp;
    String account;

    Transaction(int id, int amount, String merchant, long timestamp, String account) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.timestamp = timestamp;
        this.account = account;
    }
}

public class Problem_9 {

    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<int[]> findTwoSum(int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();
        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }
            map.put(t.amount, t);
        }
        return result;
    }

    public List<int[]> findTwoSumWithinWindow(int target, long windowMs) {
        List<int[]> result = new ArrayList<>();
        transactions.sort(Comparator.comparingLong(t -> t.timestamp));
        Map<Integer, List<Transaction>> map = new HashMap<>();
        for (Transaction t : transactions) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                for (Transaction c : map.get(complement)) {
                    if (Math.abs(t.timestamp - c.timestamp) <= windowMs) {
                        result.add(new int[]{c.id, t.id});
                    }
                }
            }
            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }
        return result;
    }

    public List<List<Integer>> findKSum(int k, int target) {
        List<List<Integer>> res = new ArrayList<>();
        transactions.sort(Comparator.comparingInt(t -> t.amount));
        kSumHelper(0, k, target, new ArrayList<>(), res);
        return res;
    }

    private void kSumHelper(int start, int k, int target, List<Integer> path, List<List<Integer>> res) {
        if (k == 2) {
            int left = start, right = transactions.size() - 1;
            while (left < right) {
                int sum = transactions.get(left).amount + transactions.get(right).amount;
                if (sum == target) {
                    List<Integer> p = new ArrayList<>(path);
                    p.add(transactions.get(left).id);
                    p.add(transactions.get(right).id);
                    res.add(p);
                    left++;
                    right--;
                } else if (sum < target) left++;
                else right--;
            }
            return;
        }
        for (int i = start; i < transactions.size() - k + 1; i++) {
            if (i > start && transactions.get(i).amount == transactions.get(i - 1).amount) continue;
            path.add(transactions.get(i).id);
            kSumHelper(i + 1, k - 1, target - transactions.get(i).amount, path, res);
            path.remove(path.size() - 1);
        }
    }

    public List<Map<String, Object>> detectDuplicates() {
        Map<String, List<String>> map = new HashMap<>();
        for (Transaction t : transactions) {
            String key = t.amount + "|" + t.merchant;
            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t.account);
        }
        List<Map<String, Object>> res = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : map.entrySet()) {
            if (new HashSet<>(e.getValue()).size() > 1) {
                String[] parts = e.getKey().split("\\|");
                Map<String, Object> m = new HashMap<>();
                m.put("amount", Integer.parseInt(parts[0]));
                m.put("merchant", parts[1]);
                m.put("accounts", e.getValue());
                res.add(m);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Problem_9 ta = new Problem_9();
        ta.addTransaction(new Transaction(1, 500, "Store A", 36000000, "acc1"));
        ta.addTransaction(new Transaction(2, 300, "Store B", 36900000, "acc2"));
        ta.addTransaction(new Transaction(3, 200, "Store C", 37800000, "acc3"));
        ta.addTransaction(new Transaction(4, 500, "Store A", 38000000, "acc2"));

        System.out.println("Two Sum (500): " + ta.findTwoSum(500));
        System.out.println("Two Sum 1h window (500): " + ta.findTwoSumWithinWindow(500, 3600000));
        System.out.println("K Sum 3, target 1000: " + ta.findKSum(3, 1000));
        System.out.println("Duplicates: " + ta.detectDuplicates());
    }
}