package ggc.app.main;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import ggc.core.WarehouseManager;
import ggc.app.main.Message;
import pt.tecnico.uilib.forms.Form;
import java.io.IOException;
import ggc.core.exception.MissingFileAssociationException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
class DoSaveFile extends Command<WarehouseManager> {

  /** @param receiver */
  DoSaveFile(WarehouseManager receiver) {
    super(Label.SAVE, receiver);
  }

  @Override
  public final void execute() throws CommandException{
    try{
      if (_receiver.hasFilename())
        _receiver.save();
      else{
        Form form = new Form();
        form.addStringField("filename", Message.newSaveAs());
        form.parse();
        String filename = form.stringField("filename");
        _receiver.saveAs(filename);
        }
      } catch(IOException exe){
        System.err.println(exe);
      } catch(MissingFileAssociationException ex){
        System.err.println(ex);
      }
  }
}

