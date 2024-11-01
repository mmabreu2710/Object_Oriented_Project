package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.UnsufficientUnitsException;
import ggc.app.exception.UnavailableProductException;
import ggc.core.exception.NoSuchProductException;
import ggc.core.exception.NoSuchPartnerException;



/**
 * 
 */
public class DoRegisterSaleTransaction extends Command<WarehouseManager> {

  public DoRegisterSaleTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addIntegerField("deadline", Message.requestPaymentDeadline());
    addStringField("idProduct", Message.requestProductKey());
    addIntegerField("amount", Message.requestAmount());

    
  }

  @Override
  public final void execute() throws CommandException {
   

    String idPartner = stringField("idPartner");
    int deadline = integerField("deadline");
    String idProduct = stringField("idProduct");
    int amount = integerField("amount");
    try{
      _receiver.getWarehouse().doSaleByCredit(_receiver.getWarehouse().getProduct(idProduct), amount, 
        _receiver.getWarehouse().getPartner(idPartner), deadline);
    } catch (UnsufficientUnitsException ex){
      throw new UnavailableProductException(ex.getProductId(), ex.getNeededAmount(), ex.getAvailableAmount());
    } catch (NoSuchProductException ex){
      System.err.println(ex);
    } catch (NoSuchPartnerException ex){
      System.err.println(ex);
    }


  }

}
