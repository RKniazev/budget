package budget;

import budget.data.*;

import java.util.*;

public class Menu {
    private Store store;
    private StoreCategories categories;
    private boolean working = true;
    private Scanner scanner;
    private boolean workingSecondLvlMenu;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public StoreCategories getCategories() {
        return categories;
    }

    public void setCategories(StoreCategories categories) {
        this.categories = categories;
    }

    public void start() {
        StringBuilder menu = new StringBuilder()
                .append("Choose your action:" + System.lineSeparator())
                .append("1) Add income" + System.lineSeparator())
                .append("2) Add purchase" + System.lineSeparator())
                .append("3) Show list of purchases" + System.lineSeparator())
                .append("4) Balance" + System.lineSeparator())
                .append("5) Save" + System.lineSeparator())
                .append("6) Load" + System.lineSeparator())
                .append("7) Analyze (Sort)" +System.lineSeparator())
                .append("0) Exit" + System.lineSeparator());

        this.scanner = new Scanner(System.in);

        while (this.working) {
            System.out.print(menu);
            switch (this.scanner.nextLine()) {
                case "1": {
                    addIncome();
                    System.out.println();
                    break;
                }
                case "2": {
                    workingSecondLvlMenu = true;
                    while (workingSecondLvlMenu){
                        addPurchase();
                    }
                    System.out.println();
                    break;
                }
                case "3": {
                    workingSecondLvlMenu = true;
                    while (workingSecondLvlMenu){
                        showListPurchases();
                    }
                    System.out.println();
                    break;
                }
                case "4": {
                    showBalance();
                    System.out.println();
                    break;
                }
                case "5": {
                    saveToFile();
                    System.out.println();
                    break;
                }
                case "6": {
                    loudFromFile();
                    System.out.println();
                    break;
                }
                case "7": {
                    sort();
                    System.out.println();
                    break;
                }
                case "0": {
                    this.working = false;
                    System.out.println();
                    System.out.println("Bye!");
                    break;
                }

            }
        }

        this.scanner.close();
    }

    private void addIncome() {
        System.out.println();
        System.out.println("Enter income:");
        double income = Double.parseDouble(this.scanner.nextLine());
        Operation operation = new Operation(Type.INCOME, -1 , "income", income);
        store.add(operation);
        System.out.println("Income was added!");
    }

    private void addPurchase() {
        boolean showAllInMenu = false;
        showCategoryMenu(showAllInMenu);
        int category = Integer.parseInt(this.scanner.nextLine()) - 1;

        if (!isBack(category,showAllInMenu)){
            System.out.println(System.lineSeparator() + "Enter purchase name:");
            String name = this.scanner.nextLine();
            System.out.println("Enter its price:");
            double price = Double.parseDouble(this.scanner.nextLine());
            Operation operation = new Operation(Type.PURCHASE , category , name , price);
            store.add(operation);
            System.out.println("Purchase was added!");
        } else {
            workingSecondLvlMenu = false;
        }
    }

    private void showListPurchases() {
        boolean showAllInMenu = true;
        if (this.store.getAllParcel().isEmpty()){
            workingSecondLvlMenu = false;
            System.out.println(System.lineSeparator() + "Purchase list is empty!" + System.lineSeparator());

        } else {
            showCategoryMenu(showAllInMenu);
            int category = Integer.parseInt(this.scanner.nextLine()) - 1;
            if (!isBack(category,showAllInMenu)){
                String result = isAll(category,showAllInMenu) ? createStringAllPurchel() : createStringByCategory(category);
                System.out.println(result);
            } else {
                workingSecondLvlMenu = false;
            }
        }

    }
    private String createStringByCategory(int id){
        List<Operation> list = this.store.getByCategory(id);
        double count = 0;
        StringBuilder stingResult = new StringBuilder();
        stingResult.append(System.lineSeparator() + this.categories.getCategoryById(id) + ":" + System.lineSeparator());

        for (Operation operation : list) {
            if (operation.getType() == Type.PURCHASE) {
                stingResult.append(operation.getName() + String.format(" $%.2f", operation.getAmount()) + System.lineSeparator());
                count += operation.getAmount();
            }
        }


        stingResult.append(list.size() == 0 ? "Purchase list is empty!" : String.format("Total sum: $%.2f", count));

        return stingResult.toString();
    }
    private String createStringAllPurchel(){
        List<Operation> list = this.store.getAll();
        double count = 0;
        StringBuilder stingResult = new StringBuilder();
        stingResult.append(System.lineSeparator() + "All:" + System.lineSeparator());

        for (Operation operation : list) {
            if (operation.getType() == Type.PURCHASE) {
                stingResult.append(operation.getName() + String.format(" $%.2f", operation.getAmount()) + System.lineSeparator());
                count += operation.getAmount();
            }
        }
        stingResult.append(String.format("Total sum: $%.2f", count));

        return stingResult.toString();
    }

