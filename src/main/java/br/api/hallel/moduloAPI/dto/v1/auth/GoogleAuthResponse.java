package br.api.hallel.moduloAPI.dto.v1.auth;

import br.api.hallel.moduloAPI.model.Membro;

public record GoogleAuthResponse(String token, Membro membro) {
}
