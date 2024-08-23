package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService
{
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository)
    {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    public Optional<Message> saveMessage(Message message)
    {
        Optional<Message> result = Optional.empty();
        String message_text = message.getMessageText();
        if (isValidMessageText(message_text) && accountRepository.existsById(message.getPostedBy()))
        {
            result = Optional.of(messageRepository.save(message));
        }

        return result;
    }

    public Optional<Message> getMessageByID(int id)
    {
        return messageRepository.findById(id);
    }

    public boolean deleteMessageByID(int id)
    {
        boolean deleted = false;
        if (messageRepository.existsById(id))
        {
            messageRepository.deleteById(id);
            deleted = true;
        }
        return deleted;
    }

    public List<Message> findMessagesByPostedBy(int postedBy)
    {
        return messageRepository.findByPostedBy(postedBy);
    }

    public boolean updateMessage(Message message, int id)
    {
        boolean updated = false;
        String message_text = message.getMessageText();
        if (isValidMessageText(message_text) && messageRepository.existsById(id))
        {
            Message updatedMessage = messageRepository.getById(id);
            updatedMessage.setMessageText(message_text);
            messageRepository.save(updatedMessage);
            updated = true;
        }

        return updated;
    }

    public boolean isValidMessageText(String message_text)
    {
        return !message_text.isBlank() && message_text.length() < 255;
    }
}
