package com.example.comunidadconectada;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.nio.charset.StandardCharsets;
import javax.net.ssl.SSLSocketFactory;

public class MqttManager {
    private static final String TAG = "MqttManager";
    private static MqttManager instance;

    // Broker privado EMQX Serverless con SSL/TLS
    private static final String DEFAULT_BROKER_URL = "ssl://a7bfe043.ala.us-east-1.emqxsl.com:8883";
    private static final String DEFAULT_USERNAME = "kevin";
    private static final String DEFAULT_PASSWORD = "kevin";
    private static final String TOPIC_ALARMA = "comunidad/alarma/activar";

    private MqttClient mqttClient;

    public interface MqttCallbackListener {
        void onMessageReceived(String topic, String message);
        void onConnectionLost(Throwable cause);
    }

    private MqttCallbackListener listener;

    private MqttManager() {
    }

    public static synchronized MqttManager getInstance() {
        if (instance == null) {
            instance = new MqttManager();
        }
        return instance;
    }

    public void setListener(MqttCallbackListener listener) {
        this.listener = listener;
    }

    public void connect(Context context, String brokerUrl, String clientId, String username, String password) {
        new Thread(() -> {
            try {
                if (mqttClient != null && mqttClient.isConnected()) {
                    return;
                }

                String url = (brokerUrl != null && !brokerUrl.isEmpty()) ? brokerUrl : DEFAULT_BROKER_URL;
                String cId = (clientId != null && !clientId.isEmpty()) ? clientId : MqttClient.generateClientId();
                String user = (username != null && !username.isEmpty()) ? username : DEFAULT_USERNAME;
                String pass = (password != null && !password.isEmpty()) ? password : DEFAULT_PASSWORD;

                mqttClient = new MqttClient(url, cId, new MemoryPersistence());

                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setConnectionTimeout(15);
                options.setKeepAliveInterval(30);
                options.setAutomaticReconnect(true);

                if (user != null && pass != null) {
                    options.setUserName(user);
                    options.setPassword(pass.toCharArray());
                }

                if (url.startsWith("ssl://")) {
                    options.setSocketFactory(SSLSocketFactory.getDefault());
                }

                mqttClient.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        Log.w(TAG, "Conexión MQTT perdida", cause);
                        if (listener != null) {
                            listener.onConnectionLost(cause);
                        }
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
                        Log.d(TAG, "Mensaje MQTT recibido [" + topic + "]: " + payload);
                        if (listener != null) {
                            listener.onMessageReceived(topic, payload);
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.d(TAG, "Entrega MQTT completada");
                    }
                });

                mqttClient.connect(options);
                Log.i(TAG, "Conectado exitosamente al broker MQTT SSL: " + url);

                // Suscribirse a los tópicos de la comunidad
                mqttClient.subscribe("comunidad/#", 1);

            } catch (Exception e) {
                Log.e(TAG, "Error conectando a MQTT: " + e.getMessage(), e);
            }
        }).start();
    }

    public void publishAlarma(String residente, String vivienda, OnPublishCompleteListener callback) {
        String mensaje = "{\"alerta\":\"PANICO\", \"residente\":\"" + residente + "\", \"vivienda\":\"" + vivienda + "\", \"timestamp\":" + System.currentTimeMillis() + "}";
        publish(TOPIC_ALARMA, mensaje, 1, callback);
    }

    public void publish(String topic, String payload, int qos, OnPublishCompleteListener callback) {
        new Thread(() -> {
            try {
                if (mqttClient == null || !mqttClient.isConnected()) {
                    connect(null, null, null, null, null);
                }

                if (mqttClient != null && mqttClient.isConnected()) {
                    MqttMessage message = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
                    message.setQos(qos);
                    mqttClient.publish(topic, message);
                    Log.i(TAG, "Publicado en [" + topic + "]: " + payload);
                    if (callback != null) {
                        callback.onSuccess();
                    }
                } else if (callback != null) {
                    callback.onError(new Exception("Cliente MQTT no conectado"));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al publicar mensaje MQTT: " + e.getMessage(), e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        }).start();
    }

    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            new Thread(() -> {
                try {
                    mqttClient.disconnect();
                } catch (MqttException e) {
                    Log.e(TAG, "Error al desconectar MQTT", e);
                }
            }).start();
        }
    }

    public interface OnPublishCompleteListener {
        void onSuccess();
        void onError(Exception e);
    }
}
