package com.example.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private AccountService accountService;

    private MessageService messageService;
    
    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }
    
    // create a new user
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        try{
            Account registeredAccount = accountService.newAccount(account);
            return ResponseEntity.ok(registeredAccount);
        }
        catch(IllegalStateException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }

    }
    
    //login a user
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        try{
            Account loggedAccount = accountService.loginAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(loggedAccount);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //create a new message
    @PostMapping("/messages")
    public ResponseEntity<Message> newMessage(@RequestBody Message message){
        try{
            Message newMessage = messageService.newMessage(message);
            return ResponseEntity.ok(newMessage);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    //get all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
       List<Message> messages = messageService.everyMessage();
       return ResponseEntity.ok(messages);
    }

    //get a message by its id
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> messageById(@PathVariable("message_id") int messageId){
        try{
            Message message = messageService.getMessagebyId(messageId);
            return ResponseEntity.ok(message);
        }
        catch(EntityNotFoundException e){
            return ResponseEntity.ok(null);
        }
        
    }

    //delete a message by its id
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable("message_id") int messageId){
        try{
            int deletedMessage = messageService.deleteMessageById(messageId);
            return ResponseEntity.ok(deletedMessage);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.ok(null);
        }
        
    }

    //update a message by its id
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable("message_id") int messageId, @RequestBody Message message){
        try{
            int updatedMessage = messageService.updateMessageById(messageId, message.getMessageText());
            return ResponseEntity.ok(updatedMessage);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
        catch(EntityNotFoundException e){
            return ResponseEntity.badRequest().build();
        }
    }

    // all of a user's messages
    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccount(@PathVariable("account_id") int accountId){
        List<Message> messages = messageService.getAllMessagesByAccount(accountId);
    
        return ResponseEntity.ok(messages);
    }
}
