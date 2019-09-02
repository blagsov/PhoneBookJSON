import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

/* code written
 * by Zoya Klocheva
 */

public class Main {
    //константы имени и номера
    private static final Pattern NUMBER = Pattern.compile("\\+?[1-9]+");
    private static final Pattern NAME = Pattern.compile("[A-Za-z]+");

    //соответствие/несоответствие паттернам и команды вывода списка. для удобства
    private static boolean isList(String string) {
        return string.equals("LIST");
    }

    private static boolean isName(String string) {
        return NAME.matcher(string).matches() && !isList(string);
    }

    private static boolean isPhone(String string) {
        return NUMBER.matcher(string).matches();
    }


    public static void main(String[] args) throws IOException, ParseException {
        //чтение с консоли
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        //карта "телефонной книги", ключ и значение строковое, упорядоченное
        Map<String, String> phoneBook = new TreeMap<>();
        //множество для возможности поиска по значению
        Set<Map.Entry<String, String>> entrySet = phoneBook.entrySet();
        //цикл, пока не остановишь
        for (; ; ) {
            System.out.println("Please, enter subscriber’s name or number: ");
            String command = reader.readLine().trim();//чтение
            //вывод списка
            if (command.equals("LIST") && isList(command)) {
                for (String name : phoneBook.keySet()) {
                    System.out.println("The name: " + name + ", Number: " + phoneBook.get(name));
                }
            }
            //-------------------------------------------------------------------------------------------------
            //Сохранение книжки на компьютер в JSON-файл
            else if (command.equals("EXPORT")) {
                System.out.println("Input a path for a save JSON-FILE (example: path\\new.JSON) :");
                String path = reader.readLine().trim();
                JSONObject book = new JSONObject();
                for (Map.Entry<String, String> pair : entrySet) {
                    book.put(pair.getKey(), pair.getValue());

                    PrintWriter printWriter = new PrintWriter(path);
                    printWriter.write(String.valueOf(book));
                    printWriter.flush();
                    printWriter.close();
                }
            }
            //--------------------------------------------------------------------------------------------------
            //случай, когда имя введено и известно, выводится вместе с соответствующим номером
            else if (isName(command) && phoneBook.containsKey(command) && !isPhone(command)) {
                System.out.println("The name " + command + " is known, a number is: " + phoneBook.get(command));
            }
            //---------------------------------------------------------------------------------------------
            //случай, когда введеный номер известен, и ищется значение - имя
            else if (isPhone(command) && phoneBook.containsValue(command) && !isName(command)) {
                for (Map.Entry<String, String> pair : entrySet)
                    if (command.equals(pair.getValue())) {
                        System.out.println("The number " + command + " is known, a name is: " + pair.getKey());
                    }
            }
            //-------------------------------------------------------------------------------------------------
            //случай, когда введенного имени нет, добавление его и номера
            else if (!phoneBook.containsKey(command) && isName(command) && !isPhone(command)) {
                System.out.println("The name is not in the database! Please, enter the number:");
                String number = reader.readLine().trim();
                phoneBook.put(command, number);
            }
            //--------------------------------------------------------------------------------------------------
            //случай, когда введенного номера нет, добавление его и имени
            else if (!phoneBook.containsValue(command) && isPhone(command) && !isName(command)) {
                System.out.println("The number is not in the database! Please, enter the name");
                String name = reader.readLine().trim();
                phoneBook.put(name, command);
            }
            //--------------------------------------------------------------------------------------------------
            //остановка программы при вводе "выход"
            else if (command.equals("EXIT")) {
                break;
            }
            //--------------------------------------------------------------------------------------------------
            //вывод ошибки при неправильном вводе
            else {
                System.out.println("Error!");
            }
        }

    }
}
