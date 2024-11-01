package ggc.core;

import java.io.Serializable;
import java.lang.Math;
//esboco das Sales ainda falta ver pormenores
public class SaleByCredit extends Sale implements Serializable{
    private int _deadLine;
    private double _amountPaid;
    private double _amountToPay;

    public SaleByCredit(int quantity, Product product, Partner partner, int deadline,
        int id){

        super(product, quantity, id, partner);
        _deadLine = deadline;
        super.setBaseValue(super.setSalePrice(product, partner, quantity));
    }
    public int getDeadLine(){
        return _deadLine;
    }
    public double getAmountpaid(){
        return _amountPaid;
    }

    public void pay(double price, int date){
        this.setPaymentDate(date);
        _amountPaid = price;
    }

    public void setAmountToPay(int currentDate){
        Partner partner = this.getPartner();
        _amountToPay = (partner.paySale(currentDate, this, false));
    }

    


    @Override
    public boolean isPaid(){
        return (_amountPaid != 0);
    }
    @Override
    public String toString(){
        String venda = "VENDA" + "|" + this.getId() + "|" + this.getPartner().getId() + "|" + this.getProduct().getId()+ "|" +
            this.getQuantity() + "|" + Math.round(this.getBaseValue()) ;
        if (this.isPaid()){
            venda += "|" + Math.round(this.getAmountpaid())
                + "|" + this.getDeadLine() + "|" + this.getPaymentDate();
        }
        else
            venda += "|" + Math.round(_amountToPay)
                + "|" + this.getDeadLine();

        return venda;
    }
}
