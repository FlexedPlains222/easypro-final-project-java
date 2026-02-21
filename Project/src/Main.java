import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static int dayMoney = 0;

    private static final Part[] STANDARD_PARTS = {
            new Part(PartType.ENGINE, 500, 200),
            new Part(PartType.WHEEL, 100, 50),
            new Part(PartType.BRAKE, 80, 40),
            new Part(PartType.TRANSMISSION, 300, 150),
            new Part(PartType.BATTERY, 120, 30),
            new Part(PartType.HEADLIGHT, 60, 20),
            new Part(PartType.BUMPER, 150, 60)
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        Warehouse warehouse = new Warehouse(20);
        warehouse.addPart(STANDARD_PARTS[0], 2);
        warehouse.addPart(STANDARD_PARTS[1], 4);
        warehouse.addPart(STANDARD_PARTS[2], 3);
        warehouse.addPart(STANDARD_PARTS[4], 1);

        ArrayList<Car> fixedCarsOfDay = new ArrayList<Car>();
        ArrayList<Car> refusedCarsOfDay = new ArrayList<Car>();

        AutoService service = new AutoService(1000, warehouse, 50, 10);

        int count = random.nextInt(5) + 1;
        generateQueue(count, random, service);

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Показать склад");
            System.out.println("2. Показать очередь");
            System.out.println("3. Взять следующую машину в ремонт");
            System.out.println("4. Пополнить склад");
            System.out.println("5. Подождать очереди");
            System.out.println("6. Выход");
            System.out.print("Выберите действие: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка ввода, попробуйте снова.");
                continue;
            }

            switch (choice) {
                case 1:
                    warehouse.showInfo();
                    System.out.println("Баланс автосервиса: " + service.getMoney() + " монет");
                    break;
                case 2:
                    service.showQueue();
                    break;
                case 3:
                    repairNextCar(service, fixedCarsOfDay, refusedCarsOfDay, scanner);
                    break;
                case 4:
                    restockWarehouse(service, scanner);
                    break;
                case 5:
                    count = random.nextInt(5) + 1;
                    generateQueue(count, random, service);
                    System.out.print("Отремонтировано машин за день:");
                    printCars(fixedCarsOfDay);
                    System.out.println(" ");
                    System.out.print("Отказано в ремонте за день: ");
                    printCars(refusedCarsOfDay);
                    System.out.println(" ");
                    System.out.println("Выручка: " + dayMoney);
                    fixedCarsOfDay.clear();
                    refusedCarsOfDay.clear();
                    dayMoney = 0;
                    break;
                case 6:
                    exit = true;
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Неверный пункт меню.");
            }
        }
        scanner.close();
    }

    private static void repairNextCar(AutoService service, ArrayList<Car> fixed, ArrayList<Car> refused, Scanner scanner) {
        Car car = service.pollCar();
        if (car == null) {
            System.out.println("Очередь пуста, нечего ремонтировать.");
            return;
        }

        System.out.println("\n--- Ремонт машины " + car.name + " ---");
        car.showInfo();
        System.out.println(" ");
        System.out.println("Починить машину?(1- да, 2-нет)");
        int question = scanner.nextInt();
        scanner.nextLine();
        if (question == 1){
            if (car.isFixed()) {
                System.out.println("Машина уже исправна! Отпускаем без ремонта.");
                return;
            }

            ArrayList<Part> brokenCopy = new ArrayList<>(car.getBrokenParts());
            int repairedCount = 0;
            for (Part part : brokenCopy) {
                if (service.getWarehouse().hasPart(part)) {
                    service.getWarehouse().takePart(part);
                    car.fixPart(part);
                    service.addMoney(part.repairPrice);
                    System.out.println("Отремонтирована деталь: " + part.partType + " (доход +" + part.repairPrice + ")");
                    dayMoney += part.repairPrice;
                    repairedCount++;
                } else {
                    System.out.println("Нет на складе детали " + part.partType + " – пропускаем.");

                }
            }

            if (car.isFixed()) {
                System.out.println("Машина полностью исправлена и уезжает!");
                fixed.add(car);
            } else {
                System.out.println("Не все поломки устранены (не хватило деталей).");
                refused.add(car);
                service.refuseFine(car, true);
            }
            System.out.println("Текущий баланс: " + service.getMoney());
        }
        else {
            service.refuseFine(car, false);
        }
    }



    private static void generateQueue(int count, Random random, AutoService service){
        String[] carNames = {"Toyota", "BMW", "Lada", "Ford", "Honda"};
        for (int i = 0; i < count; i++) {
            Car car = new Car(carNames[i % carNames.length] + "-" + (i + 1));
            for (Part p : STANDARD_PARTS) {
                car.addPart(p);
            }
            int brokenCount = random.nextInt(3) + 1;
            for (int j = 0; j < brokenCount; j++) {
                Part randomPart = STANDARD_PARTS[random.nextInt(STANDARD_PARTS.length)];
                car.breakPart(randomPart);
            }
            service.enqueueCar(car);
        }
    }

    private static void restockWarehouse(AutoService service, Scanner scanner) {
        System.out.println("\n--- Пополнение склада ---");
        System.out.println("Доступные детали (тип / цена / цена установки):");
        for (int i = 0; i < STANDARD_PARTS.length; i++) {
            System.out.print((i + 1) + ". ");
            STANDARD_PARTS[i].showInfo();
        }
        System.out.print("Выберите номер детали: ");
        int partIndex;
        try {
            partIndex = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода.");
            return;
        }
        if (partIndex < 0 || partIndex >= STANDARD_PARTS.length) {
            System.out.println("Неверный номер.");
            return;
        }
        Part selectedPart = STANDARD_PARTS[partIndex];

        System.out.print("Введите количество: ");
        int count;
        try {
            count = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Ошибка ввода.");
            return;
        }
        if (count <= 0) {
            System.out.println("Количество должно быть положительным.");
            return;
        }

        int totalCost = selectedPart.price * count;
        if (service.getMoney() < totalCost) {
            System.out.println("Недостаточно денег! Нужно " + totalCost + ", есть " + service.getMoney());
            return;
        }

        if (service.getWarehouse().addPart(selectedPart, count)) {
            service.addMoney(-totalCost);
            System.out.println("Детали добавлены. Потрачено " + totalCost + ". Остаток: " + service.getMoney());
        } else {
            System.out.println("Не удалось добавить детали (превышение лимита склада).");
        }
    }
    private static void printCars(ArrayList<Car> cars) {
        for (int i = 0; i < cars.size(); i++) {
            System.out.print(cars.get(i).name);
            if (i == cars.size() - 1) {
                System.out.print(". ");
            } else {
                System.out.print(", ");
            }
        }
    }
}