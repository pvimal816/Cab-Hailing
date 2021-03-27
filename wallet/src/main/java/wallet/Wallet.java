package wallet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Wallet {

    @Id
    Long custId;
    Long balance;

    public Wallet(){
    }

    public Wallet(Long id, Long balance) {
        this.custId = id;
        this.balance = balance;
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return custId != null ? custId.equals(wallet.custId) : wallet.custId == null;
    }

    @Override
    public int hashCode() {
        return custId != null ? custId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "custId=" + custId +
                ", balance=" + balance +
                '}';
    }
}
