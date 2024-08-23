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

    /**
     * Uses the MessageRepository to get a list of all existing messages
     * @return The list of messages that currently exist
     */
    public List<Message> getAllMessages()
    {
        return messageRepository.findAll();
    }

    /**
     * Uses the MessageRepository to save a message if the message text is valid and the account posting it exists
     * @param message The message to save
     * @return Optional<Message> of the saved message or empty if unsuccessful
     */
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

    /**
     * Uses the MessageRepository to get a message based on the message's ID
     * @param id The ID of the message to get
     * @return Optional<Message> of the message to get or empty if unsuccessful
     */
    public Optional<Message> getMessageByID(int id)
    {
        return messageRepository.findById(id);
    }

    /**
     * Uses the MessageRepository to delete a message based on the message's ID
     * @param id The ID of the message to delete
     * @return Boolean indicating if deletion was successful
     */
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

    /**
     * Uses the MessageRepository to get a list of all messages posted by an acount with a specified ID
     * @param postedBy The ID of the account that posted the messages to get
     * @return List of messages posted by the account with the specified ID
     */
    public List<Message> findMessagesByPostedBy(int postedBy)
    {
        return messageRepository.findByPostedBy(postedBy);
    }

    /**
     * Uses the MessageRepository to update a message based on the message's ID
     * @param message Message with updated message text
     * @param id The ID of the message to update
     * @return Boolean indicating whether the update was successful
     */
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

    /**
     * Validates message text based on these requirements:
     * * the text is not blank
     * * the text is less than 255 characters long
     * @param message_text The message text to validate
     * @return Boolean indicating whether the text is valid
     */
    public boolean isValidMessageText(String message_text)
    {
        return !message_text.isBlank() && message_text.length() < 255;
    }
}
