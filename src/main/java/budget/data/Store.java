package budget.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Store {
    private static final Store INST = new Store();

    private final List<Operation> operations = new ArrayList<>();

    public Store getStore() {
        return INST;
    }

    public void add(Operation operation){
        operations.add(operation);
    }

    public Operation get(int index){
        return operations.get(index);
    }

    public List<Operation> getAll(){
        return operations;
    }

    public List<Operation> getAllParcel(){
        List<Operation> result = new ArrayList<>();

        operations.stream().filter(e -> e.getType()==Type.PURCHASE).forEach(e -> result.add(e));

        return result;
    }

    public List<Operation> getByCategory(int id) {
        List<Operation> result = new ArrayList<>();
        operations.forEach(element -> { if (element.getCategory()==id && element.getType().equals(Type.PURCHASE)) result.add(element);});
        return result;
    }

    public List<Operation> getByCategorySorted(int id){
        return getByCategory(id);
    }

    public double countAmount(){
        double count = 0;
        count += operations.stream().filter(operation -> operation.getType()==Type.INCOME).mapToDouble(Operation::getAmount).sum();
        count -= operations.stream().filter(operation -> operation.getType()==Type.PURCHASE).mapToDouble(Operation::getAmount).sum();
        return count;
    }

    public double countAllPurchase(){
        return operations.stream().filter(e -> e.getType() == Type.PURCHASE).mapToDouble(Operation::getAmount).sum();
    }

    public double countCategoryById(int id){
        return (double) getByCategory(id).stream().count();
    }


    public void saveToFile(String part){
        File file = new File(part);
        try {
            FileWriter fileWriter = new FileWriter(file);
            for (Operation operation : operations){
                String write = operation.getType() + "/"
                        + operation.getCategory() + "/"
                        + operation.getName() + "/"
                        + operation.getAmount() + System.lineSeparator();
                fileWriter.write(write);
            }
            fileWriter.close();
        } catch (Exception e){
            System.out.println("Error with File");
        }

    }

    public void loadFromFile(String part){
        File file = new File(part);
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while (bufferedReader.ready()){
                String[] arguments = bufferedReader.readLine().split("/");
                Type type = arguments[0].equals("PURCHASE") ? Type.PURCHASE : Type.INCOME;
                int category = Integer.parseInt(arguments[1]);
                String name = arguments[2];
                double amount = Double.parseDouble(arguments[3]);

                add(new Operation(type,category,name, amount));
            }
            fileReader.close();
        }  catch (Exception e){
            System.out.println("Error with File");
        }


    }
}