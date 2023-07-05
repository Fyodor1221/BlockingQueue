package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue2 = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queue3 = new ArrayBlockingQueue<>(100);
    public static int QUEUELENGTH = 10_000;


    public static void main(String[] args) {
        new Thread(() -> {
            try {
                String str;
                for (int i = 0; i < QUEUELENGTH; i++) {
                    str = generateText("abc", 100_000);
                    queue1.put(str);
                    queue2.put(str);
                    queue3.put(str);
                }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }).start();

        count(queue1, 'a');
        count(queue2, 'b');
        count(queue3, 'c');

    }

    public static void count (BlockingQueue<String> queue, char ch) {
        new Thread(() -> {
            String currentStr;
            String maxStr = null;
            long counter;
            long counterMax = 0;
            for (int i = 0; i < QUEUELENGTH; i++) {
                try {
                    currentStr = queue.take();
                    counter = currentStr.chars()
                            .filter(value -> value == ch)
                            .count();
                    if (counter > counterMax) {
                        counterMax = counter;
                        maxStr = currentStr;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Наибольшее количество " + ch + " - " + counterMax + " в строке:\n" + maxStr);
        }).start();
    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}