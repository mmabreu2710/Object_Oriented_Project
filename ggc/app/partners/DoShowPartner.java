package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.Partner;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.core.*;

/**
 * Show partner.
 */
class DoShowPartner extends Command<WarehouseManager> {

  DoShowPartner(WarehouseManager receiver) {
    super(Label.SHOW_PARTNER, receiver);
    addStringField("key", Message.requestPartnerKey());
  }

  @Override
  public void execute() throws CommandException {
    String id = stringField("key");
    try{
      Partner partner = _receiver.getWarehouse().getPartner(id);
      _display.addLine(partner.toString());
      for (Notification n : partner.getNotifications()){
        _display.addLine(n.toString());
      }
      _display.display();
      partner.removeAllNotifications();
    } catch(NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(id);
    }
  
  }

}
