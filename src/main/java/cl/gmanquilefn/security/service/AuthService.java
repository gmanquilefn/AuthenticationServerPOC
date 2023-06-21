package cl.gmanquilefn.security.service;

import cl.gmanquilefn.security.model.AuthRequest;
import cl.gmanquilefn.security.model.AuthResponse;
import cl.gmanquilefn.security.entity.Role;
import cl.gmanquilefn.security.entity.UserEntity;
import cl.gmanquilefn.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(AuthRequest request) {

        var user = UserEntity.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        userRepo.save(user);

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password())
        );

        var user = userRepo.findByUsername(request.username()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken);
    }
}
