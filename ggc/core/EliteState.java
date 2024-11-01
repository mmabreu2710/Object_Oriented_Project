package ggc.core;

import java.io.Serializable;

public class EliteState extends PartnerState implements Serializable{


	
	public void nextState(Partner partner){
		//do nothing
	}

	
	public void prevState(Partner partner){
		partner.setState(new SelectionState());
	}


	
	public double paySaleP2(Sale sale, int daysBefore){
		
		double baseValue = sale.getBaseValue();
		double discount = baseValue * 0.10;
		return (baseValue - discount);
	}

	
	public double paySaleP3(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		double discount = baseValue * 0.05;
		return (baseValue - discount);
	}

	
	public double paySaleP4(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		return baseValue;
	}

	
	public String toString(){
		return "ELITE";
	}

	
	public double deductPoints(int daysAfter, double currentPoints, Partner partner){ 
		if (daysAfter > 15){
			this.prevState(partner);
			return (0.75 * currentPoints);
		}
		return 0;
	}

	
	public void checkState(double points, Partner partner){
		if (points <= 25000)
			this.prevState(partner);
	}

}