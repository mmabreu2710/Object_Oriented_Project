package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Product;
import ggc.core.Batch;
import ggc.core.exception.NoSuchProductException;
import ggc.app.exception.UnknownProductKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Show all products.
 */
class DoShowBatchesByProduct extends Command<WarehouseManager> {

  DoShowBatchesByProduct(WarehouseManager receiver) {
    super(Label.SHOW_BATCHES_BY_PRODUCT, receiver);
    addStringField("Product", Message.requestProductKey());
  }

  @Override
  public final void execute() throws CommandException {
    String id = stringField("Product");
    try{
      Product productEscolhido = _receiver.getWarehouse().getProduct(id);
    
      List<Batch> list = productEscolhido.getBatches();
      Collections.sort(list, Batch.getComparatorBatch());
      for (Batch b : list)
        _display.addLine(b.toString());
      _display.display();
    } catch (NoSuchProductException ex){
      throw new UnknownProductKeyException(id);
    }
  }

}
