package com.websocket.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.websocket.dbmodel.User;
import com.websocket.dbmodel.UserMessage;
import com.websocket.repository.MessageRepository;
import com.websocket.repository.UserRepository;

@Controller
@RestController
@RequestMapping("/api")
public class MessageController {
	
	@Autowired
	MessageRepository messageRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	@MessageMapping("/message/send")
    @SendTo("/channel/message")
    public UserMessage sendMessage(@Payload UserMessage userMessage) {
		return addMessage(userMessage);
    }

	private UserMessage addMessage(UserMessage userMessage) {
		List<User> users = userRepository.findByUserName(userMessage.getMessageTo());
		userMessage.setMessageDate(new java.util.Date());
		for (User user : users) {
			if(user.isOnline() == true) {
				userMessage.setMessageReadStatus("READ");
			}else {
				userMessage.setMessageReadStatus("UNREAD");
			}
		}
		return messageRepository.save(userMessage);
	}

    @MessageMapping("/message/add")
    @SendTo("/channel/user")
    public User addUser(@Payload User user, SimpMessageHeaderAccessor headerAccessor) {
    	System.out.println(user.getUserName());
        headerAccessor.getSessionAttributes().put("username", user.getUserName());
        return user;
    }

	@MessageMapping("/info")
    public Map<String, Object> info(@PathParam("t") String t) {
        Map<String, Object> map = new HashMap<String, Object>();
        System.out.println(t);
        map.put("status", 200);
        map.put("message", "Record fetch successfully");
        return map;
    }

	@GetMapping("/message/getlastactiveuser/{from}")
    public Map<String, Object> getLastActiveUser(@PathVariable("from") String from) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserMessage message = null;
        List<UserMessage> allMessage = messageRepository.findByMessageFrom(from, new Sort(Sort.Direction.ASC, "messageDate"));
        int count = allMessage.size();
        if(count != 0){
        	message = allMessage.get(count-1);
        }
        List<User> users = userRepository.findByUserName(message.getMessageTo());
        User user = null;
        if(users.size() != 0){
        	user = users.get(0);
        }
        List<UserMessage> allLastUserMessage = messageRepository.getAllMessage(message.getMessageTo(), from, new Sort(Sort.Direction.ASC, "messageDate"));
        
        map.put("status", 200);
        map.put("lastactiveuser",user);
        map.put("content", allLastUserMessage);
        map.put("message", "Record fetch successfully");	
        return map;
    }
    
    @GetMapping("/message/get/{to}/{from}")
    public Map<String, Object> getMessageUser(@PathVariable("to") String to, @PathVariable("from") String from) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<UserMessage> allMessage = messageRepository.getAllMessage(to, from, new Sort(Sort.Direction.ASC, "messageDate"));
        map.put("status", 200);
        map.put("content", allMessage);
        map.put("message", "Record fetch successfully");	
        return map;
    }
    
    @GetMapping("/user/all")
    public Map<String, Object> getAllUser() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> allusers = userRepository.findAll();
        map.put("status", 200);
        map.put("content", allusers);
        map.put("message", "Record fetch successfully");
        return map;
    }
    
    @GetMapping("/user/messageuser")
    public Map<String, Object> getAllMessageUser() {
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> allusers = userRepository.findAll();
        map.put("status", 200);
        map.put("content", allusers);
        map.put("message", "Record fetch successfully");
        return map;
    }
    
    
    
    @PostMapping("/chat/create")
    public Map<String, Object> createMessage(@RequestBody UserMessage userMessage) {
    	Map<String, Object> map = new HashMap<String, Object>();
        UserMessage record = addMessage(userMessage);
        map.put("status", 200);
        map.put("message", "Message send successfully");
        map.put("record", record);
        return map;
    }
    
    @PostMapping("/user/signup")
    public Map<String, Object> userSignup(@RequestBody User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        User record = userRepository.save(user);
        map.put("status", 200);
        map.put("message", "User created successfully");
        map.put("record", record);
        return map;
    }
    
    @PostMapping("/user/login")
    public Map<String, Object> userLogin(@RequestBody User user) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> users = userRepository.findByUserName(user.getUserName());
        if(users.size() == 0) {
        	map.put("status", 500);
            map.put("message", "Username is invalid");
        }else {
        	for (User row : users) {
				if(row.getUserPassword().equalsIgnoreCase(user.getUserPassword())) {
					row.setOnline(true);
		            User savedUser = userRepository.save(row);
		            map.put("status", 200);
		            map.put("message", "Login Successfully");
		            map.put("record", savedUser);
				}else {
					map.put("status", 500);
		            map.put("message", "Password is invalid");
				}
			}
        }
        return map;
    }
    
    @GetMapping("/user/logout/{username}")
    public Map<String, Object> userLogout(@PathVariable("username") String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<User> users = userRepository.findByUserName(username);
    	for (User row : users) {
			row.setOnline(false);
            userRepository.save(row);
            map.put("status", 200);
            map.put("message", "User Logout successfully");
		}
        return map;
    }
}
