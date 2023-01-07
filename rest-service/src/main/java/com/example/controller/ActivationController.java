package com.example.controller;


import com.example.service.UserActivationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class ActivationController {
    private final UserActivationService activationService;

    @Autowired
    public ActivationController(UserActivationService activationService) {
        this.activationService = activationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/activation")
    public ResponseEntity<?> activation(@RequestParam("id") String id){
        var res = activationService.activation(id);
        if(res){
            return ResponseEntity.ok().body("Регистрация успешно завершена");
        }

        return ResponseEntity.internalServerError().build();
    }
}
