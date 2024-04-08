package com.example.server.MQTT_config;

import org.springframework.context.annotation.Configuration;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.influxdb.client.InfluxDBClient;

import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;



@Configuration
public class MqttBean{
    private final String token = "r0jzvsZZKAvIAHLilbPHf9DWGoji4WpgUDE0MQKDLR4PHEdE8TlaCXZ-FtU4lpuSl0AqdHuOJmsoUYO5OIZ3hQ==";
    private final String org = "2d15c57353bf8d43";
    private final String bucket = "iot_data";
    private final String url = "https://us-east-1-1.aws.cloud2.influxdata.com/";
    InfluxDBConnection inConn = new InfluxDBConnection();
    private InfluxDBClient influxDBClient = inConn.buildConnection(url, token, bucket, org);

    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://anhtuan@broker.emqx.io:1883"});
        options.setUserName("user");
        options.setPassword("123".toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound(){
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(MqttAsyncClient.generateClientId(), mqttPahoClientFactory(), "sensor");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(){
        return new MessageHandler(){
            @Override
            public void handleMessage(Message<?> message) throws MessagingException{
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if(topic.equals("sensor")){
                    System.out.println("Received from sensor topic: " + message.getPayload().toString());
                    String msg = message.getPayload().toString();
                    //write data to InfluxDB
                    inConn.writePointbyPOJO(influxDBClient, msg);
                }
            }
        };
    }

    @Bean
    public MessageChannel mqttOutboundChannel(){
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound(){
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(MqttAsyncClient.generateClientId(), mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("sensor");
        return messageHandler;
    }
}