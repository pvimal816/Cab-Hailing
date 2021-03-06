package wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;

public interface WalletRepository extends Repository<Wallet, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    void save(Wallet wallet);
    
    @Query(value = "select custId from Wallet")
    List<Long> allIds();
    Wallet findByCustId(Long custId);
    boolean existsByCustId(Long custId);
    List<Wallet> findWalletsByCustId(Long custId);
    
    @Query(value = "select sum(balance) from Wallet")
    Long totalBalance();
}
