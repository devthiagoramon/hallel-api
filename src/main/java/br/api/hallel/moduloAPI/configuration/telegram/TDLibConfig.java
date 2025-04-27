package br.api.hallel.moduloAPI.configuration.telegram;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Configuration
public class TDLibConfig {

    @PostConstruct
    public void loadLibrary() {

//        try {
//            // Obtém o diretório onde a aplicação está rodando
//            String projectDir = System.getProperty("user.dir");
//            String libDir = projectDir + File.separator + "libs";
//
//            // Caminho para a DLL dentro do diretório "libs"
//            File libFile = new File(libDir, "tdjni.dll");
//
//            if (!libFile.exists()) {
//                throw new RuntimeException("tdjni.dll não encontrada em " + libFile.getAbsolutePath());
//            }
//
//            // Criar uma cópia temporária para evitar problemas de bloqueio de arquivo
//            File tempLib = File.createTempFile("tdjni", ".dll");
//            Files.copy(libFile.toPath(), tempLib.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//            // Carregar a DLL
//            System.load(tempLib.getAbsolutePath());
//
//            System.out.println("TDLib carregado com sucesso!");
//
//        } catch (IOException | UnsatisfiedLinkError e) {
//            throw new RuntimeException("Erro ao carregar TDLib: " + e.getMessage(), e);
//        }
    }

}

