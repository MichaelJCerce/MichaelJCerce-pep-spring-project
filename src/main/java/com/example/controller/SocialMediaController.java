package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/")
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public  ResponseEntity<Account> createUser(@RequestBody Account account) {  
        if (accountService.getAccountByUsername(account.getUsername()) != null)
            return ResponseEntity.status(409).build();
        else if (account.getUsername() == "" || account.getPassword().length() < 4)
            return ResponseEntity.status(400).build();

        return ResponseEntity.ok(accountService.persistAccount(account));
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account user = accountService.verifyAccount(account.getUsername(), account.getPassword());

        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        if (accountService.getAccountById(message.getPostedBy()) == null || message.getMessageText().length() > 255 || message.getMessageText().length() == 0)
            return ResponseEntity.status(400).build();
        
        return ResponseEntity.ok(messageService.persistMessage(message));
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> getSpecificMessage(@PathVariable Integer messageId) {
        Message message = messageService.getMessageById(messageId);
        return message == null ? ResponseEntity.status(200).build() : ResponseEntity.ok(message);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteSpecificMessage(@PathVariable Integer messageId) {
        int deletedRow = messageService.deleteMessageById(messageId);
        return deletedRow == 0 ? ResponseEntity.status(200).build() : ResponseEntity.ok(deletedRow);
    }

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateSpecificMessage(@PathVariable Integer messageId, @RequestBody Message updatedMessage) {
        if (updatedMessage.getMessageText().length() == 0 || updatedMessage.getMessageText().length() > 255) return ResponseEntity.status(400).build();
        int updatedRow = messageService.updateMessageById(messageId, updatedMessage);
        return updatedRow == 0 ? ResponseEntity.status(400).build() : ResponseEntity.ok(updatedRow);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllAccountMessages(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.getAllMessagesByPostedBy(accountId));
    }
}
