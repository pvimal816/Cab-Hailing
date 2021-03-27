package wallet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class WalletController {
    public final LoadDatabase loadDatabase;
    private final WalletRepository repository;

    @Autowired
    public WalletController(LoadDatabase loadDatabase, WalletRepository repository) {
        this.loadDatabase = loadDatabase;
        this.repository = repository;
    }


    @GetMapping("/getBalance")
    @ResponseBody
    public Long getBalance(@RequestParam Long custId){
        try{
            Wallet wallet = repository.findWalletsByCustId(custId).get(0);
            return wallet.balance;
        }catch(IndexOutOfBoundsException e){
            // should not occur
            return -1L;
        }
    }

    @GetMapping("/addAmount")
    @ResponseBody
    public void addAmount(@RequestParam Long custId, @RequestParam Long amount){
        try{
            Wallet wallet = repository.findWalletsByCustId(custId).get(0);
            wallet.balance = wallet.balance+amount;
            repository.save(wallet);
        }catch(IndexOutOfBoundsException e){
            //handle exception
        }
    }

    @GetMapping("/deductAmount")
    @ResponseBody
    public boolean deductAmount(@RequestParam Long custId, @RequestParam Long amount){
        try{
            Wallet wallet = repository.findWalletsByCustId(custId).get(0);
            if(wallet.balance < amount)
                return false;
            wallet.balance = wallet.balance-amount;
            repository.save(wallet);
            return true;
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    @GetMapping("/reset")
    @ResponseBody
    void reset() {
        try {
            loadDatabase.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
