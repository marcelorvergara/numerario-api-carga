package com.numerario.apicarga.controllers;


import com.numerario.apicarga.entities.PontosAtendimentoEntity;
import com.numerario.apicarga.services.PointsOfService;
import com.numerario.apicarga.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/constants")
public class ConstantsLoadController {

    @Autowired
    UsersService usersService;

    @Autowired
    PointsOfService pointsOfService;

    @GetMapping("/load-users")
    public ResponseEntity<String> loadUsersFromFile() {

       var resultUsers = this.usersService.executeUsers();
       return ResponseEntity.status(HttpStatus.OK).body("Foram inseridos: " + resultUsers.size() + " registros de usuários");

//        int[] desiredTerminalsColumns = {2, 4, 5, 7};
//        List<TerminalsEntity> terminalsExcelData = excelUtils.readExcelFile(content, 3, desiredTerminalsColumns);

    }

    @GetMapping("/load-pos")
    public List<PontosAtendimentoEntity> loadPointsOfService() {
        var resultPointOfServices = this.pointsOfService.executePointsOfService();
        return resultPointOfServices;
    }

    @GetMapping("/load-terminals")
    public ResponseEntity<String> loadTerminalsFromFile() {

        return null;
    }
}
