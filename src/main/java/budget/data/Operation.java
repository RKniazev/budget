package budget.data;

public class Operation {
    private Type type;
    private int category;
    private String name;
    private double amount;

    public Operation(Type type, int category ,String name, double amount) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public int getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("name - " + this.name + " ")
                .append("amount - " + this.amount)
                .toString();
    }
}