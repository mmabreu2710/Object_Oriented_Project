package ggc.core;
import java.util.Comparator;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.io.Serializable;
import java.lang.Math;
import java.util.Collection;
import ggc.core.exception.NoSuchTransactionException;

public class Partner implements Serializable, Observer{
    private String _name;
    private String _address;
    private String _id;
    private PartnerState _partnerState;
    private double _points;
    private List<Batch> _batches;
    private Collection<Notification> _notifications;
    private Collection<Acquisition> _allAcquisitions;
    private Collection<Sale> _allSales;
    private double _acquisitionValue;
    private double _madeSalesValue;
    private double _paidSalesValue;
    private DeliveryNotificationMode _deliveryNotificationMode;



    public Partner(String name, String address, String id){
        _name = name;
        _address = address;
        _id = id;
        _partnerState = (new NormalState());
        _batches = new ArrayList<>();
        _notifications = new ArrayList<>(); 
        _allAcquisitions = new ArrayList<>();  
        _allSales = new ArrayList<>();    
        _deliveryNotificationMode = new DeliverNotification();
    }
    public String getName(){
        return _name;
    }
    public String getAdress(){
        return _address;
    }
    public String getId(){
        return _id;
    }

    public DeliveryNotificationMode getDeliveryNotificationMode(){
        return _deliveryNotificationMode;
    }




    public void setState(PartnerState state){
        _partnerState = state;
    }


    public double getPoints(){
        return _points;
    }
    public Collection<Sale> getAllSales(){
        return _allSales;
    }

    public Collection<Sale> getPaidSales(){
        Collection<Sale> paidSales = new ArrayList<>();
        for (Sale s : _allSales){
            if(s.isPaid())
                paidSales.add(s);
        }
        return paidSales;
    }

    public void removePoints(double points){
        _points -= points;
    }

    public void addPoints(double points){
        _points += points;
    }

    public void addBatch(Batch batch)
    {
        _batches.add(batch);
    }
    public void addNotification(Notification n){
        _notifications.add(n);
    }
    public void addAcquisition(Acquisition a){
        _allAcquisitions.add(a);
    }
    public void addSale(Sale sale){
        _allSales.add(sale);
    }

    public SaleByCredit getSale(int idTransaction) throws NoSuchTransactionException{
        Transaction transaction = null;
        for (Sale s: _allSales){
            if (s.getId() == idTransaction){
                transaction = s;
            }   
        }

    if (transaction == null)
      throw new NoSuchTransactionException();
        return (SaleByCredit)transaction;
    }

   @Override 
    public boolean equals(Object obj){ //Compares different products using their id

        return (this.getId()).toLowerCase().equals((((Partner) obj).getId()).toLowerCase()); 
    }

    public List<Batch> getBatches(){
        this.removeEmptyBatches();
        return _batches;
    }
    public Collection<Notification> getNotifications(){
        return _notifications;
    }
    public void removeAllNotifications(){
        _notifications.clear();
    }
    public Collection<Acquisition> getAllAcquisitions(){
        return _allAcquisitions;
    }
    @Override
    public int hashCode(){
        return _id.hashCode();
    }

    public static Comparator<Partner> getComparatorPartner(){
        return COMPAREPARTNER;
    }

    private static final Comparator<Partner> COMPAREPARTNER = new Comparator<Partner>(){
        @Override
        public int compare(Partner a, Partner b){
            return ((a.getId().toLowerCase()).compareTo(b.getId().toLowerCase()));
        }
    };

    @Override
    public String toString(){ 
        return _id + "|" + _name + "|" + _address + "|" + _partnerState.toString() + "|" + Math.round(this.getPoints()) + "|" 
        + Math.round(this.getAcquisitionValue()) + "|" + Math.round(this.getMadeSalesValue()) + "|" + Math.round(this.getPaidSalesValue());

    }

    public double getAcquisitionValue(){
        return _acquisitionValue;
    }
    
    public double getMadeSalesValue(){
        return _madeSalesValue;
    }
    
    public double getPaidSalesValue(){
        return _paidSalesValue;
    }
    public void addAcquisitionValue(double price){
        _acquisitionValue += price;
    }

    public void addPaidSalesValue(double price){
        _paidSalesValue += price;
    }

    public void addMadeSalesValue(double price){
        _madeSalesValue += price;
    }

    public void removeEmptyBatches(){
        Iterator<Batch> iter = _batches.iterator();
        while(iter.hasNext()){
            if (iter.next().getQuantity() == 0)
                iter.remove();
        }
    }

    
    public void update(Notification n){
        _deliveryNotificationMode.addNotification(n, _notifications);
    }

    public void setDeliveryNotificationMode(DeliveryNotificationMode d){
        _deliveryNotificationMode = d;
    }
    

    public void checkState(){
        _partnerState.checkState(_points, this);
    }


    public double paySale(int currentDate, SaleByCredit sale, boolean payingNow){
        int deadline = sale.getDeadLine();
        Product product = sale.getProduct();
        int n = product.getNumberOfDays();
        double salePrice = 0;
        if ((deadline - currentDate) >= n){
            salePrice = _partnerState.paySaleP1(sale);
            if (payingNow)
                this.addPoints(10 * salePrice);
        }
        else if((0 <= (deadline - currentDate) && (deadline - currentDate) < n)){
            salePrice = _partnerState.paySaleP2(sale, (deadline - currentDate));
            if (payingNow)
                this.addPoints(10 * salePrice);
        }
        else if(0 < (currentDate - deadline) && (currentDate - deadline) <= n){
            salePrice = _partnerState.paySaleP3(sale, (currentDate - deadline));
            if (payingNow){
                double deductingPoints = _partnerState.deductPoints((currentDate - deadline), 
                    this.getPoints(), this);
                this.removePoints(deductingPoints);
            }
        }
        else if((currentDate - deadline) > n){
            salePrice = _partnerState.paySaleP4(sale, (currentDate - deadline));
            if (payingNow){
                double deductingPoints = _partnerState.deductPoints((currentDate - deadline), 
                    this.getPoints(), this);
                this.removePoints(deductingPoints);
            }
        }
        if (payingNow){
            this.addPaidSalesValue(salePrice);
            this.checkState();
        }

        return salePrice;
    }

}
