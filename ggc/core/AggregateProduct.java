package ggc.core;

import java.io.Serializable;
import java.lang.Math;
import ggc.core.exception.UnsufficientUnitsException;
import java.util.Collection;

public class AggregateProduct extends Product implements Serializable{
    private Recipe _recipe;

    public AggregateProduct(String id, Recipe recipe){
		super(id);
        _recipe = recipe;
        this.setNumberOfDays(3);
	}
    
    @Override
    public String toString(){
        return this.getId() + "|" + Math.round(this.getMaxPrice())  + "|" + this.getTotalQuantity()  + "|" + _recipe.toString();
    }

    
    public Recipe getRecipe(){
        return _recipe;
    }

    @Override
    public boolean breakdownable(){
        return true;
    }

    @Override
    public void checkQuantity(int quantity) throws UnsufficientUnitsException{
        int availableAmount = 0;
        for (Batch b : this.getBatches())
        {
            availableAmount += b.getQuantity();
        }
        if (availableAmount < quantity){
            int missingAmount = quantity - availableAmount;
            Collection<Component> components = _recipe.getComponents();
            for (Component c : components){
                int neededAmountComponent = missingAmount * c.getQuantity();
                if (neededAmountComponent > c.getProduct().getTotalQuantity()){
                    throw new UnsufficientUnitsException(c.getProduct().getTotalQuantity(),c.getProduct(), neededAmountComponent);                    }
                }
            } 
        
    }

    public void checkBreakdownQuantity(int quantity)throws UnsufficientUnitsException{
        int availableAmount = 0;
        for (Batch b : this.getBatches())
        {
            availableAmount += b.getQuantity();
        }
        if (availableAmount < quantity)
            throw new UnsufficientUnitsException(availableAmount, this, quantity); 
        }
}


