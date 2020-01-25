package budget;

import budget.data.StoreCategories;
import budget.data.Store;

public class Main {
    public static void main(String[] args) {
        Store store = new Store();
        StoreCategories category = new StoreCategories();
        Menu menu = new Menu();
        menu.setStore(store);
        menu.setCategories(category);
        menu.start();
    }
}
