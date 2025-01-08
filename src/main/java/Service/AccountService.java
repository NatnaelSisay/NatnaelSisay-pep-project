package Service;

import java.util.Optional;

import DAO.AccountDAO;
import Model.Account;
import Util.ValidationUtil;
import io.javalin.http.HttpStatus;

/**
 * Provide methods for Account class to interact with the database and perform buisness logic
 */
public class AccountService {

    AccountDAO accountDAO;

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * Given username fetch Account information
     * 
     * @param username the account username
     * @return account with provided username or null if username doesn't exist
     */
    public Account getAccountByUserName(String username){
        Optional<Account> account = this.accountDAO.getAccountByUserName(username);
        return account.isPresent() ? account.get() : null;
    }

    /**
     * Save new account to database
     * 
     * @param account account object with username and password
     * @return HttpStatus.OK if account is saved else HttpStatus.BAD_REQUEST
     *  this could happen
     *  - the account is null or empty
     *  - invalid length of password
     *  - empty username
     *  - or account already exist by the same username
     */
    public HttpStatus save(Account account){
        if(!ValidationUtil.isValidAccount(account)){
            return HttpStatus.BAD_REQUEST;
        }
        
        Account existingAccount = this.getAccountByUserName(account.username);
        if(existingAccount != null){
            // user already exists
            return HttpStatus.BAD_REQUEST;
        }

        this.accountDAO.save(account);
        return HttpStatus.OK;
    }

    
    /**
     * Find account with the provided username and password information
     * 
     * @param account, account information with username and password
     * @return Optional of account or empty depending on wether the account exist or not
     */
    public Optional<Account> findAccountByUserNameAndPassword(Account account){
        return this.accountDAO.findAccountByUserNameAndPassword(account);
    }
}
