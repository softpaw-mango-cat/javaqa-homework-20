package ru.netology.transfer.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.LoginPageV3;

public class MoneyTransferTest {
    private LoginPageV3 loginPage;

    @BeforeEach
    void setup() {
        loginPage = Selenide.open("http://localhost:9999",
                LoginPageV3.class);
    }

    @Test
    void shouldTransferMoneyBetweenTwoCards() {
        // залогинились
        var info = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var verificationPage = loginPage.validLogin(info);
        var dashboardPage = verificationPage.validVerify(verificationCode);

        // сохраняем балансы карт
        int firstCardBalance = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardBalance = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo());

        // выбираем карту для оплаты - первую
        var transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getFirstCardInfo());

        // проверяем что она предзаполнена
        Assertions.assertTrue(transferPage.checkToCard(DataHelper.getFirstCardInfo()));
        // переводим деньги и возвращаемся на страницу с картами
        var returningPage = transferPage.transfer("100", DataHelper.getSecondCardInfo());

        // сохраняем новые балансы
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());

        // ассертим изменения баланса для обоих карт
        Assertions.assertEquals(firstCardBalance + 100, firstCardNewBalance);
        Assertions.assertEquals(secondCardBalance - 100, secondCardNewBalance);
    }
}