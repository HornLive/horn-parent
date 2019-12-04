/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.horn.mommons.dao.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class Producer implements Closeable {
    private final KafkaProducer<Object, String> producer;
    private String topic;
    private static final String KAFKA_SERVER_URL="dtone.kafka.server.url";
    private static final String KAFKA_CLIENT_ID="dtone.kafka.client.id";
    private static final String KAFKA_TOPIC="dtone.event.topic";

    public Producer(Configuration conf) {
        Properties props = new Properties();
        String serverUrl = conf.getString(KAFKA_SERVER_URL);
        String clientId = conf.getString(KAFKA_CLIENT_ID);
        props.put(BOOTSTRAP_SERVERS_CONFIG, serverUrl);
        props.put(CLIENT_ID_CONFIG, clientId);
        props.put(KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.ByteArraySerializer");
        props.put(VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        this.producer = new KafkaProducer<Object, String>(props);
        this.topic = conf.getString(KAFKA_TOPIC);
        
        System.out.println(serverUrl);
        System.out.println(clientId);
        System.out.println(topic);
    }
 

    public void send(String log) {
        // Send asynchronously
        long startTime = System.currentTimeMillis();
        producer.send(new ProducerRecord<Object, String>(topic, log),
                new NoOpCallBack(startTime, log));
    }


    public void close() {
        if (producer != null) {
            producer.close();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
    	@SuppressWarnings("resource")
		Producer p = new Producer(new Configuration());
    	ArrayList<String> list = new ArrayList<String>();
    	list.add("美食");
    	list.add("温泉");
    	
    	int i=10000;
    	while(true){
    		DataDoc data = new DataDoc(i++, "汤山温泉1日游", list);
        	p.send(JSON.toJSONString(data));
        	System.out.println(JSON.toJSONString(data));
        	Thread.sleep(3000);
    	}
    }
}


class NoOpCallBack implements Callback {
    private static final Logger logger = LoggerFactory.getLogger(
            NoOpCallBack.class);
    private final long startTime;
    private final String message;

    public NoOpCallBack(long startTime, String message) {
        this.startTime = startTime;
        this.message = message;
    }


    public void onCompletion(RecordMetadata metadata, Exception exception) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (metadata != null) {
            logger.debug(
                    "message(" + message + ") sent to partition(" + metadata.partition() +
                            "), " +
                            "offset(" + metadata.offset() + ") in " + elapsedTime + " ms");
        } else {
            logger.error("message sent error", exception);
        }
    }
}

