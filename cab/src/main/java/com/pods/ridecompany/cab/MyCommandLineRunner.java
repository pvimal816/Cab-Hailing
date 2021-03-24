package com.pods.ridecompany.cab;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final ActiveCabsRepository activeCabsRepo;
    private final CabCredentialsRepository cabCredentialsRepository;

    @Autowired
    public MyCommandLineRunner(ActiveCabsRepository activeCabsRepo, CabCredentialsRepository cabCredentialsRepository) {
        this.activeCabsRepo = activeCabsRepo;
        this.cabCredentialsRepository = cabCredentialsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        File cabInfoFile = new File("IDs.txt");
        BufferedReader br = new BufferedReader(new FileReader(cabInfoFile));
        String st;

        //skip the first line containing "****"
        br.readLine();
        while((st=br.readLine())!=null){
            //Halt when line containing "****" is encountered
            if(st.equals("****"))
                break;
            CabCredentials cabCredentials = new CabCredentials(Integer.valueOf(st));
            cabCredentialsRepository.save(cabCredentials);
        }
    }
}
