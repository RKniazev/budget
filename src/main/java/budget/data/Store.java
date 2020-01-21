package budget.data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        for (Operation operation : operations){
            if (operation.getType() == Type.PURCHASE){
                result.add(operation);
            }
        }
        return result;
    }

    public List<Operation> getAllParcelSorted(){
        return sortOperation(getAllParcel());
    }

    public List<Operation> getByCategory(int id) {
        List<Operation> result = new ArrayList<>();
        for (Operation operation : operations) {
            if (operation.getCategory() == id) {
                result.add(operation);
            }
        }
        return result;
    }

    public List<Operation> getByCategorySorted(int id){
        return sortOperation(getByCategory(id));
    }

    public List<Operation> sortOperation(List<Operation> input){
        boolean isSorted = false;
        while (!isSorted){
            isSorted = true;
            for (int a = 0; a < input.size()-1; a++) {
                if (input.get(a).getAmount() < input.get(a+1).getAmount()){
                    isSorted = false;
                    Collections.swap(input,a,a+1);
                }
            }
        }

        return input;
    }

    public double countAmount(){
        double count = 0;
        for (Operation operation : operations){
            switch (operation.getType()){
                case INCOME: {
                    count += operation.getAmount();
                    break;
                }
                case PURCHASE: {
                    count -= operation.getAmount();
                    break;
                }
            }
        }
        return count;
    }

    public double countCategoryById(int id){
        double count = 0;
        for (Operation operation : getByCategory(id)){
            count += operation.getAmount();
        }
        return count;
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