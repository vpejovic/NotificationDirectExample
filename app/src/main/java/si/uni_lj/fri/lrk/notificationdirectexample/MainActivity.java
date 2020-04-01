package si.uni_lj.fri.lrk.notificationdirectexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.IconCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String channelID = "si.uni_lj.fri.lrk.notificationdirectexample";

    private static final int notificationId = 101;

    private static final String KEY_TEXT_REPLY = "key_text_reply";

    NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = NotificationManagerCompat.from(this);

        createNotificationChannel(channelID, "DirectReply News", "Example news channel");

        handleIntent();
    }

    private void handleIntent(){
        Intent intent = this.getIntent();
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String inputText = remoteInput.getString(KEY_TEXT_REPLY);

            TextView myTextView = findViewById(R.id.textView);
            myTextView.setText(inputText);
        }
    }

    private void createNotificationChannel(String id, String name, String description) {

        if (Build.VERSION.SDK_INT < 26) {
            return;
        } else {

            NotificationChannel channel = new NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            notificationManager.createNotificationChannel(channel);
        }

    }

    public void sendNotification(View view) {

        String replyLabel = "Enter your reply here";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                android.R.drawable.ic_dialog_info,
                "Reply",
                resultPendingIntent)
                .addRemoteInput(remoteInput).build();


        Notification newMessageNotification = new NotificationCompat.Builder(this, channelID)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("My Notification")
                .setContentText("This is a test message")
                .addAction(replyAction).build();

        notificationManager.notify(notificationId, newMessageNotification);
    }

}
