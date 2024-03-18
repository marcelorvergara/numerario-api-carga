package com.numerario.apicarga.controllers;


import com.numerario.apicarga.services.PointsOfService;
import com.numerario.apicarga.services.TerminalsTypesService;
import com.numerario.apicarga.services.UsersService;
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
    UsersService usersService;

    @Autowired
    PointsOfService pointsOfService;

    @Autowired
    TerminalsTypesService terminalsTypesService;

    @GetMapping("/load-users")
    public ResponseEntity<Object> loadUsersFromFile() {
        try {
            var resultUsers = this.usersService.executeUsers();
            return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultUsers.size() + " registros de usuários");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-pos")
    public ResponseEntity<Object> loadPointsOfServiceFromFile() {
        try {
            var resultPointOfServices = this.pointsOfService.executePointsOfService();
            return ResponseEntity.status(HttpStatus.OK).body("Forma inseridos: " + resultPointOfServices.size() + " pontos de atendimentos");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-terminals-types")
    public ResponseEntity<Object> loadTerminalTypesFromFile() {
        try {
            var resultTypeTerminal = this.terminalsTypesService.executeTypeOfTerminals();
            return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultTypeTerminal.size() + " tipos de terminais");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/load-terminals")
    public ResponseEntity<String> loadTerminalsFromFile() {
//        int[] desiredTerminalsColumns = {2, 4, 5, 7};
//        List<TerminalsEntity> terminalsExcelData = excelUtils.readExcelFile(content, 3, desiredTerminalsColumns);
        return null;
    }
}
