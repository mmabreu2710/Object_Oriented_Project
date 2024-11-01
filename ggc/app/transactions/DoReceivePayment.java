package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.exception.NoSuchTransactionException;
import ggc.app.exception.UnknownTransactionKeyException;


/**
 * Receive payment for sale transaction.
 */
public class DoReceivePayment extends Command<WarehouseManager> {

  public DoReceivePayment(WarehouseManager receiver) {
    super(Label.RECEIVE_PAYMENT, receiver);
    addIntegerField("idTransaction", Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws CommandException {
    int id = integerField("idTransaction");
    try{
      _receiver.getWarehouse().doReceivePayment(id);
    }catch(NoSuchTransactionException e){
      throw new UnknownTransactionKeyException(id);
    }
  }

}