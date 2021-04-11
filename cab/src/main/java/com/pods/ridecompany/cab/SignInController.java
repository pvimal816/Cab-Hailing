package com.pods.ridecompany.cab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;

@Controller
public class SignInController {
    public final ActiveCabsRepository activeCabsRepository;
    public final CabCredentialsRepository cabCredentialsRepository;
    private static String RIDE_SERVICE_URL;
    private static RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public SignInController(ActiveCabsRepository activeCabsRepository, CabCredentialsRepository cabCredentialsRepository) {
        RIDE_SERVICE_URL = "http://ride-service-cluster:8081";
        this.activeCabsRepository = activeCabsRepository;
        this.cabCredentialsRepository = cabCredentialsRepository;
    }

    @RequestMapping("/signIn")
    @ResponseBody
    @Transactional
    public boolean signIn(@RequestParam Long cabId, @RequestParam Long initialPos){
        if(!cabCredentialsRepository.existsByCabId(cabId)){
            return false;
        }
        if(activeCabsRepository.existsByCabId(cabId)){
            //cabId is already signed in
            return false;
        }
        //send a sign-in request to /rideService
        boolean result = restTemplate.getForObject(
                RIDE_SERVICE_URL + "/cabSignsIn?cabId=" + cabId + "&initialPos=" + initialPos,
                Boolean.class);
        if(!result)
            return result;

        ActiveCab cab = new ActiveCab(cabId, initialPos);
        activeCabsRepository.save(cab);
        return true;
    }
}
