package ggc.core;

import java.io.Serializable;

public class NormalState extends PartnerState implements Serializable{


	
	public void nextState(Partner partner){
		partner.setState(new SelectionState());
	}

	
	public void prevState(Partner partner){
		//do nothing
	}


	
	public double paySaleP2(Sale sale, int daysBefore){
		return (sale.getBaseValue());
	}

	
	public double paySaleP3(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		double fine = baseValue * 0.05;
		return (baseValue + (fine * daysAfter));
	}

	
	public double paySaleP4(Sale sale, int daysAfter){
		double baseValue = sale.getBaseValue();
		double fine = baseValue * 0.10;
		return (baseValue + (fine * daysAfter));
	}

	
	public String toString(){
		return "NORMAL";
	}

	
	public double deductPoints(int daysAfter, double currentPoints, Partner partner){ 
		return currentPoints;
	}

	
	public void checkState(double points, Partner partner){
		if (points >= 2000)
			this.nextState(partner);
	}

}