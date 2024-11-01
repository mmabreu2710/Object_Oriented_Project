package ggc.core;

import java.io.Serializable;
import java.io.IOException;
import ggc.core.exception.BadEntryException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import ggc.core.exception.UnavailableFileException;
import ggc.core.Product;
import ggc.core.Partner;
import ggc.core.exception.NoSuchProductException;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.exception.PartnerAlreadyExistsException;
import ggc.core.exception.ProductAlreadyExistsException;
import ggc.app.exception.UnknownTransactionKeyException;
import java.util.Collection;
import ggc.core.exception.NoSuchTransactionException;
import ggc.core.exception.UnsufficientUnitsException;
import java.lang.Math;



/**
 * Class Warehouse implements a warehouse.
 */
public class Warehouse implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202109192006L;

  //current date
  private int _date;

  //Partner collection
  private Set<Partner> _partners = new HashSet<>();

  //Product collection, tenho de ver o que preciso de ter aqui
  private Set<Product> _products = new HashSet<>();

  //represents the number of transactions made by the warehouse
  private int _numberOfTransactions = -1;

  private Collection<Transaction> _transactions = new ArrayList<>();

  private double _globalBalance;

  private double _balance;

  public double getGlobalBalance(){
    return _globalBalance;
  }

  public double getBalance(){
    return _balance;
  }
  public void advanceDate(int days){

    _date = _date + days;
  }

  public int getDate(){
    return _date;
  }

  public void addPartner(Partner partner) throws PartnerAlreadyExistsException{
    if (!hasPartner(partner.getId()))
      _partners.add(partner);
    else
      throw new PartnerAlreadyExistsException();
  }
  public void addProduct(Product product) throws ProductAlreadyExistsException{
    if (!hasProduct(product.getId())){
      _products.add(product);
      for (Partner p : _partners){
        product.addObserver(p);
      }
    }
    else
      throw new ProductAlreadyExistsException();
  }

  public boolean hasProduct(String id){
    for (Product p : _products){
      if ((id.toLowerCase()).equals(p.getId().toLowerCase()))
        return true;
    }
    return false;
  }
  public boolean hasPartner(String id){
    for (Partner p : _partners){
      if ((id.toLowerCase()).equals(p.getId().toLowerCase()))
        return true;
    }
    return false;
  }

  public Product getProduct(String id) throws NoSuchProductException{
    Product specificproduct = null;
    for (Product product : _products){
      if (product.getId().equals(id)){
        specificproduct = product;
        break;
      }
    }
    if (specificproduct == null)
      throw new NoSuchProductException();
    return specificproduct;
  }

  public Partner getPartner(String id) throws NoSuchPartnerException{
    Partner specificPartner = null;
    for (Partner partner : _partners)
      if (partner.getId().toLowerCase().equals(id.toLowerCase())){
        specificPartner = partner;
        break;
      }
    if (specificPartner == null)
      throw new NoSuchPartnerException();
    return specificPartner;
  }

  public Set<Partner> getAllPartners(){
    return _partners;
  }

  public Set <Product> getAllProducts(){
    return _products;
  }

  public void addTransaction(Transaction transaction){
    if (!hasTransaction(transaction.getId()))
      _transactions.add(transaction);
  }

  public boolean hasTransaction(int id){
    return (id <= _numberOfTransactions);
  }

  public Transaction getTransaction(int idTransaction) throws NoSuchTransactionException{
    Transaction transaction = null;
    for (Transaction t: _transactions){
      if (t.getId() == idTransaction){
        transaction = t;
      }
    }
    if (transaction == null)
      throw new NoSuchTransactionException();
    if (transaction instanceof SaleByCredit){
      ((SaleByCredit)transaction).setAmountToPay(_date);
    }
    return transaction;
  }
  
  public String DoShowTransaction(int key) throws NoSuchTransactionException{
    
    return (this.getTransaction(key).toString());
  }


  public void doAcquisition(Product product, int quantity,  Partner partner, double price){
    _numberOfTransactions++;
    Double baseValue = price * quantity;
    Acquisition acquisition = new Acquisition(product, partner, quantity, _numberOfTransactions, _date, 
      baseValue);
    Batch batch = new Batch(product, partner, price, quantity);
    if (!this.hasProduct(product.getId())){
      product.notifyObservers(new Notification(product,batch,NotificationType.NEW));
    }
    if (product.getMinPrice() > price){
      product.notifyObservers(new Notification(product,batch,NotificationType.BARGAIN)); 
    }
    if (product.getMaxTransactionPrice() < price)
      product.changeMaxTransactionPrice(price);
    product.addBatch(batch);
    partner.addBatch(batch);
    partner.addAcquisition(acquisition);
    _transactions.add(acquisition);
    partner.addAcquisitionValue(baseValue);
    _balance -= baseValue;
    _globalBalance -= baseValue;
  }
  public void doSaleByCredit(Product product, int quantity, Partner partner, int deadline) throws UnsufficientUnitsException{ 

    
    //if the product doesn't exist in the necessary amount this method throws an exception
    product.checkQuantity(quantity); 
    //ordering the batches of the product by price
    _numberOfTransactions++;
    SaleByCredit sale = new SaleByCredit(quantity, product, partner, deadline, _numberOfTransactions);
    _transactions.add(sale);
    partner.addSale(sale);
    double price = sale.getBaseValue();
    partner.addMadeSalesValue(price);
    _balance += price;
  
  }
  public void doBreakdownsale(Partner partner, Product product, int quantity) throws UnsufficientUnitsException{
    if (product.breakdownable()){
      ((AggregateProduct)product).checkBreakdownQuantity(quantity);
      _numberOfTransactions++;
      BreakdownSale sale = new BreakdownSale(product, quantity, _numberOfTransactions, partner, _date);
      partner.addSale(sale);
      _transactions.add(sale);
      double breakdownPrice = sale.getBaseValue();
      if (breakdownPrice > 0){
        partner.addMadeSalesValue(breakdownPrice);
        partner.addPaidSalesValue(breakdownPrice);
        _balance += breakdownPrice;
        _globalBalance += breakdownPrice;
        partner.addPoints(breakdownPrice * 10);
        sale.setAmountPaid(breakdownPrice);
      }
    }
  }


  public Collection<Batch> getBatchesUnderPrice(double price){
    for (Partner p : _partners)
      p.removeEmptyBatches();

    List<Batch> batchesUnderPrice = new ArrayList<>();
    for(Product p : _products){
      p.removeEmptyBatches();
      for(Batch b : p.getBatches()){
        if (b.getPrice() < price){
          batchesUnderPrice.add(b);
        }
      }
    }
    Collections.sort(batchesUnderPrice, Batch.getComparatorBatch());
    return batchesUnderPrice;
  }

  public void doReceivePayment(int idTransaction) throws NoSuchTransactionException{
    if (idTransaction < 0 || (idTransaction > _numberOfTransactions)){
      throw new NoSuchTransactionException();
    }

    Transaction transaction = this.getTransaction(idTransaction);
    Partner partner = transaction.getPartner();
    SaleByCredit sale = partner.getSale(idTransaction);
    double paidPrice = partner.paySale(_date, sale, true);
    sale.pay(paidPrice, _date);
    _globalBalance += paidPrice;
  }
  
  public Collection<Sale> doShowPartnerSales(Partner p) throws NoSuchPartnerException{
    if(!hasPartner(p.getId()))
      throw new NoSuchPartnerException();
    Collection<Sale> partnerSales = p.getAllSales();
    for (Sale s: partnerSales){
        if(s instanceof SaleByCredit)
          ((SaleByCredit)s).setAmountToPay(_date);
    }
    return partnerSales;
  }
  
  public Collection<Sale> getPaymentsByPartner(String idPartner) throws NoSuchPartnerException{
      Partner partner = this.getPartner(idPartner);
      return partner.getPaidSales();
  }

  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  void importFile(String txtfile) throws IOException, BadEntryException, NoSuchProductException, NoSuchPartnerException,
  PartnerAlreadyExistsException, ProductAlreadyExistsException {
    Parser parser = new Parser(this);
    parser.parseFile(txtfile);
  }

}