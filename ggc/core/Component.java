package ggc.core;

import java.io.Serializable;
import java.util.Comparator;

public class Component implements Serializable{
    private Product _product;
    private int _quantity;
    public Component(Product product, int quantity){
        _product = product;
        _quantity = quantity;
    }
    public Product getProduct(){
        return _product;
    }
    public int getQuantity(){
        return _quantity;
    }
    public static Comparator<Component> getComparatorComponents(){
        return COMPARECOMPONENTS;
    }
    
    private static final Comparator<Component> COMPARECOMPONENTS = new Comparator<Component>(){
        @Override
        public int compare(Component a, Component b){
            return a.getQuantity()-b.getQuantity();
        }
    };
    
}