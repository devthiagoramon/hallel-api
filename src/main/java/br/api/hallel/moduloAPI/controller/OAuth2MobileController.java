package br.api.hallel.moduloAPI.controller;

import br.api.hallel.moduloAPI.dto.v1.auth.GoogleAuthRequest;
import br.api.hallel.moduloAPI.dto.v1.auth.GoogleAuthResponse;
import br.api.hallel.moduloAPI.exceptions.main.LoginGoogleException;
import br.api.hallel.moduloAPI.model.ERole;
import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.model.Role;
import br.api.hallel.moduloAPI.model.StatusMembro;
import br.api.hallel.moduloAPI.repository.MembroRepository;
import br.api.hallel.moduloAPI.repository.RoleRepository;
import br.api.hallel.moduloAPI.security.services.JwtService;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuth2MobileController {

    private final JwtService jwtService;
    private final MembroRepository membroRepository;
    private final RoleRepository roleRepository;
    private static final String CLIENT_ID = "1060759694626-c98qb76632sh0ocgm908006ap7gfvur1.apps.googleusercontent.com";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @PostMapping("/google/mobile")
    public ResponseEntity<GoogleAuthResponse> authenticateGoogleMobile(
            @RequestBody
            GoogleAuthRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), JSON_FACTORY)
                    .setAudience(Collections.singleton(CLIENT_ID))
                    .build();
            GoogleIdToken idToken = verifier.verify(request.idToken());

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String meail = payload.getEmail();
                String name = (String) payload.get("name");

                Optional<Membro> usuarioOptional = membroRepository.findByEmail(meail);
                Membro usuario = usuarioOptional.orElseGet(() -> {
                    Membro membro = new Membro();
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String senhaAleatoria = UUID.randomUUID()
                                                .toString()
                                                .substring(0, 8);
                    HashSet<Role> roles = new HashSet<>();
                    List<Role> rolesBD = roleRepository.findAll();

                    rolesBD.forEach(role -> {
                        if (role.getName() == ERole.ROLE_USER) {
                            roles.add(role);
                        }
                    });
                    membro.setEmail(meail);
                    membro.setNome(name);
                    membro.setSenha(encoder.encode(senhaAleatoria));
                    membro.setStatusMembro(StatusMembro.ATIVO);
                    membro.setRoles(roles);
                    return membroRepository.save(membro);

                });
                String jwt = jwtService.generateToken(usuario);
                atualizarTokenMembro(usuario, jwt.replace("Bearer ", ""));
                return ResponseEntity.ok(new GoogleAuthResponse(jwt, usuario));
            }
        } catch (Exception e) {
            throw new LoginGoogleException("Token invalido");
        }
        throw new LoginGoogleException("Token invalido");
    }

    public void atualizarTokenMembro(Membro membro, String token) {
        membro.setToken(token);
        membroRepository.save(membro);
    }

}
