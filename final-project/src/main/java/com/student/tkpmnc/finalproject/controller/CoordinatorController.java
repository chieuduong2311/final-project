package com.student.tkpmnc.finalproject.controller;

import com.student.tkpmnc.finalproject.api.model.Journey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinatorController {

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/getAssignedDriver")
    public ResponseEntity<Void> sendMessage(@RequestBody Journey textMessageDTO) {
        template.convertAndSend("/topic/message", textMessageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload Journey textMessageDTO) {
        // receive message from client
    }


    @SendTo("/topic/message")
    public Journey broadcastMessage(@Payload Journey textMessageDTO) {
        System.out.println(textMessageDTO);
        return textMessageDTO;
    }
}
