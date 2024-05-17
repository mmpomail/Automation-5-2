package ru.netology.auto;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.when;
import static ru.netology.auto.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.auto.DataGenerator.Registration.getUser;
import static ru.netology.auto.DataGenerator.getRandomLogin;
import static ru.netology.auto.DataGenerator.getRandomPassword;

public class AuthorizationTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    void testSuccessAuthorization() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("h2")
                .shouldHave(Condition.text("Личный кабинет"))
                .shouldBe(Condition.visible);
    }

    @Test
    void testAuthorizationForNonRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void testAuthorizationForBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Пользователь заблокирован"))
                .shouldBe(Condition.visible);
    }

    @Test
    void testForAuthorizationWithWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);

    }

    @Test
    void testForAuthorizationWithEmptyLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("button.button").click();
        $("[data-test-id=login] span.input__sub").shouldHave
                        (Condition.exactText("Поле обязательно для заполнения"))

                .shouldBe(Condition.visible);
    }

    @Test
    void testForAuthorizationWithWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("button.button").click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(Condition.text("Неверно указан логин или пароль"))
                .shouldBe(Condition.visible);
    }

    @Test
    void testForAuthorizationWithEmptyPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("button.button").click();
        $("[data-test-id=password] span.input__sub").shouldHave
                        (Condition.exactText("Поле обязательно для заполнения"))

                .shouldBe(Condition.visible);
    }
}