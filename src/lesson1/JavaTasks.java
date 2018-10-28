package lesson1;

import kotlin.NotImplementedError;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
     * каждый на отдельной строке. Пример:
     *
     * 13:15:19
     * 07:26:57
     * 10:00:03
     * 19:56:14
     * 13:15:19
     * 00:40:31
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 00:40:31
     * 07:26:57
     * 10:00:03
     * 13:15:19
     * 13:15:19
     * 19:56:14
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) {
        try (FileInputStream in = new FileInputStream(inputName);
             PrintWriter out = new PrintWriter(outputName)) {
            Scanner sc = new Scanner(in);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            List<Date> dates = new ArrayList<>();
            while (sc.hasNext()) {
                dates.add(sdf.parse(sc.next()));
            }
            Collections.sort(dates);
            dates.forEach(date -> out.println(sdf.format(date)));
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while reading file", e);
        }
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        try (FileInputStream in = new FileInputStream(inputName);
             PrintWriter out = new PrintWriter(outputName)) {

            Scanner sc = new Scanner(in);
            Map<String, List<String>> addressMap = new HashMap<>();
            String addressInfo;
            String[] addressParts;

            while (sc.hasNext()) {
                addressInfo = sc.nextLine();
                addressParts = addressInfo.split("-");
                if (addressParts.length != 2) {
                    throw new IllegalArgumentException();
                }
                String currentAddress = addressParts[1].trim();
                List<String> people = addressMap.getOrDefault(currentAddress, new ArrayList<>());
                if (people.isEmpty()) {
                    addressMap.put(currentAddress, people);
                }
                people.add(addressParts[0].trim());
            }
            addressMap.keySet().stream().sorted().forEach(address -> {
                String people = addressMap.get(address).stream().sorted().collect(Collectors.joining(", "));
                out.println(address + " - " + people);
            });

        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while reading file", e);
        }
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) {
        try (FileInputStream in = new FileInputStream(inputName);
             PrintWriter out = new PrintWriter(outputName)) {

            Scanner sc = new Scanner(in);
            List<Double> list = new ArrayList<>();
            Double temperature;
            while (sc.hasNext()) {
                temperature = Double.parseDouble(sc.nextLine());
                list.add(temperature);
            }
            Collections.sort(list);
            list.forEach(out::println);
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while reading file", e);
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        try (FileInputStream in = new FileInputStream(inputName);
             PrintWriter out = new PrintWriter(outputName)) {

            Scanner sc = new Scanner(in);
            List<Integer> seq = new ArrayList<>();
            Map<Integer, Integer> map = new HashMap<>();
            Integer maxCount = 0;
            Integer maxValue = -1;
            while (sc.hasNext()) {
                Integer cur = sc.nextInt();
                seq.add(cur);
                Integer count = map.getOrDefault(cur, 0) + 1;
                if (maxCount < count || (maxCount.equals(count) && cur < maxValue)) {
                    maxCount = count;
                    maxValue = cur;
                }
                map.put(cur, count);
            }

            Integer finalMaxValue = maxValue;
            seq.forEach(el -> {
                if (!el.equals(finalMaxValue)) {
                    out.println(el);
                }
            });
            for (int i = 0; i < maxCount; ++i) {
                out.println(finalMaxValue);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while reading file", e);
        }
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        int firstPointer = 0;
        int secondPointer = first.length;

        for (int i = 0; i < second.length; ++i) {
            if (firstPointer == first.length) {
                continue;
            }
            if (secondPointer == second.length) {
                second[i] = first[firstPointer++];
                continue;
            }
            if (first[firstPointer].compareTo(second[secondPointer]) < 0) {
                second[i] = first[firstPointer++];
            } else {
                second[i] = second[secondPointer++];
            }
        }
        Arrays.stream(second).forEach(System.out::println);
    }
}
