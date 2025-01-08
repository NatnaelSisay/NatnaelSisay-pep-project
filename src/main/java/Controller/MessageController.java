package Controller;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Message;
import Service.MessageService;
import Util.GeneralUtil;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService service){
        this.messageService = service;
    }

    public void save(Context context) throws JsonProcessingException{
        Message message = GeneralUtil.extractMessageFromBody(context.body());
        if(message == null){
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }
        
        Optional<Message> savedMessage = this.messageService.save(message);
        if(!savedMessage.isPresent()) {
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        context.status(200).result(GeneralUtil.convertToJson(savedMessage.get()));
    }
}
