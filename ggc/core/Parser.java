package ggc.core;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import ggc.core.Component;
import ggc.core.exception.NoSuchProductException;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.exception.PartnerAlreadyExistsException;
import ggc.core.exception.ProductAlreadyExistsException;
import java.util.Set;
import java.util.Collection;

import ggc.core.exception.BadEntryException;

public class Parser {

  private Warehouse _store;

  public Parser(Warehouse w) {
    _store = w;
  }

  void parseFile(String filename) throws IOException, BadEntryException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = reader.readLine()) != null)
        parseLine(line);
    }
  }

  private void parseLine(String line) throws BadEntryException, BadEntryException {
    String[] components = line.split("\\|");

    switch (components[0]) {
      case "PARTNER":
        parsePartner(components, line);
        break;
      case "BATCH_S":
        parseSimpleProduct(components, line);
        break;

      case "BATCH_M":
        parseAggregateProduct(components, line);
        break;
        
      default:
        throw new BadEntryException("Invalid type element: " + components[0]);
    }
  }

  //PARTNER|id|nome|endereço
  private void parsePartner(String[] components, String line) throws BadEntryException {
    if (components.length != 4)
      throw new BadEntryException("Invalid partner with wrong number of fields (4): " + line);
    
    String id = components[1];
    String name = components[2];
    String address = components[3];
    try {
    Partner partner = new Partner(name, address, id);
    _store.addPartner(partner);
    } catch(PartnerAlreadyExistsException exe){
      System.err.println(exe);
    }

  }

  //BATCH_S|idProduto|idParceiro|prec ̧o|stock-actual
  private void parseSimpleProduct(String[] components, String line) throws BadEntryException {
    if (components.length != 5)
      throw new BadEntryException("Invalid number of fields (4) in simple batch description: " + line);
    
    String idProduct = components[1];
    String idPartner = components[2];
    double price = Double.parseDouble(components[3]);
    int stock = Integer.parseInt(components[4]);
    try{
      if (!_store.hasProduct(idProduct)){
        SimpleProduct simpleProduct = new SimpleProduct(idProduct);
        _store.addProduct(simpleProduct);
      }

      Product product = _store.getProduct(idProduct);
      Partner partner = _store.getPartner(idPartner);
      Batch batch = new Batch(product, partner, price, stock);//a batch sabe a que produto pertence? eu diria que sim
      
      product.addBatch(batch);
      partner.addBatch(batch);
      partner.removeAllNotifications();
    }catch(NoSuchPartnerException exe){
        System.err.println(exe);
    }catch(NoSuchProductException exe){
      System.err.println(exe);
    }catch(ProductAlreadyExistsException exe){
      System.err.println(exe);
    }

  }
 
    
  //BATCH_M|idProduto|idParceiro|prec ̧o|stock-actual|agravamento|componente-1:quantidade-1#...#componente-n:quantidade-n
  private void parseAggregateProduct(String[] components, String line) throws BadEntryException {
    if (components.length != 7)
      throw new BadEntryException("Invalid number of fields (7) in aggregate batch description: " + line);
    
    String idProduct = components[1];
    String idPartner = components[2];
    try{
      if (!_store.hasProduct(idProduct)) {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();
        
        for (String component : components[6].split("#")) {
          String[] recipeComponent = component.split(":");

          products.add(_store.getProduct(recipeComponent[0]));

          quantities.add(Integer.parseInt(recipeComponent[1]));
        }

        Collection<Component> componentsSet = new ArrayList<>();
        for (int i = 0; i < products.size(); i++){ //products and quantities have the same size
          Component component = new Component(products.get(i), quantities.get(i));
          componentsSet.add(component);
        }
        Recipe recipe = new Recipe(componentsSet, Double.parseDouble(components[5]));
        AggregateProduct aggregateproduct = new AggregateProduct(idProduct, recipe);
        _store.addProduct(aggregateproduct);
      }
      
      Product product = _store.getProduct(components[1]);
      Partner partner = _store.getPartner(components[2]);
      double price = Double.parseDouble(components[3]);
      int stock = Integer.parseInt(components[4]);

      Batch batch = new Batch(product, partner, price, stock);
      product.addBatch(batch);
      partner.addBatch(batch);
      partner.removeAllNotifications();
    } catch(NoSuchPartnerException exe){
        System.err.println(exe);
    }catch(NoSuchProductException exe){
      System.err.println(exe);
    }catch(ProductAlreadyExistsException exe){
      System.err.println(exe);
    }
  }
}