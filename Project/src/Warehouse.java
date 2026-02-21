import java.util.ArrayList;

public class Warehouse {
    private ArrayList<Part> parts;
    private ArrayList<Integer> counts;
    private int capacity;

    public Warehouse(int capacity) {
        this.capacity = capacity;
        parts = new ArrayList<>();
        counts = new ArrayList<>();
    }

    public boolean addPart(Part part, int count) {
        if (count <= 0) return false;
        int currentTotal = getTotalCount();
        if (currentTotal + count > capacity) {
            System.out.println("Недостаточно места на складе! Свободно: " + (capacity - currentTotal));
            return false;
        }

        int index = parts.indexOf(part);
        if (index != -1) {
            counts.set(index, counts.get(index) + count);
        } else {
            parts.add(part);
            counts.add(count);
        }
        return true;
    }

    public boolean hasPart(Part part) {
        int index = parts.indexOf(part);
        return index != -1 && counts.get(index) > 0;
    }

    public Part takePart(Part part) {
        int index = parts.indexOf(part);
        if (index == -1 || counts.get(index) == 0) return null;

        int newCount = counts.get(index) - 1;
        if (newCount == 0) {
            parts.remove(index);
            counts.remove(index);
        } else {
            counts.set(index, newCount);
        }
        return part;
    }

    public int getTotalCount() {
        int total = 0;
        for (int c : counts) total += c;
        return total;
    }

    public void showInfo() {
        System.out.println("==== СКЛАД (вместимость " + capacity + ") ====");
        if (parts.isEmpty()) {
            System.out.println("Склад пуст");
            return;
        }

        ArrayList<PartType> types = new ArrayList<>();
        ArrayList<Integer> typeCounts = new ArrayList<>();

        for (int i = 0; i < parts.size(); i++) {
            Part part = parts.get(i);
            PartType type = part.partType;
            int index = types.indexOf(type);
            if (index == -1) {
                types.add(type);
                typeCounts.add(counts.get(i));
            } else {
                typeCounts.set(index, typeCounts.get(index) + counts.get(i));
            }
        }

        for (int i = 0; i < types.size(); i++) {
            System.out.println(types.get(i) + ": " + typeCounts.get(i) + " шт.");
        }
        System.out.println("Всего деталей: " + getTotalCount());
    }
}