package ggc.app.lookups;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Batch;

/**
 * Lookup products cheaper than a given price.
 */
public class DoLookupProductBatchesUnderGivenPrice extends Command<WarehouseManager> {

  public DoLookupProductBatchesUnderGivenPrice(WarehouseManager receiver) {
    super(Label.PRODUCTS_UNDER_PRICE, receiver);
    addRealField("price", Message.requestPriceLimit());
  }

  @Override
  public void execute() throws CommandException {

    double price = realField("price");
    for (Batch b :_receiver.getWarehouse().getBatchesUnderPrice(price))
      _display.addLine(b.toString());
    _display.display();
  }
}
