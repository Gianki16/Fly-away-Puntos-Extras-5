package utec.api.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utec.api.domain.User;
import utec.api.dto.request.RegisterUserDTO;
import utec.api.dto.response.NewIdDTO;
import utec.api.exceptions.BusinessException;
import utec.api.repository.UserRepository;


import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public NewIdDTO registerUser(RegisterUserDTO registerUserDTO) {
        // Validar si el email ya existe
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new BusinessException("Email already exists");
        }

        // Crear usuario
        User user = modelMapper.map(registerUserDTO, User.class);
        user.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return new NewIdDTO(savedUser.getId().toString());
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }
}

