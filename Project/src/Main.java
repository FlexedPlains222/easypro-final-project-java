import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
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

        AutoService service = new AutoService(1000, warehouse, 50, 10);

        String[] carNames = {"Toyota", "BMW", "Lada", "Ford", "Honda"};
        for (int i = 0; i < 3; i++) {
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

        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
            System.out.println("1. Показать склад");
            System.out.println("2. Показать очередь");
            System.out.println("3. Взять следующую машину в ремонт");
            System.out.println("4. Пополнить склад");
            System.out.println("5. Выход");
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
                    repairNextCar(service, scanner);
                    break;
                case 4:
                    restockWarehouse(service, scanner);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Выход из программы.");
                    break;
                default:
                    System.out.println("Неверный пункт меню.");
            }
        }
        scanner.close();
    }

    private static void repairNextCar(AutoService service, Scanner scanner) {
        Car car = service.pollCar();
        if (car == null) {
            System.out.println("Очередь пуста, нечего ремонтировать.");
            return;
        }

        System.out.println("\n--- Ремонт машины " + car.name + " ---");
        car.showInfo();

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
                repairedCount++;
            } else {
                System.out.println("Нет на складе детали " + part.partType + " – пропускаем.");
            }
        }

        if (car.isFixed()) {
            System.out.println("Машина полностью исправлена и уезжает!");
        } else {
            System.out.println("Не все поломки устранены (не хватило деталей).");
        }
        System.out.println("Текущий баланс: " + service.getMoney());
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
}