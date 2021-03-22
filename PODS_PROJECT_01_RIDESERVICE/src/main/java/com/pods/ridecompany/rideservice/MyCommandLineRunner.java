package com.pods.ridecompany.rideservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private final CabRepository cabRepo;
    private final CustomerRepository customerRepo;

    @Autowired
    public MyCommandLineRunner(CabRepository cabRepo, CustomerRepository customerRepo) {
        this.cabRepo = cabRepo;
        this.customerRepo = customerRepo;
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
            Cab cabCredentials = new Cab(Integer.valueOf(st));
            cabRepo.save(cabCredentials);
        }

        while((st=br.readLine())!=null){
            //Halt when line containing "****" is encountered
            if(st.equals("****"))
                break;
            Customer customer = new Customer(Integer.valueOf(st));
            customerRepo.save(customer);
        }
    }
}
