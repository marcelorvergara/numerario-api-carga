package com.numerario.apicarga.config;

import com.numerario.apicarga.entities.UnidadesNegocioEntity;
import com.numerario.apicarga.repositories.UnidadesNegocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {


    private UnidadesNegocioRepository unidadesNegocioRepository;

    @Autowired
    public DataLoader(UnidadesNegocioRepository unidadesNegocioRepository) {
        this.unidadesNegocioRepository = unidadesNegocioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (unidadesNegocioRepository.count() == 0) {
            unidadesNegocioRepository.save(UnidadesNegocioEntity.builder().nomeUnidade("Unidade Administrativa").codTipoUnidade(1).build());
            unidadesNegocioRepository.save(UnidadesNegocioEntity.builder().nomeUnidade("Unidade Administrativa").codTipoUnidade(5).build());
            unidadesNegocioRepository.save(UnidadesNegocioEntity.builder().nomeUnidade("Ponto de Atendimento").codTipoUnidade(7).build());
            unidadesNegocioRepository.save(UnidadesNegocioEntity.builder().nomeUnidade("Ponto de Atendimento").codTipoUnidade(10).build());
        }
    }
}
