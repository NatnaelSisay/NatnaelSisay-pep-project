package Service;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Util.GeneralUtil;
import Util.ValidationUtil;

/**
 * Provide methods for Message class to interact with the database and perform buisness logic
 */
public class MessageService {
    MessageDAO messageDAO;
    AccountService accountService;

    public MessageService(MessageDAO messageDAO, AccountService accountService){
        this.messageDAO = messageDAO;
        this.accountService = accountService;
    }

    public Optional<Message> save(Message message){
        if(!GeneralUtil.isMessageValid(message)){
            return Optional.empty();
        }

        // check if the user id exist in database
        Optional<Account> userAccount = this.accountService.findById(message.posted_by);
        if(!userAccount.isPresent()) return Optional.empty();

        Message result = this.messageDAO.save(message);
        return result == null ? Optional.empty() : Optional.of(result);
    }

    public Optional<Message> delete(int message_id){
        Optional<Message> message = this.messageDAO.findById(message_id);
        
        if(message.isEmpty()){
            return Optional.empty();
        }

        this.messageDAO.delete(message_id);
        return message;
    }

    public Optional<List<Message>> findAccountMessages(int account_id){
        return this.messageDAO.findByPostedById(account_id);
    }

    public Optional<List<Message>> findAll(){
        return this.messageDAO.findAll();
    }

    public Optional<Message> findById(int message_id){
        return this.messageDAO.findById(message_id);
    }

    public Optional<Message> updateMessage(int message_id, String messageBody) throws JsonProcessingException{
        Message message = GeneralUtil.extractMessageFromBody(messageBody);

        if(!ValidationUtil.isValidMessage(message)){
            return Optional.empty();
        }

        int rowsUpdated = this.messageDAO.update(message_id, message);
        if(rowsUpdated == 0) return Optional.empty();

        return findById(message_id);
    }
}
