package examples.immutable.convenient;

import examples.immutable.AccountStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Optional;

import static examples.immutable.AccountStatus.*;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.lang3.Validate.*;

public class Account {

    private final String id;
    private final AccountStatus status;
    private final Optional<String> email;

    public Account(Builder builder) {
        this.id = notEmpty(builder.id);
        this.status = notNull(builder.status);
        this.email = checkEmail(builder.email);
    }

    public Account verify(String email) {
        return copy()
                .status(VERIFIED)
                .email(of(email))
                .build();
    }

    public Account changeEmail(String email) {
        return copy()
                .email(of(email))
                .build();
    }

    public Account deactivate() {
        return copy()
                .status(INACTIVE)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id.equals(account.id) && status == account.status && email.equals(account.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, email);
    }

    private Builder copy() {
        return Builder.account()
                .id(id)
                .status(status)
                .email(email);
    }

    private Optional<String> checkEmail(Optional<String> email) {
        isTrue(
                notNull(email).map(StringUtils::isNotBlank).orElse(false) || this.status.equals(CREATED),
                "Email must be filled when status %s",
                this.status
        );
        return email;
    }

    public String getId() {
        return id;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public static final class Builder {

        private String id;
        private AccountStatus status;
        private Optional<String> email = empty();

        private Builder() {
        }

        public static Builder account() {
            return new Builder();
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder status(AccountStatus status) {
            this.status = status;
            return this;
        }

        public Builder email(Optional<String> email) {
            this.email = email;
            return this;
        }

        public Account build() {
            return new Account(this);
        }

    }
}
