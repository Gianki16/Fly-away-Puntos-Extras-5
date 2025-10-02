package utec.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utec.api.dto.request.RegisterUserDTO;
import utec.api.dto.response.NewIdDTO;
import utec.api.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@Valid @RequestBody RegisterUserDTO newUser) {
        NewIdDTO result = userService.registerUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok().body((Object) user))  // Cast explícito
                .orElse(ResponseEntity.notFound().build());
    }
}
