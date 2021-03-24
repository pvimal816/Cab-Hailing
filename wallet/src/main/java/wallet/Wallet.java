package wallet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Wallet {

    @Id
    Integer custId;
    Integer balance;

    public Wallet(){
    }

    public Wallet(Integer id, Integer balance) {
        this.custId = id;
        this.balance = balance;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
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
