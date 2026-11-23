package com.example.comunidadconectada;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ComunidadFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM_Service";
    public static final String CHANNEL_ID_ANUNCIOS = "canal_anuncios";
    public static final String CHANNEL_NAME_ANUNCIOS = "Anuncios de la Comunidad";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo FCM Token recibido: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "Mensaje FCM recibido de: " + remoteMessage.getFrom());

        String titulo = "Comunidad Conectada";
        String cuerpo = "";

        if (remoteMessage.getNotification() != null) {
            titulo = remoteMessage.getNotification().getTitle() != null ? remoteMessage.getNotification().getTitle() : titulo;
            cuerpo = remoteMessage.getNotification().getBody() != null ? remoteMessage.getNotification().getBody() : "";
        } else if (!remoteMessage.getData().isEmpty()) {
            titulo = remoteMessage.getData().containsKey("titulo") ? remoteMessage.getData().get("titulo") : titulo;
            cuerpo = remoteMessage.getData().containsKey("mensaje") ? remoteMessage.getData().get("mensaje") : "";
        }

        mostrarNotificacion(titulo, cuerpo);
    }

    private void mostrarNotificacion(String titulo, String cuerpo) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID_ANUNCIOS,
                    CHANNEL_NAME_ANUNCIOS,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notificaciones de avisos y alertas vecinales");
            channel.enableVibration(true);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Intent intent = new Intent(this, AnunciosActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_ANUNCIOS)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(titulo)
                .setContentText(cuerpo)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        if (notificationManager != null) {
            try {
                notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            } catch (SecurityException e) {
                Log.w(TAG, "No se tiene permiso para mostrar notificación: " + e.getMessage());
            }
        }
    }
}
