package ua.carrental.lab2.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.carrental.lab2.service.UserService;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Data
    public static class LoginRequest {
        public String username;
        public String password;
    }

    @Data
    @AllArgsConstructor
    private static class LoginResponse {
        public String token;
        public boolean shouldUseAdminFrontend;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        String token = userService.logIn(request.username, request.password);
        return ResponseEntity.ok(new LoginResponse(
                token,
                userService.shouldEnableAdminFrontend(request.username)
        ));
    }

    @Data
    public static class RegisterRequest {
        public long passportId;
        public String username;
        public String password;
    }

    @PostMapping("register")
    public ResponseEntity<LoginResponse> registerAndLogin(@Validated @RequestBody RegisterRequest request) {
        String token = userService.registerAndLogIn(request.passportId, request.username, request.password);
        return ResponseEntity.ok(new LoginResponse(
                token,
                userService.shouldEnableAdminFrontend(request.username)
        ));
    }
}
