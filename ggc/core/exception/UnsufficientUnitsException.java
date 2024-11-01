package ggc.core.exception;
import ggc.core.Product;


//Exception thrown when a product does not have sufficient quantity required for a sale
public class UnsufficientUnitsException extends Exception{
	private int _availableAmount;
	private Product _product;
	private int _neededAmount;


	public UnsufficientUnitsException(int amount, Product product, int needed){
		_availableAmount = amount;
		_product = product;
		_neededAmount = needed;


	}

	public int getAvailableAmount(){
		return _availableAmount;
	}

	public String getProductId(){
		return _product.getId();
	}

	public int getNeededAmount(){
		return _neededAmount;
	} 


}