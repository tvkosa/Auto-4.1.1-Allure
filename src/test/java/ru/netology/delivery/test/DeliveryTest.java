package ru.netology.delivery.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setup() {
        open("http://localhost:9999");
        $("[data-test-id=date] input").click();
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL + "A", Keys.DELETE));
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    public void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = DataGenerator.generateRandomDateShift();
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = DataGenerator.generateRandomDateShift();
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible).
                shouldHave(exactText("Успешно!\n" + "Встреча успешно запланирована на " + firstMeetingDate));
        setup();
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification']").shouldHave(exactText("Необходимо подтверждение\n" +
                "У вас уже запланирована встреча на другую дату. Перепланировать?\n" + "Перепланировать"));
        $$("button").find(exactText("Перепланировать")).shouldBe(visible).click();
        $("[data-test-id='success-notification']").shouldBe(visible).
                shouldHave(exactText("Успешно!\n" + "Встреча успешно запланирована на " + secondMeetingDate));
    }

    @Test
    public void shouldRequestIfNoDeliveryToTheCity() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateInvalidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(DataGenerator.generateRandomDateShift()));
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id=city].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldRequestValidDateIfDays2() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(-1));
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible).
                shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    public void shouldMeetingSuccessfullyBookedIfDays4() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(4));
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='success-notification']").shouldBe(visible).
                shouldHave(exactText("Успешно!\n" + "Встреча успешно запланирована  на " +
                        DataGenerator.generateDate(4)));
    }

    @Test
    public void shouldRequestNoRussianLetterInName() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(DataGenerator.generateRandomDateShift()));
        $("[data-test-id=name] input").setValue("Gortchisina Varvara");
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    public void shouldRequestAgreementCheckbox() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(DataGenerator.generateRandomDateShift()));
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='agreement'].input_invalid .checkbox__text").shouldBe(visible)
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    public void shouldRequestIfNotChosenCity() {
        $("[data-test-id=city] input").setValue("");
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(DataGenerator.generateRandomDateShift()));
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub").shouldBe(visible).
                shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldRequestIfNotChosenDate() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=name] input").setValue(DataGenerator.generateName("ru"));
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='date'] .input__sub").shouldBe(visible).
                shouldHave(exactText("Неверно введена дата"));
    }

    @Test
    public void shouldRequestIfEmptyName() {
        $("[data-test-id=city] input").setValue(DataGenerator.generateValidCity());
        $("[data-test-id=date] input").setValue(DataGenerator.generateDate(DataGenerator.generateRandomDateShift()));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue(DataGenerator.generatePhone());
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Запланировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }
}
