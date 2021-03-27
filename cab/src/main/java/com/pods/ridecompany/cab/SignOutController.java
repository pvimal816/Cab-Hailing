package com.pods.ridecompany.cab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

@Controller
public class SignOutController {
    public final ActiveRideRepository activeRideRepository;
    public final ActiveCabsRepository activeCabsRepository;
    private static final String RIDE_SERVICE_URL = "http://host.docker.internal:8081";
    private static final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public SignOutController(ActiveRideRepository activeRideRepository, ActiveCabsRepository activeCabsRepository) {
        this.activeRideRepository = activeRideRepository;
        this.activeCabsRepository = activeCabsRepository;
    }

    @RequestMapping("/signOut")
    @ResponseBody
    @Transactional
    public boolean signOut(@RequestParam Long cabId){
        if(!activeCabsRepository.existsByCabId(cabId) ||
                activeRideRepository.existsActiveRideByCabId(cabId)){
            return false;
        }
        //send a sign-out request to /rideService
        boolean result = restTemplate.getForObject(
                RIDE_SERVICE_URL+"/cabSignsOut?cabId="+cabId, Boolean.class);
        if(!result)
            return false;
        activeCabsRepository.removeActiveCabByCabId(cabId);
        return true;
    }
}
