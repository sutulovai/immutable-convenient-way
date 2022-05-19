package examples.immutable.invonvenient;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {

    @Test
    void should_successfully_instantiate_and_validate_nothing() {
        // given
        var account = new Account(null, null, null);

        // when //then
        assertThat(account.getId()).isNull();
        assertThat(account.getEmail()).isNull();
        assertThat(account.getStatus()).isNull();
    }

}