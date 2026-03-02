package com.nefarious.socialnetwork.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


public class ProducerWithCallback {
    public static final Logger log = LoggerFactory.getLogger(ProducerWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        log.info("I am a Kafka Producer");

        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        // Create a producer
        KafkaProducer<String, String > producer = new KafkaProducer<>(properties);

        for (int j = 0; j < 2; j++){
            // For teh same key, messages go to the same partition
            for (int i = 0; i < 10; i++){
                String topic = "first_topic";
                String key = "id_" + i;
                String value = "Hello World " + i;

                // Create a record
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);

                // Send Data
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        // Executed when record is sucessfully sent / or exception is thrown
                        if (e == null) {
                            log.info("Record is sent");
                            log.info(recordMetadata.topic(), recordMetadata.offset(), recordMetadata.partition(), recordMetadata.timestamp());
                        } else {
                            log.error("Error while producing", e);
                        }

                    }
                });
            }
        }


        // Flush
        producer.flush();
        producer.close();
    }
}
