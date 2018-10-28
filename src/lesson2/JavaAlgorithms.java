package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.FileInputStream;
import java.util.*;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        try (FileInputStream in = new FileInputStream(inputName)) {

            Scanner sc = new Scanner(in);
            List<Integer> seq = new ArrayList<>();
            while (sc.hasNext()) {
                seq.add(sc.nextInt());
            }
            int quantity = seq.size();
            int[] maxForPos = new int[quantity];
            int[] maxPos = new int[quantity];
            int[] minForPos = new int[quantity];
            int[] minPos = new int[quantity];
            int j;
            for (int i = 0; i < quantity; ++i) {
                j = quantity - i - 1;
                if (i == 0) {
                    minForPos[i] = seq.get(i);
                    minPos[i] = i;
                    maxForPos[j] = seq.get(j);
                    maxPos[j] = j;
                } else {
                    if (seq.get(i) < minForPos[i - 1]) {
                        minForPos[i] = seq.get(i);
                        minPos[i] = i;
                    } else {
                        minForPos[i] = minForPos[i - 1];
                        minPos[i] = minPos[i - 1];
                    }
                    if (seq.get(j) > maxForPos[j + 1]) {
                        maxForPos[j] = seq.get(j);
                        maxPos[j] = j;
                    } else {
                        maxForPos[j] = maxForPos[j + 1];
                        maxPos[j] = maxPos[j + 1];
                    }
                }
            }
            int dif = -1;
            int difPos = -1;
            for (int i = 0; i < quantity; ++i) {
                int curDif = maxForPos[i] - minForPos[i];
                if (curDif > dif) {
                    dif = curDif;
                    difPos = i;
                }
            }

            return new Pair<Integer, Integer>(minPos[difPos] + 1, maxPos[difPos] + 1);
        } catch (Exception e) {
            throw new IllegalArgumentException("Exception while reading file", e);
        }
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        int result = 0;
        for (int i = 1; i <= menNumber; ++i)
            result = (result + choiceInterval) % i;
        return result + 1;
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */
    static public String longestCommonSubstring(String first, String second) {

        int[][] subStringMatrix = new int[first.length() + 1][second.length() + 1];

        int subStringLength = 0;
        int row = 0;
        int column = 0;

        for (int i = 0; i <= first.length(); i++) {
            for (int j = 0; j <= second.length(); j++) {
                if (i != 0 && j != 0 && first.charAt(i - 1) == second.charAt(j - 1)) {
                    subStringMatrix[i][j] = subStringMatrix[i - 1][j - 1] + 1;
                    if (subStringLength < subStringMatrix[i][j]) {
                        subStringLength = subStringMatrix[i][j];
                        row = i;
                        column = j;
                    }
                } else {
                    subStringMatrix[i][j] = 0;
                }
            }
        }

        StringBuilder subStr = new StringBuilder();
        if (subStringLength != 0) {
            while (subStringMatrix[row][column] != 0) {
                subStr.append(first.charAt(row - 1));
                subStringLength--;
                row--;
                column--;
            }

        }
        return subStr.reverse().toString();
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        if (limit <= 1) {
            return 0;
        }
        boolean[] numbers = new boolean[limit + 1];
        for (int i = 2; i < limit + 1; ++i) {
            numbers[i] = true;
        }
        for (int i = 2; i < numbers.length; ++i) {
            if (numbers[i]) {
                for (int j = 2; i * j < numbers.length; ++j) {
                    numbers[i * j] = false;
                }
            }
        }
        int counter = 0;
        for (int i = 2; i < limit + 1; ++i) {
            if (numbers[i]) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        try (FileInputStream in = new FileInputStream(inputName)) {
            Scanner sc = new Scanner(in);

            List<String> rows = new ArrayList<>();
            while (sc.hasNext()) {
                rows.add(sc.nextLine());
            }

            int qRows = rows.size();
            int qCols = rows.get(0).split(" ").length;
            char[][] table = new char[qRows][qCols];
            for (int i = 0; i < qRows; ++i) {
                String[] chars = rows.get(i).split(" ");
                for (int j = 0; j < qCols; ++j) {
                    table[i][j] = chars[j].charAt(0);
                }
            }

            Set<String> result = new HashSet<>();
            words.forEach(word -> {
                for (int i = 0; i < qRows; ++i) {
                    for (int j = 0; j < qCols; ++j) {
                        if (tryNext(table, word, 0, i, j)) {
                            result.add(word);
                            return;
                        }
                    }
                }
            });
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    static private boolean tryNext(char[][] table, String word, int wordInx, int i, int j) {
        if (wordInx == word.length()) {
            return true;
        }
        char curChar = word.charAt(wordInx);
        if (table[i][j] != curChar) {
            return false;
        } else {
            boolean left = false, top = false, right = false, bot = false;
            int qRows = table.length;
            int qCols = table[0].length;
            if (j > 0) {
                left = tryNext(table, word, wordInx + 1, i, j - 1);
            }
            if (j < qCols - 1) {
                right = tryNext(table, word, wordInx + 1, i, j + 1);
            }
            if (i > 0) {
                bot = tryNext(table, word, wordInx + 1, i - 1, j);
            }
            if (i < qRows - 1) {
                top = tryNext(table, word, wordInx + 1, i + 1, j);
            }
            return left || top || right || bot;
        }
    }
}
