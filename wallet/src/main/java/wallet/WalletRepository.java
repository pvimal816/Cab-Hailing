package wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.transaction.Transactional;
import java.util.List;

public interface WalletRepository extends Repository<Wallet, Integer> {
    void save(Wallet wallet);
    @Query(value = "select custId from Wallet")
    List<Integer> allIds();
    Wallet findByCustId(Integer custId);
    boolean existsByCustId(Integer custId);
    List<Wallet> findWalletsByCustId(Integer custId);
}
