package softwareEngineering.ManoniSgaravattiFerretti.emspServer;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwareEngineering.ManoniSgaravattiFerretti.emspServer.ChargingSessionManager.NotificationGenerator;

@Service
public class NotificationSender{
    @Autowired
    FirebaseMessaging firebaseMessaging;

    public String sendNotification(NotificationGenerator note) throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle(note.getTitle())
                .setBody(note.getBody())
                .build();

        Message message = Message
                .builder()
                .setToken(note.getTo().toString())
                .setNotification(notification)
                .putAllData(note.getData())
                .build();

        return firebaseMessaging.send(message);
    }
}
