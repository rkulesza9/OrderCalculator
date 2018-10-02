package illeagle99.ordercalculator.backend;

/**
 * Created by kules on 9/25/2016.
 */


public class Item {
    private String name;
    private double cost;
    private boolean taxable;

    public Item(){
        name = "item"; /* to differentiate */
        cost = 0;
        taxable = true;
    }
    public Item(String name, double cost,boolean isTaxable){
        this.name = name;
        this.cost = cost;
        taxable = isTaxable;
    }

    public void setName(String nm){ name = nm; }
    public void setCost(double cos){
        cost = cos;
    }
    public void setTaxable(boolean taxable){
        this.taxable = taxable;
    }
    public String getName(){ return name; }
    public double getCost(){ return cost; }
    public boolean isTaxable(){ return taxable; }

    @Override
    public String toString(){
        return String.format("%s : $%.2f %s",name,cost,taxable ? "[is taxable]" : "");
    }
}
