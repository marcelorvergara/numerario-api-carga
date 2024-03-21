package com.numerario.apicarga.controllers;


import com.numerario.apicarga.entities.TerminaisEntity;
import com.numerario.apicarga.services.PontosAtendimentoService;
import com.numerario.apicarga.services.TerminaisService;
import com.numerario.apicarga.services.TipoTerminalService;
import com.numerario.apicarga.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/constants")
public class ConstantsLoadController {

    @Autowired
    UsuariosService usuariosService;

    @Autowired
    PontosAtendimentoService pontosAtendimentoService;

    @Autowired
    TipoTerminalService tipoTerminalService;

    @Autowired
    TerminaisService terminaisService;

    @GetMapping("/load-users")
    public ResponseEntity<Object> loadUsersFromFile() {
        try {
            var resultUsers = this.usuariosService.executeUsers();
            return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultUsers.size() + " registros de usuários");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-pos")
    public ResponseEntity<Object> loadPointsOfServiceFromFile() {
        try {
            var resultPointOfServices = this.pontosAtendimentoService.executePointsOfService();
            return ResponseEntity.status(HttpStatus.OK).body("Forma inseridos: " + resultPointOfServices.size() + " pontos de atendimentos");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-terminals-types")
    public ResponseEntity<Object> loadTerminalTypesFromFile() {
        try {
            var resultTypeTerminal = this.tipoTerminalService.executeTypeOfTerminals();
            return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultTypeTerminal.size() + " tipos de terminais");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-terminals")
    public ResponseEntity<Object> loadTerminalsFromFile() {
        try{
            var resultTerminalService = this.terminaisService.executeTerminals();
            return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultTerminalService.size() + " terminais");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
