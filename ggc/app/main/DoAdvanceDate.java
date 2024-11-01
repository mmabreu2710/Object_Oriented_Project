package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.main.Message;
import ggc.app.exception.InvalidDateException;

/**
 * Advance current date.
 */
class DoAdvanceDate extends Command<WarehouseManager> {

  /** @param receiver */
  DoAdvanceDate(WarehouseManager receiver) {
    super(Label.ADVANCE_DATE, receiver);
    addIntegerField("number", Message.requestDaysToAdvance());
  }

  @Override
  public final void execute() throws CommandException {

    Integer number = integerField("number");

    if (number < 0)
      throw new InvalidDateException(number);

    _receiver.getWarehouse().advanceDate(number);
  }

}
