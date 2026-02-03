package ru.netology.transfer.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.LoginPageV3;

public class MoneyTransferTest {
    private LoginPageV3 loginPage;

    @BeforeEach
    void setup() {
        loginPage = Selenide.open("http://localhost:9999", LoginPageV3.class);
    }


    @Test
    void shouldTransferMoneyBetweenOwnCards() {

        var info = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var verificationPage = loginPage.validLogin(info);
        var dashboardPage = verificationPage.validVerify(verificationCode);

    }
}