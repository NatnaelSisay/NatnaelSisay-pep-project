package Controller;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
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
        app.post("/messages", messageController::save);
        
        return app;
    }

}