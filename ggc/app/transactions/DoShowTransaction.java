package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoSuchTransactionException;
import ggc.app.exception.UnknownTransactionKeyException;


/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<WarehouseManager> {

  public DoShowTransaction(WarehouseManager receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    addIntegerField("key",Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    try{
      _display.addLine(_receiver.getWarehouse().DoShowTransaction(integerField("key")));
      _display.display();
    } catch(NoSuchTransactionException ex){
      throw new UnknownTransactionKeyException(integerField("key"));
    }
  }

}
