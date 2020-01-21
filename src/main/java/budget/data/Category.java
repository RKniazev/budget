package budget.data;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final List<String> categories = new ArrayList<>();

    public Category() {
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
        for (String string:strings){
            categories.add(string);
        }
    }

    public String getCategoryById(int id){
        return categories.get(id);
    }
}