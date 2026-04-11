package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SalesManager {

    private static SalesManager instance;
    private final List<SalesRecord> records = new ArrayList<>();

    private SalesManager() {}

    public static SalesManager getInstance() {
        if (instance == null) instance = new SalesManager();
        return instance;
    }

    public void addRecord(SalesRecord record) {
        records.add(record);
    }

    public List<SalesRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public float getTotalRevenue() {
        float total = 0;
        for (SalesRecord r : records) total += r.total;
        return total;
    }

    public float getAverageOrderValue() {
        if (records.isEmpty()) return 0;
        return getTotalRevenue() / records.size();
    }
}
