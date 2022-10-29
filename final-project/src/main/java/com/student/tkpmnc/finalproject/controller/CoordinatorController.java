package com.student.tkpmnc.finalproject.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoordinatorController {
// ---socket io server test---
//    @Autowired
//    private JourneyModule journeyModule;
//
//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMessage(@RequestBody DriverBroadcastMessage broadcastMessage) {
//        journeyModule.sendEvent(broadcastMessage);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//    ---end of socket io server test---

//    @Autowired
//    private SimpMessagingTemplate template;
//
//    @PostMapping("/getAssignedDriver")
//    public ResponseEntity<Void> sendMessage(@RequestBody Journey textMessageDTO) {
//        template.convertAndSend("/topic/message", textMessageDTO);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @MessageMapping("/sendMessage")
//    public void receiveMessage(@Payload Journey textMessageDTO) {
//        // receive message from client
//    }
//
//
//    @SendTo("/topic/message")
//    public Journey broadcastMessage(@Payload Journey textMessageDTO) {
//        System.out.println(textMessageDTO);
//        return textMessageDTO;
//    }
}
