package Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Util.GeneralUtil;

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

    /**
     * Delete message from database given message_id
     * @param message_id
     * @return if the message with message_id exist retrive and delete the message, else
     *  return empty Optional
     */
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
}
