package Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;

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

    public static Message extractMessageFromBody(String body) throws JsonProcessingException {
        if(body == null || body.trim().isEmpty()){
            return null;
        }

        Message message = objectMapper.readValue(body, Message.class);
        return message;
    }

    public static String convertToJson(Message message) throws JsonProcessingException{
        return objectMapper.writeValueAsString(message);
    }
    
    public static boolean isMessageValid(Message message){
        if(message.message_text.trim().isEmpty()) return false;
        if(message.message_text.length() > 255) return false;
        return true;
    }
}
