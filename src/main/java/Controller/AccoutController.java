package Controller;

import io.javalin.http.Context;

import io.javalin.http.HttpStatus;

import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;

import Model.Account;
import Service.AccountService;
import Util.GeneralUtil;


/**
 * Provides methods to handle all HTTP requests provided as Javlin Context.
 * 
 * This class contains methods to
 * - register a new account
 * - authenticate a user account
 */
public class AccoutController {

    AccountService accountService;
    
    public AccoutController(AccountService service){
        this.accountService = service;
    }
    
    /**
     * Handle account registration from javlin context
     * 
     * @param context Javlin context containing account information
     * @throws JsonProcessingException will be thrown if there is issue with the request body
     */

    public void registerAccount(Context context) throws JsonProcessingException{

        Account account = GeneralUtil.extractAccountFromBody(context.body());
        
        if(account == null){
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        HttpStatus status = this.accountService.save(account);
        
        if(status == HttpStatus.BAD_REQUEST) {
            context.status(status); 
            return;
        }

        account = this.accountService.getAccountByUserName(account.username);
        context.status(status).result(GeneralUtil.convertAccountObjToJson(account));
    }
 

    /**
     * Handle authentication from javlin context
     * 
     * @param context Javlin context containing account information
     * @throws JsonProcessingException will be thrown if there is issue with the request body
     */

    public void login(Context context) throws JsonProcessingException{
        Account account = GeneralUtil.extractAccountFromBody(context.body());
        
        if(account == null){
            context.status(HttpStatus.BAD_REQUEST);
            return;
        }

        Optional<Account> extractedAccount = this.accountService.findAccountByUserNameAndPassword(account);

        if(extractedAccount.isEmpty()){
            context.status(HttpStatus.UNAUTHORIZED);
            return;
        }

        context.status(HttpStatus.OK).result(GeneralUtil.convertAccountObjToJson(extractedAccount.get()));
    }

}
