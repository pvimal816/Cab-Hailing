package wallet;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

@Component
public class LoadDatabase {
    public final WalletRepository walletRepo;

    @Autowired
    public LoadDatabase(WalletRepository walletRepo){
        this.walletRepo = walletRepo;
    }

    public void init() throws IOException {
        File cabInfoFile = new File("IDs.txt");
        BufferedReader br = new BufferedReader(new FileReader(cabInfoFile));
        String st;

        //skip the first line containing "****"
        br.readLine();
        while((st=br.readLine())!=null)
            if(st.equals("****"))
                break;
        while((st=br.readLine())!=null)
            if(st.equals("****"))
                break;

        st = br.readLine();
        long init_balance = Long.parseLong(st);
        br = new BufferedReader(new FileReader(new File("IDs.txt")));
        br.readLine();
        while((st=br.readLine())!=null)
            if(st.equals("****"))
                break;
        while((st=br.readLine())!=null){
            //Halt when line containing "****" is encountered
            if(st.equals("****"))
                break;
            long customer_id = Long.parseLong(st);
            if(walletRepo.existsByCustId(customer_id)){
                Wallet walletEntry = walletRepo.findByCustId(customer_id);
                walletEntry.balance = init_balance;
                walletRepo.save(walletEntry);
            }else{
                Wallet walletEntry = new Wallet(customer_id, init_balance);
                walletRepo.save(walletEntry);
            }
        }
    }
}
