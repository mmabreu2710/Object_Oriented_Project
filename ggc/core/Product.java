package ggc.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.Serializable;
import java.lang.Math;
import ggc.core.exception.UnsufficientUnitsException;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Collections;


public abstract class Product implements Serializable{
    private String _id;
    private double _maxTransactionPrice;
    private List<Batch> _batches = new ArrayList<>();
    private Collection<Observer> _observers = new ArrayList<>();
    private int _numberOfDays;

    Product(String id){
        _id = id;
    }
    /**
     * @return product id.
     */
    public String getId(){
        return _id;
    }

    /**
     * @see Batch
     * adds a batch to the group of product batches.
     */
    public void addBatch(Batch batch){
    	_batches.add(batch);
    }
    /**
     * @return group of batches
     */
    public List<Batch> getBatches(){
        this.removeEmptyBatches();
        return _batches;
    }
    /**
     * @return maximum price of all the product batches.
     */
    public double getMaxPrice(){
        double price = 0;
        for (Batch b : _batches)
            if (b.getPrice() > price)
                price = b.getPrice();
        return price;
    }

    /**
     * @return minimumPrice of all the product batches
     */
    public double getMinPrice(){
        double price = getMaxPrice();
        for (Batch b : _batches)
            if (b.getPrice() < price)
                price = b.getPrice();
        return price;
    }

    /**
     * @see batch
     * @return Total quantity of a product, with one or more batches.
    */
    public int getTotalQuantity(){
        int quantity = 0;
        for (Batch b : _batches)
            quantity += b.getQuantity();
        return quantity; 
    }

    /**
     * @see Observer
     * @return Collection of observers of this product
     */
    public Collection<Observer> getObservers(){
        return _observers;
    }
    
    /**
     * @see Observer
     */
    public void SetObservers(Collection<Observer> _observerlist){
        _observers = _observerlist;
    }
    
    /**
     * @return Comparator of different Products
     */
    public static Comparator<Product> getComparatorProduct(){
        return COMPAREPRODUCT;
    }

    /**
     * @see Product
     * @return specifific comparator for two different Products.
    */
    private static final Comparator<Product> COMPAREPRODUCT = new Comparator<Product>(){
        @Override
        public int compare(Product a, Product b){
            return ((a.getId().toLowerCase()).compareTo(b.getId().toLowerCase()));
        }
    };

    /**
     * @return String that represents a Product.
    */
    @Override
    public String toString(){
        return _id + "|" + Math.round(this.getMaxPrice()) + "|" + this.getTotalQuantity();
    }

    /**
     * @return boolean value from comparing Products. 
     * Override of the equals method for comparing Products.
    */
    @Override 
    public boolean equals(Object obj){ //Compares different products using their id
       return ((this.getId()).toLowerCase()).equals((((Product) obj).getId()).toLowerCase()); 
        

    }
    /**
     * @return Product hash code. 
     * Override of the hashCode() method for instances of Product.
    */
    @Override
    public int hashCode(){
        return _id.hashCode();
    }

    /**
     * @see Observer
     * @return boolean indicating the success of an addition of an observer to the observer list
     */
    public boolean addObserver(Observer obs) {
        return _observers.add(obs);
    }

    /**
     * @see Observer
     * @return boolean indicating the success of an removal of an observer to the observer list
     */
    public boolean removeObserver(Observer obs) {
        return _observers.remove(obs);
    }
    

    /**
     * @see Observer
     * @see Notification
     */
    public void notifyObservers(Notification n) {
        for (Observer obs : _observers)
            obs.update(n);
    }

    /**
     * @see Batch
     * @param requested quantity of the product for a sale
     */
    public void checkQuantity(int quantity) throws UnsufficientUnitsException{
        int availableAmount = 0;
        for (Batch b : _batches)
        {
            availableAmount += b.getQuantity();
        }
        if (availableAmount < quantity)
            throw new UnsufficientUnitsException(availableAmount, this, quantity); 

    }

    /**
     * @see Batch
     */
    public void removeEmptyBatches(){
        Iterator<Batch> iter = _batches.iterator();
        while(iter.hasNext()){
            if (iter.next().getQuantity() == 0)
                iter.remove();
        }
    }

    /**
     * @see Batch
     * @return price for a batch of a component resulting of a breakdown
     */
    public double getNewBatchPrice(){
    //if the product has available batches
        if (!this.getBatches().isEmpty()){
          List<Batch> productBatches = this.getBatches();
          Collections.sort(productBatches, Batch.getComparatorBatchPrice());
          return (productBatches.get(0).getPrice());
        }
        else{
          return(this.getMaxTransactionPrice());
        }
    }

    /**
     * @return maximum price of a transaction envolving this product
     */
    public double getMaxTransactionPrice(){
        return _maxTransactionPrice;
    }

    /**
     * @param price the new maximum price of a transaction envolving this product 
     */
    public void changeMaxTransactionPrice(double newPrice){
        _maxTransactionPrice = newPrice;
    }

    /**
     * @return boolean indicating if a product can be broke down
     */
    public boolean breakdownable(){
        return false;
    }

    /**
     * @return the number of days used to calculate fines/discounts when paying for 
     * a sale of the product
     */
    public int getNumberOfDays(){
        return _numberOfDays;
    }

    /**
     * @param n number of days used to calculate fines/discounts when paying for 
     * a sale of the product
     */
    public void setNumberOfDays(int n){
        _numberOfDays = n;
    }

}
