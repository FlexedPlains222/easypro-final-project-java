import java.util.ArrayDeque;

public class AutoService {
    private int money;
    private Warehouse warehouse;
    private ArrayDeque<Car> queue = new ArrayDeque<>();
    private int fineBefore;
    private int finePerUnfixed;

    public AutoService(int money, Warehouse warehouse, int fineBefore, int finePerUnfixed) {
        this.money = money;
        this.warehouse = warehouse;
        this.fineBefore = fineBefore;
        this.finePerUnfixed = finePerUnfixed;
    }

    public int getMoney() {
        return money;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void enqueueCar(Car car) {
        queue.addLast(car);
    }

    public Car pollCar() {
        return queue.pollFirst();
    }

    public int queueSize() {
        return queue.size();
    }

    public int refuseFine(Car car, boolean startedRepair) {
        int fine = startedRepair ? finePerUnfixed * car.brokenCount() : fineBefore;
        money -= fine;
        return fine;
    }

    public void showQueue() {
        System.out.println("===== ОЧЕРЕДЬ В АВТОСЕРВИС (" + queue.size() + " машин) =====");
        if (queue.isEmpty()) {
            System.out.println("Очередь пуста");
            return;
        }
        int num = 1;
        for (Car car : queue) {
            System.out.print("#" + num++ + " ");
            car.showInfo();
            System.out.println(" ");
        }
    }
}