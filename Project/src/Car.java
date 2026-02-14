import java.util.ArrayList;

public class Car {
    public String name;

    private ArrayList<Part> brokenParts = new ArrayList<Part>();
    private ArrayList<Part> parts = new ArrayList<Part>();

    public Car(String name){
         this.name = name;
    }

    public void addPart(Part part){
        parts.add(part);
    }

    public void breakPart(Part part){
        if(!brokenParts.contains(part)){
            brokenParts.add(part);
        }
    }

    public boolean isBroken(Part part){
        return brokenParts.contains(part);
    }

    public void fixPart(Part part){
        brokenParts.remove(part);
    }

    public boolean isFixed(){
        return brokenParts.size() == 0;
    }

    public ArrayList<Part> getBrokenParts(){
        return brokenParts;
    }

    public int brokenCount(){
        return brokenParts.size();
    }

    public void showInfo(){
        System.out.println("====МАШИНА: " + name + "====");
        System.out.print("Сломанные детали: ");
        printDetails(brokenParts);
        System.out.println(" ");
        System.out.print("Восстановленные детали: ");
        printDetails(parts);
    }

    private void printDetails(ArrayList<Part> parts){
        for (int i = 0; i < parts.size(); i++) {
            if (i == parts.size()-1){
                System.out.print(". ");
            }
            else {
                System.out.print(", ");
            }
            System.out.print(parts.get(i).partType);
        }
    }
}