    private void showBalance() {
        System.out.println("");
        System.out.println("Balance: $" + store.countAmount());
        System.out.println("");
    }
    private boolean isAll(int id, boolean addAllInMenu){
        if (id == categories.getCategories().size() && addAllInMenu) {
            return true;
        }
        return false;
    }
    private boolean isBack(int id, boolean addAllInMenu){
        if (id == categories.getCategories().size() && !addAllInMenu) {
            return true;
        }
        if (id > categories.getCategories().size() && addAllInMenu){
            return true;
        }
        return false;
    }
    private void showCategoryMenu(boolean addAllInMenu){
        StringBuilder text = new StringBuilder();

        List<String> allCategories = categories.getCategories();

        text.append(System.lineSeparator());

        for (String cat:allCategories){
            text.append(allCategories.indexOf(cat)+1)
                .append(") ")
                .append(cat)
                .append(System.lineSeparator());
        }
        if (addAllInMenu){
            text.append(allCategories.size()+1)
                    .append(") All")
                    .append(System.lineSeparator());
            text.append(allCategories.size()+2)
                    .append(") Back");
        }else {
            text.append(allCategories.size()+1)
                    .append(") Back");
        }
        System.out.println(text.toString());
    }

    private void saveToFile(){
        this.store.saveToFile("purchases.txt");
        System.out.println(System.lineSeparator() + "Purchases were saved!");
    }

    private void loudFromFile(){
        this.store.loadFromFile("purchases.txt");
        System.out.println(System.lineSeparator() + "Purchases were loaded!");
    }

    private void sort(){
        boolean working = true;
        while (working){
            System.out.println(System.lineSeparator() +
                    "How do you want to sort?" +
                    System.lineSeparator() +
                    "1) Sort all purchases" +
                    System.lineSeparator() +
                    "2) Sort by type" +
                    System.lineSeparator() +
                    "3) Sort certain type" +
                    System.lineSeparator() +
                    "4) Back"
            );

            int input = Integer.parseInt(this.scanner.nextLine());

            switch (input){
                case 1 : {
                    System.out.println();
                    sortAllPurchases();
                    break;
                }
                case 2 : {
                    System.out.println();
                    sortByType();
                    break;
                }
                case 3 : {
                    sortCertainType();
                    break;
                }
                case 4 : {
                    working = false;
                    break;
                }
            }
        }

    }
    private void sortAllPurchases(){
        List<Operation> list = this.store.getAllParcel();
        Collections.sort(list,(e1,e2) -> e1.getAmount() < e2.getAmount()? 1 : -1);
        if (store.countAllPurchase() == 0){
            System.out.println("Purchase list is empty!");
        }else {
            System.out.println("All:");
            list.forEach(e -> System.out.printf(e.getName() + String.format(" $%.2f", e.getAmount()) + System.lineSeparator()));
            System.out.printf("Total: $%.2f" + System.lineSeparator(), store.countAllPurchase());
        }
    }
    private void sortByType(){
        ArrayList<Integer> sortedList = new ArrayList<>();

        for (int a = 0; a < categories.getCategories().size(); a++){
            sortedList.add(a);
        }

        Collections.sort(sortedList,(o1, o2) -> store.countCategoryById(o1) < store.countCategoryById(o2) ?  1 : -1);

        System.out.print("Types:" + System.lineSeparator());
        sortedList.forEach(element -> System.out.printf("%s - $%.2f" + System.lineSeparator(), categories.getCategoryById(element) , store.countCategoryById(element)));
        System.out.printf("Total sum: $%.2f" + System.lineSeparator(), store.countAllPurchase());
    }

    private void sortCertainType(){
        Boolean showAllInMenu = false;
        showCategoryMenu(showAllInMenu);
        int idCategory = Integer.parseInt(this.scanner.nextLine()) - 1;
        List<Operation> list = this.store.getByCategorySorted(idCategory);
        if (store.countCategoryById(idCategory) == 0){
            System.out.println("Purchase list is empty!");
        }else {
            System.out.println();
            System.out.println(categories.getCategoryById(idCategory) + ":");
            list.forEach(element -> {System.out.println(element.getName() + String.format(" $%.2f", element.getAmount()));});
            System.out.printf("Total: $%.2f" + System.lineSeparator(), store.countCategoryById(idCategory));
        }
    }
}
