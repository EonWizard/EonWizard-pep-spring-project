package com.example.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    // create new message
    public Message newMessage(Message message){
        if(!accountRepository.existsById(message.getPostedBy()) || message.getMessageText().isBlank() || message.getMessageText().length() > 255){
            throw new IllegalArgumentException("Message Not Created: Message Must Not Already Exist, Be Empty, Or Be Over 255 Characters");
        }
        return messageRepository.save(message);
    }
    // get all messages
    public List<Message> everyMessage(){
        return messageRepository.findAll();
    }
    
    // get message by id
    public Message getMessagebyId(int id) {
        return messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message Not Found"));
    }

    // delete message by id
    public int deleteMessageById(int messageId){
        if(messageRepository.existsById(messageId)){
             messageRepository.deleteById(messageId);
            return 1;
        }
        else{
            throw new IllegalArgumentException("Message does not exist");
        }
    }

    // update message by id
    public int updateMessageById(int id, String messageText){
        if(messageText == null || messageText.isBlank() || messageText.length() > 255){
            throw new IllegalArgumentException("Message Not Updated: Message Must Not Already Exist, Be Empty, Or Be Over 255 Characters");
        }
        Message exists = messageRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Message Not Found"));

        exists.setMessageText(messageText);
        messageRepository.save(exists);

        return 1;
    }

    // get user messages based on account id
    public List<Message> getAllMessagesByAccount(int accountId){
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if(accountOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account not found");
        }
        return messageRepository.findByAccountId(accountId);
    }
}
