/**
 * 
 */
package com.websocket.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.websocket.dbmodel.UserMessage;

/**
 * @author Kaushik
 *
 */
@Repository
public interface MessageRepository extends MongoRepository<UserMessage, String> {

}
