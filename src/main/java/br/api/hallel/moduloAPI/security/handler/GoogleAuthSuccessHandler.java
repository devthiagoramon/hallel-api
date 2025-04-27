package br.api.hallel.moduloAPI.security.handler;

import br.api.hallel.moduloAPI.model.ERole;
import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.model.Role;
import br.api.hallel.moduloAPI.model.StatusMembro;
import br.api.hallel.moduloAPI.repository.MembroRepository;
import br.api.hallel.moduloAPI.repository.RoleRepository;
import br.api.hallel.moduloAPI.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GoogleAuthSuccessHandler
        implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    private final MembroRepository membroRepository;
    private final RoleRepository roleRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain chain,
                                        Authentication authentication) throws
            IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws
            IOException, ServletException {
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Optional<Membro> usuarioOptional = membroRepository.findByEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senhaAleatoria = UUID.randomUUID().toString()
                                    .substring(0, 8);
        HashSet<Role> roles = new HashSet<>();
        List<Role> rolesBD = roleRepository.findAll();

        rolesBD.forEach(role -> {
            if (role.getName() == ERole.ROLE_USER) {
                roles.add(role);
            }
        });

        Membro user;
        if (usuarioOptional.isPresent()) {
            user = usuarioOptional.get();
        } else {
            user = new Membro();
            user.setEmail(email);
            user.setNome(oAuth2User.getName());
            user.setSenha(encoder.encode(senhaAleatoria));
            user.setStatusMembro(StatusMembro.ATIVO);
            user.setRoles(roles);
            membroRepository.save(user);
        }


        String jwt = jwtService.generateToken(user);

        response.setContentType("application/json");
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(jwt));
        response.getWriter().flush();
    }


}
