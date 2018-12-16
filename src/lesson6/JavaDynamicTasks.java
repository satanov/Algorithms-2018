package lesson6;

import kotlin.NotImplementedError;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        int firstLength = first.length();
        int secondLength = second.length();
        int[][] dp = new int[firstLength+1][secondLength+1];

        for (int i = 0; i < firstLength; i++) {
            for (int j = 0; j < secondLength; j++) {
                if (first.charAt(i) == second.charAt(j)) {
                    dp[i+1][j+1] = dp[i][j] + 1;
                } else {
                    dp[i+1][j+1] = Math.max(dp[i][j+1], dp[i+1][j]);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int x = firstLength, y = secondLength; x != 0 && y != 0; ) {
            if (dp[x][y] == dp[x-1][y]) {
                x--;
            } else if (dp[x][y] == dp[x][y-1]) {
                y--;
            } else {
                sb.append(first.charAt(x-1));
                x--;
                y--;
            }
        }

        return sb.reverse().toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Средняя
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        int size = list.size();
        int[] prev = new int[size];
        int[] dp = new int[size];
        for (int i = 0; i < size; ++i) {
            dp[i] = 1;
            prev[i] = -1;
            for (int j = 0; j < i; ++j) {
                if (list.get(j) < list.get(i) && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    prev[i] = j;
                }
            }
        }
        int pos = 0;
        int length = dp[0];
        for (int i = 0; i < size; ++i) {
            if (dp[i] > length) {
                pos = i;
                length = dp[i];
            }
        }

        List<Integer> result = new ArrayList<>();
        while (pos != -1) {
            result.add(list.get(pos));
            pos = prev[pos];
        }
        Collections.reverse(result);
        return result;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Сложная
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        try {
            Scanner in = new Scanner(new File(inputName));
            StringBuilder sb = new StringBuilder();
            while (in.hasNext()) {
                sb.append(in.nextLine()).append("\n");
            }
            String[] arr = sb.toString().split("\n");
            int rows = arr.length;
            int cols = arr[0].split(" ").length;
            int[][] matrix = new int[rows][cols];

            for (int i = 0 ; i < rows; ++i) {
                String[] row = arr[i].split(" ");
                for (int j = 0; j < cols; ++j) {
                    matrix[i][j] = Integer.parseInt(row[j]);
                }
            }
            for (int i = 0 ; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (i == 0) {
                        if (j != 0) {
                            matrix[i][j] = matrix[i][j] + matrix[i][j-1];
                        }
                    } else {
                        if (j == 0) {
                            matrix[i][j] = matrix[i][j] + matrix[i-1][j];
                        } else {
                            matrix[i][j] = matrix[i][j] + Math.min(Math.min(matrix[i][j-1], matrix[i-1][j]), matrix[i-1][j-1]);
                        }
                    }
                }
            }
            return matrix[rows - 1][cols - 1];
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not open file");
        }
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
