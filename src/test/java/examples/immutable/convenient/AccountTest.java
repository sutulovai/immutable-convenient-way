package examples.immutable.convenient;

import org.junit.jupiter.api.Test;

import static examples.immutable.AccountStatus.*;
import static examples.immutable.convenient.Account.Builder.account;
import static java.lang.String.format;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class AccountTest {

    @Test
    void should_successfully_instantiate() {
        // given
        var id = "example-id";
        var email = "example@example.com";
        var status = VERIFIED;

        // when
        var result = account()
                .id(id)
                .status(status)
                .email(of(email))
                .build();

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getEmail().get()).isEqualTo(email);
    }

    @Test
    void should_validate_when_instantiating_combined() {
        // given
        var created = account()
                .id("example-id")
                .status(CREATED)
                .build();
        var verified = account()
                .id("example-id")
                .status(VERIFIED)
                .email(of("example@example.com"))
                .build();

        // when // then
        assertThatThrownBy(() -> account().build()).isInstanceOf(NullPointerException.class); // no id and status
        assertThatThrownBy(() -> account().id("example-id").build()).isInstanceOf(NullPointerException.class); // no status

        assertThatThrownBy(() -> created.verify(null)).isInstanceOf(NullPointerException.class); // verify with null email
        assertThatThrownBy(() -> created.verify("")) // verify with empty email
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(format("Email must be filled when status %s", VERIFIED));

        assertThatThrownBy(() -> verified.changeEmail(null)).isInstanceOf(NullPointerException.class); // change email to null
        assertThatThrownBy(() -> verified.changeEmail("")) // change email to empty
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(format("Email must be filled when status %s", VERIFIED));
    }

    @Test
    void should_successfully_verify_account() {
        // given
        var created = account()
                .id("example-id")
                .status(CREATED)
                .build();
        var email = "example@example.com";

        // when
        var verified = created.verify(email);

        // then
        assertNotSame(created, verified); // Those are different instances but represent the same entity
        assertThat(created.getEmail()).isEmpty(); // The CREATED account is not changed
        assertThat(created.getStatus()).isEqualTo(CREATED); // The CREATED account is not changed

        assertThat(verified.getId()).isEqualTo(created.getId());
        assertThat(verified.getStatus()).isEqualTo(VERIFIED);
        assertThat(verified.getEmail().get()).isEqualTo(email);
    }

    @Test
    void should_successfully_change_email() {
        // given
        var account = account()
                .id("example-id")
                .status(VERIFIED)
                .email(of("old@old.com"))
                .build();
        var newEmail = "new@new.com";

        // when
        var withNewEmail = account.changeEmail(newEmail);

        // then
        assertThat(withNewEmail.getId()).isEqualTo(account.getId());
        assertThat(withNewEmail.getStatus()).isEqualTo(account.getStatus());
        assertThat(withNewEmail.getEmail()).isEqualTo(of(newEmail));
    }

    @Test
    void should_successfully_deactivate_account() {
        // given
        var account = account()
                .id("example-id")
                .status(VERIFIED)
                .email(of("example@example.com"))
                .build();

        // when
        var deactivated = account.deactivate();

        // then
        assertThat(deactivated.getId()).isEqualTo(account.getId());
        assertThat(deactivated.getStatus()).isEqualTo(INACTIVE);
        assertThat(deactivated.getEmail()).isEqualTo(account.getEmail());
    }

}