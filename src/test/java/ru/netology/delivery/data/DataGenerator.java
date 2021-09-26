package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Data;
import lombok.Value;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


@Data
public class DataGenerator {
    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        LocalDate localDate = LocalDate.now();
        LocalDate date = localDate.plusDays(shift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deliveryDate = date.format(formatter);
        return deliveryDate;
    }

    public static int generateRandomDateShift() {
        int min = 3;
        int max = 30;
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    public static String generateValidCity() {
        String[] citiesList = {"Майкоп", "Горно-Алтайск", "Уфа", "Улан-Удэ", "Махачкала", "Магас", "Нальчик", "Элиста", "Черкесск", "Петрозаводск", "Сыктывкар", "Симферополь", "Йошкар-Ола", "Саранск",
                "Якутск", "Владикавказ", "Казань", "Кызыл", "Ижевск", "Абакан", "Грозный", "Чебоксары", "Барнаул", "Чита", "Петропавловск-Камчатский", "Краснодар", "Красноярск", "Пермь", "Владивосток", "Ставрополь", "Хабаровск",
                "Благовещенск", "Архангельск", "Астрахань", "Белгород", "Брянск", "Владимир", "Волгоград", "Вологда", "Воронеж", "Иваново", "Иркутск", "Калининград", "Калуга", "Кемерово", "Киров", "Кострома", "Курган", "Курск", "Санкт-Петербург",
                "Гатчина", "Липецк", "Магадан", "Москва", "Красногорск", "Мурманск", "Нижний Новгород", "Великий Новгород", "Новосибирск", "Омск", "Оренбург", "Орёл", "Пенза", "Псков", "Ростов-на-Дону", "Рязань", "Самара", "Саратов", "Южно-Сахалинск",
                "Екатеринбург", "Смоленск", "Тамбов", "Тверь", "Томск", "Тула", "Тюмень", "Ульяновск", "Челябинск", "Ярославль", "Севастополь", "Биробиджан", "Нарьян-Мар", "Ханты-Мансийск", "Анадырь", "Салехард"};
        Random random = new Random();
        String city = citiesList[random.nextInt(citiesList.length)];
        return city;
    }

    public static String generateInvalidCity() {
        String[] citiesList = {"Джанкой", "%", "Djankoi"};
        Random random = new Random();
        String city = citiesList[random.nextInt(citiesList.length)];
        return city;
    }

    public static String generateName(String locale) {
        Faker faker = new Faker(new Locale(locale));
        String name = faker.name().fullName();
        return name;
    }

    public static String generatePhone() {
        Faker faker = new Faker();
        String phone = faker.numerify("+7##########");
        return phone;
    }

      public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            String city = generateValidCity();
            String name = generateName(locale);
            String phone = generatePhone();
            return new UserInfo(city, name, phone);
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}

