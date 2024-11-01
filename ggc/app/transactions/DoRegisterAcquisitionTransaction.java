package ggc.app.transactions;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.core.Component;
import ggc.core.Acquisition;
import ggc.core.Recipe;
import ggc.core.Product;
import ggc.core.SimpleProduct;
import ggc.core.AggregateProduct;
import ggc.core.Batch;
import pt.tecnico.uilib.forms.Form;
import java.util.Collection;
import java.util.ArrayList;
import ggc.core.Partner;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.exception.ProductAlreadyExistsException;
import ggc.core.exception.NoSuchProductException;
import ggc.app.exception.UnknownPartnerKeyException;
import ggc.app.exception.UnknownProductKeyException;


/**
 * Register order.
 */
public class DoRegisterAcquisitionTransaction extends Command<WarehouseManager> {

  public DoRegisterAcquisitionTransaction(WarehouseManager receiver) {
    super(Label.REGISTER_ACQUISITION_TRANSACTION, receiver);
    addStringField("idPartner", Message.requestPartnerKey());
    addStringField("idProduct", Message.requestProductKey());
    addRealField("price", Message.requestPrice());
    addIntegerField("amount", Message.requestAmount());
    
  }

  
  @Override
  public final void execute() throws CommandException {
    
    String idProduct = stringField("idProduct");
    String idPartner = stringField("idPartner"); 
    Double price = realField("price");
    int amount = integerField("amount");

    try{
    //if the product doesn't exist
      if (!_receiver.getWarehouse().hasProduct(idProduct)){
        Form form = new Form();
        form.addBooleanField("aggregate", Message.requestAddRecipe());
        form.parse();
        Boolean aggregate = form.booleanField("aggregate");
        //if the product has a recipe, therefore being an aggregate product
        if (aggregate){
          form = new Form();
          form.addIntegerField("numberOfComponents", Message.requestNumberOfComponents());
          form.addRealField("alpha", Message.requestAlpha());
          form.parse();
          int numberOfComponents = form.integerField("numberOfComponents");
          Double alpha = form.realField("alpha");
          //in this cycle we're asking for information about the individual components in the recipe
          int n = 0;
          Collection<Component> components = new ArrayList<>();
          for (n = 0; n < numberOfComponents; n++){
            Form form2 = new Form();
            form2.addStringField("idComponent", Message.requestProductKey());
            form2.addIntegerField("quantity", Message.requestAmount());
            form2.parse();
            String idComponent = form2.stringField("idComponent");
            int quantity = form2.integerField("quantity");
            Component component = new Component(_receiver.getWarehouse().getProduct(idComponent), quantity);
            components.add(component);
          }
          Recipe recipe = new Recipe(components, alpha);
          AggregateProduct product = new AggregateProduct(idProduct, recipe);
          _receiver.getWarehouse().addProduct(product); 
        }
        else{
          SimpleProduct product = new SimpleProduct(idProduct);
          _receiver.getWarehouse().addProduct(product);
        }

      }
      Product product = _receiver.getWarehouse().getProduct(idProduct);
      Partner partner = _receiver.getWarehouse().getPartner(idPartner);
      _receiver.getWarehouse().doAcquisition(product, amount, partner, price);

    }catch(ProductAlreadyExistsException ex){
      System.err.println(ex);
    }catch(NoSuchProductException ex){
      throw new UnknownProductKeyException(idProduct);
    }catch (NoSuchPartnerException ex){
      throw new UnknownPartnerKeyException(idPartner);
    }
  }

}
