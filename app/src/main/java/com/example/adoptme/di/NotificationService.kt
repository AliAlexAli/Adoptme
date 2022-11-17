package com.example.adoptme.di

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import com.example.adoptme.R
import com.example.adoptme.presentation.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class NotificationService : FirebaseMessagingService() {

  override fun onMessageReceived(message: RemoteMessage) {
    // super.onMessageReceived(message)
    val intent = Intent(this, MainActivity::class.java)
    val petId = message.data["petId"].toString();
    intent.putExtra("petId", petId)
    if(petId.isNullOrEmpty()) Log.d("ActivityStart1", petId)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT);

    val notification : Notification.Builder = Notification.Builder(this, getString(R.string.default_notification_channel_id))
      .setContentTitle(message.notification?.title ?: "Default title")
      .setContentText(message.notification?.body ?: "")
      .setSmallIcon(R.drawable.ic_launcher_foreground)
      .setAutoCancel(true)
      .setContentIntent(pendingIntent)
    NotificationManagerCompat.from(this).notify(1, notification.build());
  }
}
