package Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;

public class GeneralUtil {
    static final ObjectMapper objectMapper = new ObjectMapper();

    public static Account extractAccountFromBody(String body) throws JsonProcessingException{
        if(body == null || body.trim().isEmpty()){
            return null;
        }

        Account account = objectMapper.readValue(body, Account.class);
        return account;
    }

    public static String convertAccountObjToJson(Account account) throws JsonProcessingException{
        return objectMapper.writeValueAsString(account);
    }
    
}
