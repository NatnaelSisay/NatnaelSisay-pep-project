package Controller;

import java.util.List;
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

    /**
     * Delete message from database
     * @param ctx Javlin context with path parameter of "id"
     */
    public void delete(Context ctx) throws JsonProcessingException{
        String message_id = ctx.pathParam("id");
        Optional<Message> deletedMessage = this.messageService.delete(Integer.valueOf(message_id));
        
        if(deletedMessage.isEmpty()){
            ctx.status(200);
            return;
        }

        ctx.status(200).result(GeneralUtil.convertToJson(deletedMessage.get()));
    }

    public void findAccountMessages(Context ctx) throws JsonProcessingException{
        int account_id = Integer.valueOf(ctx.pathParam("account_id"));
        System.out.println("account_id: " + account_id);
        Optional<List<Message>> messages = messageService.findAccountMessages(account_id);

        
        ctx.status(200).result(GeneralUtil.convertToJson(messages.get()));
    }
}
