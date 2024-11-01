package ggc.core;

import java.io.Serializable;
import java.lang.Math;

public class Acquisition extends Transaction implements Serializable{

    public Acquisition(Product product,Partner partner, int quantity, int id, int currentDate, 
        double baseValue){
        super(product, quantity, id, partner);
        this.setPaymentDate(currentDate);
        this.setBaseValue(baseValue);
        super.receivePayment();
    }


    @Override
    public String toString(){

        return "COMPRA" + "|" + this.getId() + "|" + this.getPartner().getId() + "|" + this.getProduct().getId()+ "|" +
        this.getQuantity() + "|" + Math.round(this.getBaseValue()) + "|" + this.getPaymentDate();
    }
}