package ggc.core;

import java.io.Serializable;
import java.lang.Math;
import java.util.Comparator;

public class Batch implements Serializable{
    private Product _product;
    private Partner _supplier;
    private double _pricePerUnit;
    private int _quantity;
    
    public Batch(Product product, Partner supplier, double price, int quantity){
        _product = product;
        _supplier = supplier;
        _pricePerUnit = price;
        _quantity = quantity;
    }

    /**
     * @return Individual price of the Product in the Batch.
    */
    public double getPrice(){
        return _pricePerUnit;
    }

    /**
     * @return Quantity of a product in a batch.
    */
    public int getQuantity(){
        return _quantity;
    }
    /**
     * @see Product
     * @return Product of the batch.
    */
    public Product getProduct(){
        return _product;
    }
    /**
     * @see Partner
     * @return Supplier of the batch.
    */
    public Partner getPartner(){
        return _supplier;
    }
    /**
     * @return String that represents a batch.
    */
    @Override
    public String toString(){

        return _product.getId() + "|" + _supplier.getId() + "|" +
        Math.round(this.getPrice()) + "|" + _quantity;
    }

    public static Comparator<Batch> getComparatorBatch(){
        return COMPAREBATCH;
    }


    private static final Comparator<Batch> COMPAREBATCH = new Comparator<Batch>(){
        /**
         * @return int representing the result of comparing two different batches
         */
        @Override
        public int compare(Batch a, Batch b){
            int comparaProduto = (a.getProduct().getId().compareTo(b.getProduct().getId()));
            if (comparaProduto != 0)
                return comparaProduto;
            int comparaParceiro = (a.getPartner().getId().compareTo(b.getPartner().getId()));
            if (comparaParceiro != 0)
                return comparaParceiro;
           if (a.getPrice() > b.getPrice())
                return 1;
            else if(a.getPrice() < b.getPrice())
                return -1;
            return (a.getQuantity() - b.getQuantity());
            
        }
    };

    public static Comparator<Batch> getComparatorBatchPrice(){
        return COMPAREBATCHPRICE;
    }

    private static final Comparator<Batch> COMPAREBATCHPRICE = new Comparator<Batch>(){
        /**
         * @return int representing the result of comparing two different batches by price only
         */
        @Override
        public int compare(Batch a, Batch b){
           if (a.getPrice() > b.getPrice())
                return 1;
            return -1;
        }
    };

    /**
    * @param units of products to remove from a product batch
    */
    public void removeUnits(int units){
        _quantity = _quantity - units;
    }


}