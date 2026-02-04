package ru.netology.transfer.steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.DashboardPage;
import ru.netology.transfer.page.LoginPageV3;
import ru.netology.transfer.page.TransferPage;
import ru.netology.transfer.page.VerificationPage;

public class TemplateSteps {
    private static LoginPageV3 loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;
    private static TransferPage transferPage;
    private static DashboardPage returningPage;
    private static int initialFirstBalance;
    private static int initialSecondBalance;

    @Пусть("открыта страница с формой авторизации {string}")
    public void openAuthPage(String url) {
        loginPage = Selenide.open(url, LoginPageV3.class);
    }

    @И("пользователь авторизовывается с именем {string} и паролем {string}")
    public void loginWithNameAndPassword(String login, String password) {
        verificationPage = loginPage.validLogin(login, password);
    }

    @И("пользователь вводит проверочный код {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переходит на страницу со своими картами")
    public void checkBalancesOnDashboard() {
        initialFirstBalance = dashboardPage.getCardBalance(DataHelper.getFirstCardInfo());
        initialSecondBalance = dashboardPage.getCardBalance(DataHelper.getSecondCardInfo());
    }

    @И("переводит {string} рублей со своей второй карты на первую")
    public void transferBetweenCards(String amount) {
        transferPage = dashboardPage.chooseCardToTransfer(DataHelper.getFirstCardInfo());
        returningPage = transferPage.transfer(amount, DataHelper.getSecondCardInfo());
    }

    @И("обновляет баланс на странице со своими картами")
    public void updateCardBalances() {
        returningPage.updateBalances();
    }

    @Тогда("баланс его первой карты должен стать {int} рублей")
    public void checkFirstResultBalance(int result1) {
        int firstCardNewBalance = returningPage.getCardBalance(DataHelper.getFirstCardInfo());
        Assertions.assertEquals(result1, firstCardNewBalance);
    }

    @И("баланс его второй карты должен стать {int} рублей")
    public void checkSecondResultBalance(int result2) {
        int secondCardNewBalance = returningPage.getCardBalance(DataHelper.getSecondCardInfo());
        Assertions.assertEquals(result2, secondCardNewBalance);
    }
}
