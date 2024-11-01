package ggc.core;

import java.io.Serializable;
import java.util.List;
import java.util.Collections;

public abstract class Sale extends Transaction implements Serializable{

    public Sale(Product product, int quantity, int id, Partner partner){
        super(product, quantity, id, partner);

    }

    public double setSalePrice(Product product, Partner partner, int quantity){
    int amount = 0;
    double price = 0;
    List<Batch> productBatches = product.getBatches();
    Collections.sort(productBatches, Batch.getComparatorBatchPrice());
    for(Batch b : productBatches){
      if (amount == quantity)
        break;
      if (b.getPrice() > product.getMaxTransactionPrice())
        product.changeMaxTransactionPrice(b.getPrice());
      int batchQuantity = b.getQuantity();
      int missing = quantity - amount;
      if (batchQuantity >= missing){
        b.removeUnits(missing);
        amount += missing;
        price = price + (b.getPrice() * missing);
      }
      else{
        b.removeUnits(batchQuantity);
        amount += batchQuantity;
        price = price + (b.getPrice() * batchQuantity);
        }

    }
        
        return price;
  }

}