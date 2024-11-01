package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Sale;
import java.util.Collection;
import ggc.core.exception.NoSuchPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;


/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerSales extends Command<WarehouseManager> {

  DoShowPartnerSales(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_SALES, receiver);
    addStringField("partner", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    try{
      Collection<Sale> allSales = _receiver.getWarehouse().doShowPartnerSales(_receiver.getWarehouse().getPartner(stringField("partner")));
      for (Sale s: allSales){
        _display.addLine(s.toString());
      }
      _display.display();
    }catch(NoSuchPartnerException e){
      throw new UnknownPartnerKeyException(stringField("partner"));
    }
  }

}
