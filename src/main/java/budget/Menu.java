package budget;

import budget.data.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private Store store;
    private Category categories;
    private boolean working = true;
    private Scanner scanner;
    private boolean workingSecondLvlMenu;

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
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
                    System.out.println("");
                    break;
                }
                case "2": {
                    workingSecondLvlMenu = true;
                    while (workingSecondLvlMenu){
                        addPurchase();
                    }
                    System.out.println("");
                    break;
                }
                case "3": {
                    workingSecondLvlMenu = true;
                    while (workingSecondLvlMenu){
                        showListPurchases();
                    }
                    System.out.println("");
                    break;
                }
                case "4": {
                    showBalance();
                    System.out.println("");
                    break;
                }
                case "5": {
                    saveToFile();
                    System.out.println("");
                    break;
                }
                case "6": {
                    loudFromFile();
                    System.out.println("");
                    break;
                }
                case "7": {
                    sort();
                    System.out.println("");
                    break;
                }
                case "0": {
                    this.working = false;
                    System.out.println("");
                    System.out.println("Bye!");
                    break;
                }

            }
        }

        this.scanner.close();
    }

    private void addIncome() {
        System.out.println("");
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
        if (addAllInMenu == true){
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
        StringBuilder result = new StringBuilder();
        Double total = 0d;

        result.append("All:" + System.lineSeparator());

        List<Operation> list = this.store.getAllParcelSorted();
        for (Operation operation : list){
            result.append(operation.getName() + String.format(" $%.2f", operation.getAmount()) + System.lineSeparator());
            total += operation.getAmount();
        }

        result.append(String.format("Total: $%.2f", total));
        if (total == 0){
            System.out.println("Purchase list is empty!");
        }else {
            System.out.println(result);
        }
    }
    private void sortByType(){
        StringBuilder result = new StringBuilder();
        Double total = 0d;
        boolean isSorted = false;

        result.append("Types:" + System.lineSeparator());
        int[] sortedIdCategory = new int[categories.getCategories().size()];
        for (int a = 0; a < sortedIdCategory.length; a++){
            sortedIdCategory[a] = a;
        }

        while (!isSorted){
            isSorted = true;
            for (int a = 0; a < sortedIdCategory.length-1; a++){

                int firstId = sortedIdCategory[a];
                int secondId = sortedIdCategory[a+1];

                if (store.countCategoryById(firstId) < store.countCategoryById(secondId)){
                    sortedIdCategory[a] = secondId;
                    sortedIdCategory[a+1] = firstId;
                    isSorted = false;
                }
            }
        }


        for (int a : sortedIdCategory){
            result.append(String.format("%s - $%.2f" + System.lineSeparator(), categories.getCategoryById(a) , store.countCategoryById(a)));
            total += store.countCategoryById(a);
        }

        result.append(String.format("Total sum: $%.2f", total));

        System.out.println(result);
    }
    private void sortCertainType(){
        Boolean showAllInMenu = false;

        showCategoryMenu(showAllInMenu);

        int category = Integer.parseInt(this.scanner.nextLine()) - 1;

        StringBuilder result = new StringBuilder();
        System.out.println();
        Double total = 0d;

        result.append( categories.getCategoryById(category) + ":" + System.lineSeparator());

        List<Operation> list = this.store.getByCategorySorted(category);

        for (Operation operation : list){
            result.append(operation.getName() + String.format(" $%.2f", operation.getAmount()) + System.lineSeparator());
            total += operation.getAmount();
        }
        result.append(String.format("Total: $%.2f", total));
        if (total == 0){
            System.out.println("Purchase list is empty!");
        }else {
            System.out.println(result);
        }
    }

}
