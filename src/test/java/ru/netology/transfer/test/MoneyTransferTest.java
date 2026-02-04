package ru.netology.transfer.test;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.DashboardPage;
import ru.netology.transfer.page.LoginPageV3;

public class MoneyTransferTest {
    private LoginPageV3 loginPage;
    private DashboardPage dashboardPage;
    private int initialFirstBalance;
    private int initialSecondBalance;

    @BeforeEach
    void setup() {
        loginPage = Selenide.open("http://localhost:9999",
                LoginPageV3.class);
        // залогинились
        var info = DataHelper.getAuthInfo();
        var verificationCode = DataHelper.getVerificationCodeFor(info);
        var verificationPage = loginPage.validLogin(info);
        dashboardPage = verificationPage.validVerify(verificationCode);
        // сохранили первоначальный баланс карт
        initialFirstBalance = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo());
        initialSecondBalance = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo());
    }

    @Test
    void shouldTransferMoneyBetweenTwoCards() {
        // выбираем карту для оплаты - первую
        var transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getFirstCardInfo());

        // проверяем что она предзаполнена
        Assertions.assertTrue(transferPage.checkToCard(DataHelper.getFirstCardInfo()));
        // переводим деньги и возвращаемся на страницу с картами
        var returningPage = transferPage.transfer("5000", DataHelper.getSecondCardInfo());
        returningPage.updateBalances();

        // сохраняем новые балансы
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());

        // ассертим изменения баланса для обоих карт
        Assertions.assertEquals(initialFirstBalance + 5000, firstCardNewBalance);
        Assertions.assertEquals(initialSecondBalance - 5000, secondCardNewBalance);
    }

    @Test
    public void shouldTransferAllBalanceFromCard() {
        // выбираем карту куда перевести - вторую
        var transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getSecondCardInfo());

        // проверяем что она предзаполнена
        Assertions.assertTrue(transferPage.checkToCard(DataHelper.getSecondCardInfo()));
        // переводим все деньги первой карты и возвращаемся на страницу с картами
        var returningPage = transferPage.transfer(String.valueOf(initialFirstBalance),
                DataHelper.getFirstCardInfo());
        returningPage.updateBalances();

        // сохраняем новые балансы
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());

        // ассертим изменения баланса для обоих карт
        Assertions.assertEquals(0, firstCardNewBalance);
        Assertions.assertEquals(initialSecondBalance + initialFirstBalance, secondCardNewBalance);
    }

    @Test
    public void shouldNotTransferMoneyWhenNegativeBalance() {
        // выбираем карту для оплаты - первую
        var transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getFirstCardInfo());

        // проверяем что она предзаполнена
        Assertions.assertTrue(transferPage.checkToCard(DataHelper.getFirstCardInfo()));
        // переводим деньги и возвращаемся на страницу с картами
        int overdraftAmount = initialSecondBalance + 100000;
        var returningPage = transferPage.transfer(String.valueOf(overdraftAmount),
                DataHelper.getSecondCardInfo());
        returningPage.updateBalances();

        // сохраняем новые балансы
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());

        // ассертим - предположим, что при овердрафте балансы не должны измениться
        Assertions.assertEquals(initialFirstBalance, firstCardNewBalance);
        Assertions.assertEquals(initialSecondBalance, secondCardNewBalance);
    }

    @Test
    public void shouldNotTransferMoneyBetweenSameCard() {
        // выбираем карту для оплаты - первую
        var transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getFirstCardInfo());

        // проверяем что она предзаполнена
        Assertions.assertTrue(transferPage.checkToCard(DataHelper.getFirstCardInfo()));
        // переводим деньги на неё же и возвращаемся на страницу с картами
        var returningPage = transferPage.transfer("5000", DataHelper.getFirstCardInfo());
        returningPage.updateBalances();

        // сохраняем новые балансы
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());

        // балансы не должны измениться
        Assertions.assertEquals(initialFirstBalance, firstCardNewBalance);
        Assertions.assertEquals(initialSecondBalance, secondCardNewBalance);
    }
}