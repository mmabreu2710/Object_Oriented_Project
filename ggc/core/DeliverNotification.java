package ggc.core;

import java.util.Collection;
import java.io.Serializable;

public class DeliverNotification implements DeliveryNotificationMode, Serializable{
    public void addNotification(Notification n, Collection<Notification> notifications){
        notifications.add(n);
    }
}