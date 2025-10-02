package utec.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import utec.api.domain.User;
import utec.api.dto.request.LoginDTO;
import utec.api.dto.response.AuthToken;
import utec.api.exceptions.BusinessException;
import utec.api.security.JwtUtil;
import utec.api.service.UserService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@Valid @RequestBody LoginDTO login) {
        try {
            // Verificar si el usuario existe
            User user = userService.findByEmail(login.getEmail())
                    .orElseThrow(() -> new BusinessException("Unknown email"));

            // Autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword())
            );

            // Generar token JWT
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new AuthToken(token));

        } catch (Exception e) {
            throw new BusinessException("Wrong password");
        }
    }
}
