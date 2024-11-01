package ggc.app.products;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Show all products.
 */
class DoShowAllProducts extends Command<WarehouseManager> {

  DoShowAllProducts(WarehouseManager receiver) {
    super(Label.SHOW_ALL_PRODUCTS, receiver);
  }

  @Override
  public final void execute() throws CommandException {
    List<Product> products = new ArrayList<>();
    for (Product p : _receiver.getWarehouse().getAllProducts())
      products.add(p);

    Collections.sort(products, Product.getComparatorProduct());

    for (Product prod : products)
      _display.addLine(prod.toString());
    _display.display();
    }
 }


