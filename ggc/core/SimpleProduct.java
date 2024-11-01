package ggc.core;

import java.io.Serializable;

public class SimpleProduct extends Product implements Serializable{

	public SimpleProduct(String id){
		super(id);
		this.setNumberOfDays(5);
	}

	
}