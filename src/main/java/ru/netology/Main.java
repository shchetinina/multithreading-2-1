package ru.netology;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int counter = 1000;
        Runnable task = () -> {
            for (int i = 0; i < counter; i++) {
                String result = generateRoute("RLRFR", 100);
                int letterNumber = countLetterInString(result, 'R');
                synchronized (sizeToFreq) {
                    int currentCount = sizeToFreq.get(letterNumber) != null ?
                            sizeToFreq.get(letterNumber) :
                            0;
                    sizeToFreq.put(letterNumber, currentCount + 1);
                }
            }
        };

        final ExecutorService threadPool = Executors.newFixedThreadPool(counter);
        threadPool.execute(task);
        threadPool.shutdown();
        formatMapOutput();
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static int countLetterInString(String text, char item) {
        int count = 0;
        for (char ch : text.toCharArray()) {
            if (item == ch) count++;
        }
        return count;
    }

    public static void formatMapOutput() {
        Map.Entry<Integer, Integer> frequentEntry = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        System.out.printf("Самое частое количество повторений %s (встретилось %s раз)\n",
                frequentEntry.getKey() != null ? frequentEntry.getKey().toString() : null,
                frequentEntry.getValue() != null ? frequentEntry.getValue().toString() : null);
        System.out.println("Другие размеры");
        for (Map.Entry<Integer, Integer> item : sizeToFreq.entrySet()) {
            if (item.equals(frequentEntry)) continue;
            System.out.printf("- %s (%s раз)\n", item.getKey(), item.getValue());
        }
    }
}