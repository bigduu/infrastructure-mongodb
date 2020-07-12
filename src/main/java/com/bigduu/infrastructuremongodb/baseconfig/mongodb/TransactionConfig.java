package com.bigduu.infrastructuremongodb.baseconfig.mongodb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;

/**
 * @author bigduu
 * @title: TransactionConfig
 * @projectName demomongo
 * @description: TODO
 * @date 2020/5/1406:32
 */
@Configuration
public class TransactionConfig {
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory factory){
        return new MongoTransactionManager(factory);
    }

}
