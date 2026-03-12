import java.util.*;

class TrieNode {
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd;
    PriorityQueue<QueryFreq> topQueries = new PriorityQueue<>(Comparator.comparingInt(a -> a.freq));
}

class QueryFreq {
    String query;
    int freq;
    QueryFreq(String query, int freq) { this.query = query; this.freq = freq; }
}

public class Problem_7 {

    private TrieNode root = new TrieNode();
    private HashMap<String, Integer> globalFreq = new HashMap<>();
    private int k = 10;

    public void addQuery(String query, int freq) {
        globalFreq.put(query, globalFreq.getOrDefault(query, 0) + freq);
        int updatedFreq = globalFreq.get(query);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);

            boolean exists = false;
            for (QueryFreq qf : node.topQueries) {
                if (qf.query.equals(query)) {
                    qf.freq = updatedFreq;
                    exists = true;
                    break;
                }
            }
            if (!exists) node.topQueries.add(new QueryFreq(query, updatedFreq));
            if (node.topQueries.size() > k) node.topQueries.poll();
        }
        node.isEnd = true;
    }

    public List<QueryFreq> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (!node.children.containsKey(c)) return new ArrayList<>();
            node = node.children.get(c);
        }
        PriorityQueue<QueryFreq> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a.freq));
        heap.addAll(node.topQueries);
        List<QueryFreq> result = new ArrayList<>();
        while (!heap.isEmpty()) result.add(heap.poll());
        Collections.reverse(result);
        return result;
    }

    public void updateFrequency(String query) {
        addQuery(query, 1);
    }

    public static void main(String[] args) {
        Problem_7 ac = new Problem_7();

        ac.addQuery("java tutorial", 1234567);
        ac.addQuery("javascript", 987654);
        ac.addQuery("java download", 456789);
        ac.addQuery("java 21 features", 1);

        List<QueryFreq> suggestions = ac.search("jav");
        for (QueryFreq q : suggestions) {
            System.out.println("\"" + q.query + "\" (" + q.freq + " searches)");
        }

        ac.updateFrequency("java 21 features");
        ac.updateFrequency("java 21 features");
        System.out.println("\nAfter trending update:");
        suggestions = ac.search("java 21");
        for (QueryFreq q : suggestions) {
            System.out.println("\"" + q.query + "\" (" + q.freq + " searches)");
        }
    }
}