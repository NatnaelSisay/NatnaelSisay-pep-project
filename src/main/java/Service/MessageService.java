package Service;

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
}
