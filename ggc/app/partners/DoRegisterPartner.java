package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.PartnerAlreadyExistsException;
import ggc.core.Partner;
import ggc.app.exception.DuplicatePartnerKeyException;

/**
 * Register new partner.
 */
class DoRegisterPartner extends Command<WarehouseManager> {

  DoRegisterPartner(WarehouseManager receiver) {
    super(Label.REGISTER_PARTNER, receiver);
    addStringField("key", Message.requestPartnerKey());
    addStringField("name", Message.requestPartnerName());
    addStringField("address", Message.requestPartnerAddress());

  }

  @Override
  public void execute() throws CommandException {
    String id = stringField("key");
    String name = stringField("name");
    String address = stringField("address");

    
    try{
      Partner partner = new Partner(name, address, id);
      _receiver.getWarehouse().addPartner(partner);
      } catch (PartnerAlreadyExistsException exe){
        throw new DuplicatePartnerKeyException(id);
      }
  }

}
