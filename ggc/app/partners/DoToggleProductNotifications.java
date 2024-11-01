package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.*;
import ggc.core.exception.NoSuchProductException;
import ggc.app.exception.UnknownProductKeyException;
import ggc.core.exception.NoSuchPartnerException;
import ggc.app.exception.UnknownPartnerKeyException;


/**
 * Toggle product-related notifications.
 */
class DoToggleProductNotifications extends Command<WarehouseManager> {

  DoToggleProductNotifications(WarehouseManager receiver) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, receiver);
    addStringField("key", Message.requestPartnerKey());
    addStringField("Product", Message.requestProductKey());
    
  }

  @Override
  public void execute() throws CommandException {
   
    String partnerId = stringField("key");
    String productId = stringField("Product");
    boolean observerInList = false;
    try{
      Partner partnerEscolhido = _receiver.getWarehouse().getPartner(partnerId);
      Product productEscolhido = _receiver.getWarehouse().getProduct(productId);
      for(Observer ob: productEscolhido.getObservers()){
        if (ob.equals(partnerEscolhido)){
          observerInList = true;
          break;
        }
      }
      if (observerInList){
        productEscolhido.removeObserver(partnerEscolhido);
      }
      else{
        productEscolhido.addObserver(partnerEscolhido);
      }
    } catch (NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(partnerId);
    }catch (NoSuchProductException ex){
      throw new UnknownProductKeyException(productId);
    }
  }

}
