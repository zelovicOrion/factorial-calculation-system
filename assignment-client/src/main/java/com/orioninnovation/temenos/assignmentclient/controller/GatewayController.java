package com.orioninnovation.temenos.assignmentclient.controller;

import com.orioninnovation.temenos.assignmentclient.dto.StartRequest;
import com.orioninnovation.temenos.assignmentclient.service.GatewayService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {
    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }
    @PostMapping("/start")
    public String start(@RequestBody StartRequest request) {
        return gatewayService.startCalculation(request);
    }
}
