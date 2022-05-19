package examples.immutable.invonvenient;

import examples.immutable.AccountStatus;

import java.util.Objects;

public class Account {

    private final String id;
    private final AccountStatus status;
    private final String email;

    public Account(String id, AccountStatus status, String email) {
        this.id = id;
        this.status = status;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && status == account.status && Objects.equals(email, account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, email);
    }

    public String getId() {
        return id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }
}
