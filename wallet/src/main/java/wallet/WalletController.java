package wallet;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;


@Controller
public class WalletController {
    public final LoadDatabase loadDatabase;
    private final WalletRepository repository;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public WalletController(LoadDatabase loadDatabase, WalletRepository repository) {
        this.loadDatabase = loadDatabase;
        this.repository = repository;
    }

    @GetMapping("/totalCurrentBalance")
    @ResponseBody
    public Long totalCurrentBalance(){
        return repository.totalBalance();
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
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void addAmount(@RequestParam Long custId, @RequestParam Long amount){
        try{
            // Wallet wallet = repository.findWalletsByCustId(custId).get(0);
            Wallet wallet = em.find(Wallet.class, custId, LockModeType.PESSIMISTIC_WRITE);
            wallet.balance = wallet.balance+amount;
            repository.save(wallet);
        }catch(Throwable e){
            //handle exception
            System.err.println("Error in wallet/addAmount: " + e.getMessage());
        }
    }

    @GetMapping("/deductAmount")
    @ResponseBody
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean deductAmount(@RequestParam Long custId, @RequestParam Long amount){
        try{
            // Wallet wallet = repository.findWalletsByCustId(custId).get(0);
            Wallet wallet = em.find(Wallet.class, custId, LockModeType.PESSIMISTIC_WRITE);
            if(wallet==null)
                return false;
            if(wallet.balance < amount)
                return false;
            wallet.balance = wallet.balance-amount;
            repository.save(wallet);
            return true;
        }catch(Throwable e){
            System.err.println("Potential error in wallet/deductAmount: " + e.getMessage());
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
