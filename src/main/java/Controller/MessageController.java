package Controller;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Message;
import Service.MessageService;
import Util.GeneralUtil;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

/**
 * Prodvide Message related method to handle HTTP requests
 * 
 * This class contains methods to
 * - save a message
 * - delete a message
 * - find all messaged
 * - find message by message id
 * - find message by account_id
 * - updatge message
 */
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService service){
        this.messageService = service;
    }
    
    /**
     * Save messaged to database after validating the message content.
     * Valid requests will recive a valid response with status of Http.OK
     * and the saved message
     * 
     * @param context
     * @throws JsonProcessingException
     */
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

        context.status(HttpStatus.OK).result(GeneralUtil.convertToJson(savedMessage.get()));
    }

    /**
     * Delete message from database
     * 
     * @param ctx Javlin context with path parameter of "id"
     */
    public void delete(Context ctx) throws JsonProcessingException{
        String message_id = ctx.pathParam("id");
        Optional<Message> deletedMessage = this.messageService.delete(Integer.valueOf(message_id));
        
        if(deletedMessage.isPresent()){
            ctx.result(GeneralUtil.convertToJson(deletedMessage.get()));
        }

        ctx.status(HttpStatus.OK);
    }
    
    /**
     * Find all messages from specific account_id.
     * If we have no messages from the account return empty list.
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    public void findAccountMessages(Context ctx) throws JsonProcessingException{
        int account_id = Integer.valueOf(ctx.pathParam("account_id"));
        System.out.println("account_id: " + account_id);
        Optional<List<Message>> messages = messageService.findAccountMessages(account_id);

        ctx.status(HttpStatus.OK).result(GeneralUtil.convertToJson(messages.get()));
    }

    /**
     * Find all messages in the database
     * @param ctx
     * @throws JsonProcessingException
     */
    public void findAll(Context ctx) throws JsonProcessingException{
        Optional<List<Message>> messages = this.messageService.findAll();
        ctx.status(HttpStatus.OK).result(GeneralUtil.convertToJson(messages.get()));
    }

    /**
     * Find a message by message_id extracted from path variable
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    public void findById(Context ctx) throws JsonProcessingException{
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Optional<Message> message = this.messageService.findById(message_id);

        if(message.isPresent()){
            ctx.result(GeneralUtil.convertToJson(message.get()));
        }

        ctx.status(HttpStatus.OK);
    }

    /**
     * Update message text for specified message_id.
     * This method require both request body and path variable.
     * 
     * @param ctx
     * @throws JsonProcessingException
     */
    public void updateMessage(Context ctx) throws JsonProcessingException{
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Optional<Message> message = this.messageService.findById(message_id);

        if(message.isEmpty()){
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }

        Optional<Message> updatedMessage = this.messageService.updateMessage(message_id, ctx.body());
        if(updatedMessage.isEmpty()) {
            ctx.status(HttpStatus.BAD_REQUEST);
            return;
        }

        ctx.status(HttpStatus.OK).result( GeneralUtil.convertToJson(updatedMessage.get()) );
    }

}
