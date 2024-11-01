package ggc.core;

import java.io.Serializable;
import ggc.core.Partner;
import ggc.core.Product;

//ver pormenores nas compras,vendas e desagregacoes, ainda nao ta tudo feito
//ha coisas que é preciso corrigir sobre o preco final em Vendas 
//é preciso ver em pormenor as Breakdownsale porque ha coisas imcompletas

public abstract class Transaction implements Serializable{
    //private static int _numberOfTransaction = 0; vou meter este no warehouse porque acho que não é 
    //suposto termos cenas estáticas no código sem que seja dito explicitamente
    private int _id;
    private int _paymentDate;
    private double _baseValue;
    private int _quantity;
    private boolean _isPaid;
    private Product _product;
    private Partner _partner;
    //private Batch _batch; acho que não é preciso aqui ass Bernardo

    public Transaction (Product product, int quantity, int id, Partner partner){
        _product = product;
        _quantity = quantity;
        _id = id;
        _partner = partner;
    }
    public int getId(){
        return _id;
    }
    public void setPaymentDate(int date){
        _paymentDate = date;
    }

    public void receivePayment(){
        _isPaid = true;
    }


    public int getPaymentDate(){
        return _paymentDate;
    }

    public Partner getPartner(){
        return _partner;
    }

    public double getBaseValue(){
        return _baseValue;
    }
    public int getQuantity(){
        return _quantity;
    }
    public boolean isPaid(){
        return _isPaid;
    }
   /* public void isFinallyPaid(){
        _isPaid = true;
    } */
    public Product getProduct(){
        return _product;
    }

    public void setBaseValue(double value){
        _baseValue = value;
    }
}
