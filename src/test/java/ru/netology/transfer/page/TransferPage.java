package ru.netology.transfer.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.transfer.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.page;

public class TransferPage {

    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final SelenideElement amountField = $("[data-test-id=amount] input");
    private final SelenideElement fromCardField = $("[data-test-id=from] input");
    private final SelenideElement toCardField = $("[data-test-id=to] input[disabled]");
    private final SelenideElement transferButton = $("button[data-test-id=action-transfer]");
    private final SelenideElement cancelButton = $("button[data-test-id=action-cancel]");


    public TransferPage() {
        heading.shouldBe(Condition.visible);
        $("h1").shouldHave(Condition.exactText("Пополнение карты"));
    }

    public DashboardPage transfer(String amount, DataHelper.CardInfo cardInfo) {
        amountField.setValue(amount);
        fromCardField.setValue(cardInfo.getNumber());
        transferButton.click();
        return page(DashboardPage.class);
    }

    public DashboardPage cancel() {
        cancelButton.click();
        return page(DashboardPage.class);
    }

   public boolean checkToCard(DataHelper.CardInfo cardInfo) {
        String cardMask = toCardField.getAttribute("value");
        String expectedDigits = cardInfo.getLastDigits();
       return cardMask.trim().endsWith(expectedDigits);
    }
}
