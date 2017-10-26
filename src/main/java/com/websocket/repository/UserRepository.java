package com.websocket.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.websocket.dbmodel.User;

/**
 * @author Kaushik
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	List<User> findByUserName(String username);
}
