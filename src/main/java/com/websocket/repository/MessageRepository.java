/**
 * 
 */
package com.websocket.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.websocket.dbmodel.UserMessage;

/**
 * @author Kaushik
 *
 */
@Repository
public interface MessageRepository extends MongoRepository<UserMessage, String> {
	
	@Query("{'$or':[ {'$and':[ {'messageTo':?0}, {'messageFrom':?1} ]}, {'$and':[ {'messageFrom':?0}, {'messageTo':?1} ]} ]}")
	List<UserMessage> getAllMessage(String to, String from, Sort sort);
	
	List<UserMessage> findByMessageFrom(String from, Sort sort);
	
}
