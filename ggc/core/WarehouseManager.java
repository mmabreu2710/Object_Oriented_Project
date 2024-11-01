package ggc.core;

import java.io.Serializable;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;


import ggc.core.exception.BadEntryException;
import ggc.core.exception.ImportFileException;
import ggc.core.exception.UnavailableFileException;
import ggc.core.exception.MissingFileAssociationException;
import ggc.core.exception.NoSuchProductException;
import ggc.core.exception.NoSuchPartnerException;
import ggc.core.exception.PartnerAlreadyExistsException;
import ggc.core.exception.ProductAlreadyExistsException;
import ggc.app.exception.FileOpenFailedException;


/** Façade for access. */
public class WarehouseManager implements Serializable {

  /** Name of file storing current warehouse. */
  private String _filename = "";

  /** The warehouse itself. */
  private Warehouse _warehouse = new Warehouse();

  public Warehouse getWarehouse(){
    return _warehouse;
  }

  public boolean hasFilename(){
    return (!_filename.isEmpty());
  }

  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */


  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {

      if (!this.hasFilename())
        throw new MissingFileAssociationException();
      try (ObjectOutputStream warehouseOut = 
        new ObjectOutputStream (new FileOutputStream(_filename))){
        warehouseOut.writeObject(_warehouse);
      } 
    }
  

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @param filename
   * @throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException, IOException {

    
    ObjectInputStream wareIn = null;


    try{
      wareIn = new ObjectInputStream(new FileInputStream(filename));
      Object warehouse = wareIn.readObject();
      _warehouse = (Warehouse)warehouse; 
      _filename = filename;
      
    } finally{
      if (wareIn != null)
        wareIn.close();
    }
  }
  

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _warehouse.importFile(textfile);
    } catch (IOException | BadEntryException | NoSuchProductException | NoSuchPartnerException |
      PartnerAlreadyExistsException | ProductAlreadyExistsException/* FIXME maybe other exceptions */ e) {
      throw new ImportFileException(textfile, e);
    }
  }

}
