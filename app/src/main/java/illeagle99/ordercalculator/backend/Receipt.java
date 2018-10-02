package illeagle99.ordercalculator.backend;

/**
 * Created by kules on 9/25/2016.
 */

import android.os.Binder;

import java.util.ArrayList;

public class Receipt extends Binder {
    private ArrayList<Item> items;
    private double tax;
    private Item selected;

    public Receipt(){
        items = new ArrayList<Item>();
        tax = 0;
        selected = null;
    }

    /* for changing created items */
    public void select(Item item){ selected = item; }
    public Item getSelected(){ return selected; }

    public boolean contains(String name){
        for(Item item : items)
            if(item.getName().equals(name))
                return true;
        return false;
    }
    public void remove(int index) { items.remove(index); }
    public void add(Item item){
        items.add(item);
    }
    public Item get(int index){ return items.get(index); }
    public int size(){ return items.size(); }
    public void setTaxRate(double t){ tax = t; }
    public double getTaxRate(){ return tax;}

    public void remove(String nameOfItem){
        for(int x = 0; x < items.size(); x++){
            Item item = items.get(x);
            if(item.getName().equalsIgnoreCase(nameOfItem))
                items.remove(x);
        }
    }

    public double itemsTotal(){
        double total = 0;
        for(Item item : items) total += item.getCost();
        return total;
    }
    public double taxAmount(){
        double total = 0;
        for(Item item: items)
            if(item.isTaxable())
                total += item.getCost()*tax;
        return total;
    }

    public double totalWithTax(){
        return itemsTotal() + taxAmount();
    }

    public String toString(){
        String receipt = "", seperator = "------------------------------------";
        double total = itemsTotal();
        double taxAmount = taxAmount();
        double balance = totalWithTax();
        for(Item item : items)
            receipt += item.toString()+"\n";
        receipt += seperator + "\n";
        receipt += String.format("items total (without tax): $%.2f%n", total);
        receipt += String.format("applicable tax: $%.2f\n", taxAmount);
        receipt += seperator + "\n";
        receipt += String.format("order total: $%.2f",balance);
        return receipt;
    }

}