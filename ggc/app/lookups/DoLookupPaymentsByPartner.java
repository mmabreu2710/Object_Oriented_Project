package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Sale;
import ggc.core.exception.NoSuchPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;


/**
 * Lookup payments by given partner.
 */
public class DoLookupPaymentsByPartner extends Command<WarehouseManager> {

  public DoLookupPaymentsByPartner(WarehouseManager receiver) {
    super(Label.PAID_BY_PARTNER, receiver);
    addStringField("partner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
  
    try{
      for (Sale s :_receiver.getWarehouse().getPaymentsByPartner(stringField("partner"))){
        _display.addLine(s.toString());
      }
      _display.display();
    }catch(NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(stringField("partner"));
    }
  }

}
