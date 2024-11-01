package ggc.app.partners;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Partner;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Show all partners.
 */
class DoShowAllPartners extends Command<WarehouseManager> {

  DoShowAllPartners(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PARTNERS, receiver);
  }

  @Override
  public void execute() throws CommandException {
    List<Partner> partners = new ArrayList<>();
    for (Partner partner : _receiver.getWarehouse().getAllPartners())
      partners.add(partner);

    Collections.sort(partners, Partner.getComparatorPartner());

    for (Partner p : partners)
      _display.addLine(p.toString());
    _display.display();
  }

}
