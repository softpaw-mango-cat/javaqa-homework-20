package ru.netology.transfer.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.transfer.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private ElementsCollection cards = $$(".list__item div");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(Condition.visible);
        $("h1").shouldHave(Condition.exactText("Ваши карты"));
    }

    private SelenideElement getCardElement(DataHelper.CardInfo cardInfo) {
        return cards.find(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) {
        String cardText = getCardElement(cardInfo).getText();
        return extractBalance(cardText);
    }

    public TransferPage chooseCardToTransfer(DataHelper.CardInfo cardInfo) {
        getCardElement(cardInfo)
                .$("button[data-test-id=action-deposit]")
                .click();
        return page(TransferPage.class);
    }

    public void updateBalances() {
        $("button[data-test-id=action-reload]").click();
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
}
