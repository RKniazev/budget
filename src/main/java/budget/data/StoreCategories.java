package budget.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoreCategories {
    private final List<String> categories = new ArrayList<>();

    public StoreCategories() {
        super();
        addStandard();
    }

    public List<String> getCategories() {
        return categories;
    }

    private void addStandard(){
        categories.add("Food");
        categories.add("Clothes");
        categories.add("Entertainment");
        categories.add("Other");
    }

    public void addCategory(String...strings){
        Collections.addAll(categories, strings);
    }

    public String getCategoryById(int id){
        return categories.get(id);
    }
}