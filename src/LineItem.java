public class LineItem extends Product {
    private int quantity;
    public double calculatedTotal;

    public LineItem(String productName, double unitPrice, int quantity) {
        super(productName, unitPrice);
        this.quantity = quantity;
        this.calculatedTotal = this.quantity * this.unitPrice;
    }

    @Override
    public String toString() {
        return String.format("%-20s%6d%10.2f%10.2f", productName, quantity, unitPrice, calculatedTotal) + "\n";
    }
}
