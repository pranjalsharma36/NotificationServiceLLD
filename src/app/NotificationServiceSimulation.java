package app;


import java.io.*;
import java.util.*;

public class NotificationServiceSimulation  {
	public static void main (String[] args) {
		System.out.println("Executing Simulation!");
		
		User user = new User("Pranjal");
		Message message = new Message("Testing");
		
		NotificationService service = new NotificationService();
		
		// subscribe
		service.subscibe(user, NotificationType.NEW_MESSAGE);
		service.subscibe(user, NotificationType.FRIEND_REQUEST);
		
		
		// send notification
		service.sendNotification(user, message, NotificationChannelType.SMS, 
                                            NotificationType.NEW_MESSAGE);
		
	}
}


class Message{
    private String message;
    
    Message(String message){
        this.message = message;
    }
    
    public String getMessage(){
        return this.message;
    }
}

enum NotificationType{
    
    NEW_MESSAGE,
    FRIEND_REQUEST,
    SYSTEM_ALERTS
}

class NotificationTypeManager{
    
    private Map<String, HashSet<NotificationType>> userNotificationcatalog;
    
    // constructor
    
    NotificationTypeManager(){
        userNotificationcatalog = new HashMap<>();
    }
    
    public void subscribe(User user, NotificationType notificationType){
        
        String id = user.getId();
        
        if(!userNotificationcatalog.containsKey(id)){
            userNotificationcatalog.put(id, new HashSet<>());
        }
        
        userNotificationcatalog.get(id).add(notificationType);
    }
    
    public boolean check(User user, NotificationType notificationType){
        
        String id = user.getId();
        
        if(!userNotificationcatalog.containsKey(id) || !userNotificationcatalog.get(id).contains(notificationType)){
            return false;
        }
        
        return true;
    }
    
    
}

class User{
    private String id;
    
    // constructor
    User(String id){
        this.id = id;
    }
    
    public String getId(){
        return this.id;
    }
}

class NotificationChannelFactory{
    
    public NotificationChannel createInstance(NotificationChannelType type){
        
        if(type == NotificationChannelType.SMS){
            return new SMSNotificationChannel();
        }
        else if(type == NotificationChannelType.EMAIL){
            return new EmailNotificationChannel();
        }
        
        return null;
    }
}
// This can implement observeeInterface since it has => subscribe, register and notify methods. It is behaving like an observee
class NotificationService {
    
    private NotificationTypeManager notificationTypeManager;
    private NotificationChannelFactory notificationChannelFactory;
    
    // constructor
    NotificationService(){
        notificationTypeManager = new NotificationTypeManager();
        notificationChannelFactory = new NotificationChannelFactory();
    }
    
    // Note : we can easily handle custom notification type as well
    public void subscibe(User user, NotificationType  notificationType){
        notificationTypeManager.subscribe(user, notificationType);
    }
    
    public void sendNotification(User user, Message message, NotificationChannelType channelType, 
                                            NotificationType notificationType){
        
        boolean allow = notificationTypeManager.check(user, notificationType);
        
        if(allow){
            // lets create a factory
            NotificationChannel channel = notificationChannelFactory.createInstance(channelType);
            channel.sendNotification(user, message);
        }
    }
    
    // new message notification
    // we will check if our user has subsribed to new message notification
    // HashMap
}

interface NotificationChannel{
    // observer => updateUser => it can also behave like an observee 
    public void sendNotification(User user, Message message);
}

class SMSNotificationChannel implements NotificationChannel {
    public void sendNotification(User user, Message message) {
        
        System.out.println("Notification send by SMS to " +user.getId() + " " + message.getMessage() );
    }
}

class EmailNotificationChannel implements NotificationChannel {
    
    public void sendNotification(User user, Message message){
        
        System.out.println("Notification send by Email to " +user.getId() + " " + message.getMessage() ); 
        
    }
}

enum NotificationChannelType{
    EMAIL,
    SMS
}


/*
Objects

User

NotificationChannel => concrete channels

Notification Service => subscribe, manage notification preferences

Notification type - new messages, friend requests, system alerts, or custom events.

Content


*/
