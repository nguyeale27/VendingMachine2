/**
 * VendingSnack is used to represent the snacks in VendingMachine
 */
class VendingSnack{
    private String name;
    private int amount;
    private double price;
    /**
     * Constructor for VendingSnack
     * @param n The name to be assigned to the created instance of VendingSnack.
     * @param a The amount to be assigned to the created instance of VendingSnack.
     * @param p The price to be assigned to the created instance of VendingSnack.
     */
    public VendingSnack(String n, int a, double p){
        n.trim();
        name = n;
        amount = a;
        price = p;
    }
    /**
     * getName returns the VendingSnack's name.
     * @return value of name
     */
    public String getName(){
        return name;
    }
    /**
     * getName returns the VendingSnack's amount.
     * @return value of amount
     */
    public int getAmount(){
        return amount;
    }
    /**
     * getName returns the VendingSnack's price.
     * @return value of name
     */
    public double getPrice(){
        return price;
    }
    /**
     * subtractAmount Subtracts from the amount when snack is purchased.
     */
    public void subtractAmount(){
        amount--;
    }
}