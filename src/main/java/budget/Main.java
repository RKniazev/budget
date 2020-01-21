package budget;

import budget.data.Category;
import budget.data.Store;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        Category category = new Category();
        Menu menu = new Menu();
        menu.setStore(store);
        menu.setCategories(category);
        menu.start();
    }
}
