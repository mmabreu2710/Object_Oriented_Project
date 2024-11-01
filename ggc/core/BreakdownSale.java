package ggc.core;

import java.io.Serializable;
import java.util.Collection;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;


public class BreakdownSale extends Sale implements Serializable{
    private double _amountPaid;
    private Collection<Batch> _batches;

    public BreakdownSale(Product product, int quantity, int id, Partner partner, int date){
        super(product, quantity, id, partner);
        super.setBaseValue(this.setSalePrice(product, partner, quantity));
        this.setPaymentDate(date);
        super.receivePayment();
    }

    public void setAmountPaid(double price){
        _amountPaid = price;
    }

    @Override
    public double setSalePrice(Product product, Partner partner, int quantity){
        double price = super.setSalePrice(product, partner, quantity);
        double priceOfComponents = 0;
        Recipe recipe = ((AggregateProduct)product).getRecipe();
        List<Batch> componentBatches = new ArrayList<>();
        Collection<Component> components = recipe.getComponents();
      for (Component c : components){
        Product componentProduct = c.getProduct();
        int units = c.getQuantity() * quantity;
        double componentPrice = componentProduct.getNewBatchPrice(); 
        Batch batch = new Batch(componentProduct, partner, componentPrice, units);
        componentProduct.addBatch(batch);
        partner.addBatch(batch);
        componentBatches.add(batch);
        priceOfComponents += componentPrice * units;
      }
      this.setBatches(componentBatches);
      double breakdownPrice = price - priceOfComponents;
      return breakdownPrice;
    }

    public void setBatches(Collection<Batch> batches){
        _batches = batches;

    }

    public double getAmountpaid(){
        return _amountPaid;
    }
    @Override
    public boolean isPaid(){
        return true;
    }
    @Override
    public String toString(){
        String breakdown = "DESAGREGAÇÃO" + "|" + this.getId() + "|" + this.getPartner().getId() + "|" + this.getProduct().getId()+ "|" +
            this.getQuantity() + "|" + Math.round(this.getBaseValue()) + "|" + Math.round(_amountPaid)
                + "|" + this.getPaymentDate() + "|";
        for(Batch b : _batches){
            breakdown += b.getProduct().getId() + ":" + b.getQuantity() + ":" + Math.round(b.getPrice() * b.getQuantity()) + "#";
        }

        breakdown = breakdown.substring(0, breakdown.length() - 1);

        return breakdown;
    }
}
