package br.api.hallel.moduloAPI.payload.resposta;

import br.api.hallel.moduloAPI.model.Membro;
import br.api.hallel.moduloAPI.model.Role;
import br.api.hallel.moduloAPI.model.StatusMembro;
import lombok.*;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MembroResponse {

    private String id;
    private String nome;
    private String email;
    private StatusMembro statusMembro;
    private Set<Role> roles;
    private Integer idade;
    private String fileImageUrl;
    private String telefone;
    private String cpf;

    public MembroResponse(String id, String nome, String email, StatusMembro statusMembro, Set<Role> roles, String fileImageUrl,
                          String telefone, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.statusMembro = statusMembro;
        this.roles = roles;
        this.fileImageUrl= fileImageUrl;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public MembroResponse toList(Membro membro) {
        MembroResponse response = new MembroResponse();
        response.setId(membro.getId());
        response.setNome(membro.getNome());
        response.setEmail(membro.getEmail());
        response.setStatusMembro(membro.getStatusMembro());
        response.setFileImageUrl(membro.getFileImageUrl());
        response.setTelefone(membro.getTelefone());
        response.setCpf(membro.getCpf());
        response.setIdade(membro.getIdade());
        return response;
    }

    public MembroResponse toResponse(Membro membro) {
        MembroResponse response = new MembroResponse();
        response.setId(membro.getId());
        response.setNome(membro.getNome());
        response.setEmail(membro.getEmail());
        response.setRoles(membro.getRoles());
        response.setStatusMembro(membro.getStatusMembro());
        response.setFileImageUrl(membro.getFileImageUrl());
        response.setTelefone(membro.getTelefone());
        response.setCpf(membro.getCpf());
        response.setIdade(membro.getIdade());
        return response;
    }

}
