package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.exception.NoSuchProductException;
import ggc.core.exception.UnsufficientUnitsException;
import ggc.app.exception.UnavailableProductException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.app.exception.UnknownPartnerKeyException;



/**
 * Register order.
 */
public class DoRegisterBreakdownTransaction extends Command<WarehouseManager> {

  public DoRegisterBreakdownTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_BREAKDOWN_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
    addIntegerField("amount", Message.requestAmount());
  }

  @Override
  public final void execute() throws CommandException {
    String idProduct = stringField("idProduct");
    String idPartner = stringField("idPartner");
    int amount = integerField("amount");
    try{
      _receiver.getWarehouse().doBreakdownsale(_receiver.getWarehouse().getPartner(idPartner),
        _receiver.getWarehouse().getProduct(idProduct), amount);
    }catch(NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(idPartner);
    }catch(NoSuchProductException ex){
      throw new UnknownProductKeyException(idProduct);
    }catch (UnsufficientUnitsException ex){
      throw new UnavailableProductException(idProduct, amount, ex.getAvailableAmount());
    }
  }

}
