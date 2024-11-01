package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Partner;
import ggc.app.exception.UnknownPartnerKeyException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import ggc.core.Batch;
import ggc.core.exception.NoSuchPartnerException;

/**
 * Show batches supplied by partner.
 */
class DoShowBatchesByPartner extends Command<WarehouseManager> {

  DoShowBatchesByPartner(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_SUPPLIED_BY_PARTNER, receiver);
    addStringField("Partner", Message.requestPartnerKey());
  }

  @Override
  public final void execute() throws CommandException {
  
    String id = stringField("Partner");
    try{
      Partner partnerEscolhido = _receiver.getWarehouse().getPartner(id);
    
      List<Batch> list = partnerEscolhido.getBatches();
      Collections.sort(list, Batch.getComparatorBatch());
      for (Batch b : list )
        _display.addLine(b.toString());
      _display.display();
    } catch (NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(id);
    }
  }

}
