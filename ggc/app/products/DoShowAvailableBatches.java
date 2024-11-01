package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ggc.core.Batch;
import ggc.core.Product;

/**
 * Show available batches.
 */
class DoShowAvailableBatches extends Command<WarehouseManager> {

  DoShowAvailableBatches(WarehouseManager receiver) {
    super(Label.SHOW_AVAILABLE_BATCHES, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    List <Batch> _avaiableBatches = new ArrayList<>();
    for (Product p : _receiver.getWarehouse().getAllProducts()){
      for (Batch b : p.getBatches()){
        _avaiableBatches.add(b);
      }
    }
    Collections.sort(_avaiableBatches, Batch.getComparatorBatch());
    for (Batch b : _avaiableBatches){
      _display.addLine(b.toString());
    }
    _display.display();
  }

}
