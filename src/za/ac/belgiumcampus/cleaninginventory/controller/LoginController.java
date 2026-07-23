package za.ac.belgiumcampus.cleaninginventory.controller;

import za.ac.belgiumcampus.cleaninginventory.model.User;
import za.ac.belgiumcampus.cleaninginventory.service.AuthenticationService;
import za.ac.belgiumcampus.cleaninginventory.utils.ActionResult;

public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController() {
        this(new AuthenticationService());
    }

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public ActionResult<User> login(String username, String password) {
        try {
            if (!authenticationService.login(username, password)) {
                return ActionResult.fail("Invalid username or password");
            }
            return ActionResult.ok("Login successful", authenticationService.getCurrentUser());
        } catch (IllegalArgumentException e) {
            return ActionResult.fail(e.getMessage());
        } catch (RuntimeException e) {
            return ActionResult.fail("Login failed: " + e.getMessage());
        }
    }
}
