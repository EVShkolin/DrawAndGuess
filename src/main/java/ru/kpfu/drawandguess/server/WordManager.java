package ru.kpfu.drawandguess.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class WordManager {

    private static final String[] words = loadWords();
    private static final Random random = new Random();

    private static String[] loadWords() {
        String[] words = new String[200];
        int index = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
            String word = br.readLine();
            while (word != null) {
                words[index++] = word;
                word = br.readLine();
            }
        } catch (IOException e) {}
        return words;
    }

    public static String[] getThreeWords() {
        int firstIndex = random.nextInt(200);
        int secondIndex = random.nextInt(200);
        int thirdIndex = random.nextInt(200);
        while (secondIndex == firstIndex) {
            secondIndex = random.nextInt(200);
        }
        while (thirdIndex == firstIndex || thirdIndex == secondIndex) {
            thirdIndex = random.nextInt(200);
        }


        return new String[]{words[firstIndex], words[secondIndex], words[thirdIndex]};
    }
}
