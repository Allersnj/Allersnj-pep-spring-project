package com.example.controller;

import java.util.List;
import java.util.Optional;

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

@RestController
public class SocialMediaController
{
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService)
    {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    /**
     * Handler for retrieving all messages
     * @return The list of all existing messages
     */
    @GetMapping("/messages")
    public List<Message> getAllMessages()
    {
        return messageService.getAllMessages();
    }

    /**
     * Handler for posting a new message
     * @param message The new message to add
     * @return ResponseEntity indicating whether the post was successful
     */
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message)
    {
        Optional<Message> postedMessage = messageService.saveMessage(message);

        if (postedMessage.isPresent())
        {
            return ResponseEntity.ok(postedMessage.get());
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handler for registering a new account
     * @param account The new account to register
     * @return ResponseEntity indicating whether registration was successful
     */
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account)
    {
        if (accountService.accountExistsByUsername(account.getUsername()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Optional<Account> registeredAccount = accountService.saveAccount(account);

        if (registeredAccount.isPresent())
        {
            return ResponseEntity.ok(registeredAccount.get());
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Handler for getting a message by its ID
     * @param message_id The ID of the message to search for
     * @return ResponseEntity with either the found message or null in its body 
     */
    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageByID(@PathVariable Integer message_id)
    {
        Optional<Message> message = messageService.getMessageByID(message_id);
        return ResponseEntity.ok(message.orElse(null));
    }

    /**
     * Handler for deleting a message by its ID
     * @param message_id The ID of the message to delete
     * @return ResponseEntity with either the number of rows updated (always 1) or null in its body
     */
    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Integer> deleteMessageByID(@PathVariable Integer message_id)
    {
        return ResponseEntity.ok(messageService.deleteMessageByID(message_id) ? 1 : null);
    }

    /**
     * Handler for logging in an existing user
     * @param account The account to log in
     * @return ResponseEntity indicating whether logging in was successful
     */
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account)
    {
        Optional<Account> authenticatedAccount = accountService.getAuthenticatedAccount(account);
        if (authenticatedAccount.isPresent())
        {
            return ResponseEntity.ok(authenticatedAccount.get());
        }
        else
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Handler for getting all messages posted by an account based on the account's ID
     * @param account_id The ID of the account that posted the messages to find
     * @return List of messages the account posted
     */
    @GetMapping("/accounts/{account_id}/messages")
    public List<Message> getMessagesByAccount(@PathVariable Integer account_id)
    {
        return messageService.findMessagesByPostedBy(account_id);
    }

    /**
     * Handler for patching a message based on the message's ID
     * @param message_id The ID of the message to patch
     * @param message The message object only containing the updated message text to use
     * @return ResponseEntity indicating whether the patch was successful
     */
    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> patchMessageByID(@PathVariable Integer message_id, @RequestBody Message message)
    {
        if (messageService.updateMessage(message, message_id))
        {
            return ResponseEntity.ok(1);
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }
}
