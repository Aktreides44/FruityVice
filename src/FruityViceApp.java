import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.google.gson.*;

public class FruityViceApp {

    public static void main(String[] args) {
        try {
            // 1. Fetch all fruits
            URL url = new URL("https://www.fruityvice.com/api/fruit/all");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();


            JsonArray fruits = JsonParser.parseString(content.toString()).getAsJsonArray();

            String searchName = "apple"; // lowercase match
            String searchGenus = "malus";
            String nutrientType = "calories";
            double min = 40, max = 100;

            System.out.println("Fruits by name '" + searchName + "':");
            for (JsonElement element : fruits) {
                JsonObject fruit = element.getAsJsonObject();
                if (fruit.get("name").getAsString().toLowerCase().contains(searchName)) {
                    printFruit(fruit);
                }
            }

            System.out.println("\nFruits by genus '" + searchGenus + "':");
            for (JsonElement element : fruits) {
                JsonObject fruit = element.getAsJsonObject();
                if (fruit.get("genus").getAsString().toLowerCase().equals(searchGenus)) {
                    printFruit(fruit);
                }
            }

            System.out.println("\nFruits with " + nutrientType + " between " + min + " and " + max + ":");
            for (JsonElement element : fruits) {
                JsonObject fruit = element.getAsJsonObject();
                JsonObject nutri = fruit.getAsJsonObject("nutritions");
                double value = nutri.get(nutrientType).getAsDouble();
                if (value >= min && value <= max) {
                    printFruit(fruit);
                }
            }

            // === Nutrient Percentage Example ===
            System.out.println("\nNutrient % of sugar for 250g of banana:");
            for (JsonElement element : fruits) {
                JsonObject fruit = element.getAsJsonObject();
                if (fruit.get("name").getAsString().equalsIgnoreCase("banana")) {
                    JsonObject nutri = fruit.getAsJsonObject("nutritions");
                    double sugarPer100g = nutri.get("sugar").getAsDouble();
                    double grams = 250;
                    double percent = (sugarPer100g * grams) / 100;
                    System.out.printf("250g of banana has %.2fg of sugar.%n", percent);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printFruit(JsonObject fruit) {
        System.out.println("Name: " + fruit.get("name").getAsString());
        System.out.println("Genus: " + fruit.get("genus").getAsString());
        System.out.println("Calories: " + fruit.getAsJsonObject("nutritions").get("calories").getAsDouble());
        System.out.println("---");
    }
}
