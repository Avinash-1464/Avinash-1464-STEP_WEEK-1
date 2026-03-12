import java.util.*;

class VideoData {
    String videoId;
    String content;

    VideoData(String videoId, String content) {
        this.videoId = videoId;
        this.content = content;
    }
}

public class Problem_10  {

    private int L1_CAPACITY = 10000;
    private int L2_CAPACITY = 100000;

    private LinkedHashMap<String, VideoData> L1 = new LinkedHashMap<>(L1_CAPACITY, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
            return size() > L1_CAPACITY;
        }
    };

    private HashMap<String, String> L2 = new HashMap<>();
    private HashMap<String, Integer> accessCount = new HashMap<>();
    private HashMap<String, VideoData> database = new HashMap<>();

    private int L1_hits = 0, L2_hits = 0, L3_hits = 0, totalRequests = 0;

    public void addToDatabase(String videoId, String content) {
        database.put(videoId, new VideoData(videoId, content));
    }

    public VideoData getVideo(String videoId) {
        totalRequests++;
        if (L1.containsKey(videoId)) {
            L1_hits++;
            return L1.get(videoId);
        }
        if (L2.containsKey(videoId)) {
            L2_hits++;
            accessCount.put(videoId, accessCount.getOrDefault(videoId, 0) + 1);
            if (accessCount.get(videoId) > 3 && database.containsKey(videoId)) {
                L1.put(videoId, database.get(videoId));
            }
            return database.get(videoId);
        }
        if (database.containsKey(videoId)) {
            L3_hits++;
            L2.put(videoId, videoId + "_ssd_path");
            accessCount.put(videoId, 1);
            return database.get(videoId);
        }
        return null;
    }

    public void invalidate(String videoId) {
        L1.remove(videoId);
        L2.remove(videoId);
        database.remove(videoId);
        accessCount.remove(videoId);
    }

    public void getStatistics() {
        double L1_hit_rate = totalRequests == 0 ? 0 : L1_hits * 100.0 / totalRequests;
        double L2_hit_rate = totalRequests == 0 ? 0 : L2_hits * 100.0 / totalRequests;
        double L3_hit_rate = totalRequests == 0 ? 0 : L3_hits * 100.0 / totalRequests;
        System.out.printf("L1: Hit Rate %.2f%%\nL2: Hit Rate %.2f%%\nL3: Hit Rate %.2f%%\nOverall: Hit Rate %.2f%%\n",
                L1_hit_rate, L2_hit_rate, L3_hit_rate, L1_hit_rate + L2_hit_rate + L3_hit_rate);
    }

    public static void main(String[] args) {
        Problem_10  cache = new Problem_10 ();
        cache.addToDatabase("video_123", "Content A");
        cache.addToDatabase("video_999", "Content B");

        cache.getVideo("video_123");
        cache.getVideo("video_123");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}