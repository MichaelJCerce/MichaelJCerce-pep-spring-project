package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message persistMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
    public List<Message> getAllMessagesByPostedBy(Integer postedBy) {
        Optional<List<Message>> messages = messageRepository.findByPostedBy(postedBy);
        return messages.get();
    }

    public Message getMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent())
            return message.get();
        return null;
    }

    public Integer deleteMessageById(Integer id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isPresent()) {
            messageRepository.deleteById(id);
            return 1;
        }   
        return 0;
    }

    public Integer updateMessageById(Integer id, Message updatedMessage) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(updatedMessage.getMessageText());
            messageRepository.save(message);
            return 1;
        }   
        return 0;
    }

}
