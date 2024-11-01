package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.*;
import java.util.List;
import java.util.Collection;
import ggc.core.exception.NoSuchPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;


/**
 * Show all transactions for a specific partner.
 */
class DoShowPartnerAcquisitions extends Command<WarehouseManager> {

  DoShowPartnerAcquisitions(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER_ACQUISITIONS, receiver);
    addStringField("partner", Message.requestPartnerKey());
    
  }

  @Override
  public void execute() throws CommandException {
    String id = stringField("partner");
    try{
      Partner partnerEscolhido = _receiver.getWarehouse().getPartner(id);
    
      Collection <Acquisition> list = partnerEscolhido.getAllAcquisitions();
      for (Acquisition a : list ){
        _display.addLine(a.toString());
      }
      _display.display();
    } catch (NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(id);
    }
  }
    

}
