import java.util.ArrayList;

public class Warehouse {
    private ArrayList<Part> parts;
    private ArrayList<PartType> countedTypes = new ArrayList<>();
    private ArrayList<Integer> counts = new ArrayList<>();

    public Warehouse() {
        parts = new ArrayList<>();
    }

    public void addPart(Part part, int count) {
        if (parts.contains(part)){
            int index = parts.indexOf(part);
            counts.add(index, count);
        }
        else {
            parts.add(part);
            counts.add(count);
        }


    }

    public boolean hasPart(Part part) {
        for (Part p : parts) {
            if (p == part) {
                return true;
            }
        }
        return false;
    }

    public Part takePart(Part part) {
        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i) == part) {
                if (counts.get(i).equals(1)){
                    parts.remove(i);
                    counts.remove(i);
                }
                else{
                    counts.set(i, counts.get(i) - 1);
                }

                return part;
            }
        }
        return null;
    }

    public void showInfo() {
        System.out.println("==== СКЛАД ====");
        if (parts.isEmpty()) {
            System.out.println("Склад пуст");
            return;
        }

        for (Part p : parts) {
            int index = countedTypes.indexOf(p.partType);
            if (index == -1) {
                countedTypes.add(p.partType);
                counts.add(1);
            } else {
                counts.set(index, counts.get(index) + 1);
            }
        }

        for (int i = 0; i < countedTypes.size(); i++) {
            System.out.println(countedTypes.get(i) + ": " + counts.get(i) + " шт.");
        }
    }
}