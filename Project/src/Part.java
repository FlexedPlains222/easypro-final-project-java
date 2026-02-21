public class Part {
    public PartType partType;
    public int price;
    public int repairPrice;

    public Part(PartType partType, int price, int repairPrice) {
        this.partType = partType;
        this.price = price;
        this.repairPrice = repairPrice;
    }

    public void showInfo() {
        System.out.println("Тип: " + partType + ", Цена за штуку: " + price + ", Цена установки: " + repairPrice);
    }
}