package ggc.core;

import java.io.Serializable;

public class SelectionState extends PartnerState implements Serializable{


	
	public void nextState(Partner partner){
		partner.setState(new EliteState());
	}

	
	public void prevState(Partner partner){
		partner.setState(new NormalState());
	}


	
	public double paySaleP2(Sale sale, int daysBefore){
		double discount = 0;
		double baseValue = sale.getBaseValue();
		if (daysBefore >= 2){
			discount = 0.05 * baseValue;
		}
		return (baseValue - discount);
	}

	
	public double paySaleP3(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		double fine = 0;
		if (daysAfter > 1)
			fine = baseValue * 0.02;
		return (baseValue + (fine * daysAfter));
	}

	
	public double paySaleP4(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		double fine = baseValue * 0.05;
		return (baseValue + (fine * daysAfter));
	}

	
	public String toString(){
		return "SELECTION";
	}

	
	public double deductPoints(int daysAfter, double currentPoints, Partner partner){ 
		if (daysAfter > 9){
			this.prevState(partner);
			return (0.90 * currentPoints);
		}
		return 0;
	}

	
	public void checkState(double points, Partner partner){
		if (points > 25000)
			this.nextState(partner);
		else if (points < 2000)
			this.prevState(partner);
	}



}