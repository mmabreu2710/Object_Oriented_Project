package ggc.core;

import java.util.Collection;
import java.io.Serializable;

public interface DeliveryNotificationMode{
    void addNotification(Notification n, Collection<Notification> notifications);
}