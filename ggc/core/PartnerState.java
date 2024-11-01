package ggc.core;

public abstract class PartnerState{
	public abstract void nextState(Partner partner);
	public abstract void prevState(Partner partner);
	
	public double paySaleP1(Sale sale){
		return (sale.getBaseValue() - (sale.getBaseValue() * 0.10));
	}
	
	public abstract double paySaleP2(Sale sale, int daysBefore);
	public abstract double paySaleP3(Sale sale, int daysAfter);
	public abstract double paySaleP4(Sale sale, int daysAfter);
	public abstract double deductPoints(int daysAfter, double currentPoints, Partner partner);
	public abstract void checkState(double points, Partner partner);

}