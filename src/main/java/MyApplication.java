import org.jsoup.Jsoup;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyApplication {

    public static void main(String[] args) {
        System.out.println("Пример входной строки https://www.simbirsoft.com/");
        System.out.print("Введите адрес странички: ");
        Scanner scanner = new Scanner(System.in);
        String urlName = scanner.next();
        String textOnly=null;
        String data;

        try{
            URL url = new URL(urlName);
            URLConnection connection = url.openConnection();
            StringBuilder words = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((data = reader.readLine()) != null) {
                words.append(data);
            }
            FileWriter fileWriter = new FileWriter("a.html");
            fileWriter.write(words.toString());

            //textOnly хранит весь текст заданной страницы
            textOnly = Jsoup.parse(words.toString()).text().toUpperCase();

            //создание карты "слово - количество его употребления"
            Map<String, Integer> wordMap = new HashMap<>();
            for (String string : textOnly.split("[^А-ЯA-Z]")) {
                if (string.length() > 0) {
                    if (wordMap.get(string) == null) {
                        wordMap.put(string, 1);
                    } else wordMap.put(string, wordMap.get(string) + 1);
                }
            }

            //вывод результата
            for (Map.Entry<String, Integer> pair : wordMap.entrySet()) {
                String word = pair.getKey();
                int value = pair.getValue();
                System.out.println(word + " " + value);
            }

            //сохранение в базу данных
            DBService.saveToDb(wordMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
