package Controller;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;

/**
 * Central controller 
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    AccountDAO accountDAO = new AccountDAO();
    AccountService accountService = new AccountService(accountDAO);
    AccoutController accoutController = new AccoutController(accountService);
    // 
    MessageDAO messageDAO = new MessageDAO();
    MessageService messageService = new MessageService(messageDAO, accountService);
    MessageController messageController = new MessageController(messageService);

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("register", accoutController::registerAccount);
        app.post("/login", accoutController::login);
        //
        app.get("/messages", messageController::findAll);
        app.get("/messages/{message_id}", messageController::findById);
        app.patch("/messages/{message_id}", messageController::updateMessage);
        app.put("/messages/{message_id}", messageController::updateMessage);
        app.post("/messages", messageController::save);
        app.delete("/messages/{id}", messageController::delete);
        app.get("/accounts/{account_id}/messages", messageController::findAccountMessages);
        
        return app;
    }

}