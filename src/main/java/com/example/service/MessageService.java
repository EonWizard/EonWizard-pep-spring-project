package com.example.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    // create new message
    public Message newMessage(Message message){
        if(message.getMessageText() == null || message.getMessageText().isBlank() || message.getMessageText().length() > 255){
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
    public void deleteMessageById(Message message){
        if(message != null){
            messageRepository.deleteById(message.getMessageId());;
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
    public List<Message> getAllMessagesByAccount(int account_id){
        return messageRepository.findByAccountId(account_id);
    }
}
