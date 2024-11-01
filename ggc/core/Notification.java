package ggc.core;

import java.io.Serializable;
import java.lang.Math;

public class Notification implements Serializable{
    private Product _product;
    private Batch _batch;
    private NotificationType _notificationType;

    public Notification(Product product, Batch batch, NotificationType notificationType){
        _product = product;
        _batch = batch;
        _notificationType = notificationType;
    }
    public Product getProduct(){
        return _product;
    }
    public NotificationType getNotificationType(){
        return _notificationType;
    }
    public Batch getBatch(){
        return _batch;
    }
    @Override
    public String toString(){
        return _notificationType + "|" + _product.getId() + "|" + Math.round(_batch.getPrice());
    }
}