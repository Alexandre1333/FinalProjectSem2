package com.fp.finalproject;

import java.util.Scanner;
import java.io.File;
import java.util.LinkedList;
import java.io.FileNotFoundException;

public class QuestionBank {
    private LinkedList<Question> questions;

    public QuestionBank() {
        this.questions = new LinkedList<>();
    }

    public QuestionBank(LinkedList<Question> questions) {
        this.questions = new LinkedList<>();
        this.questions.addAll(questions);
    }

    public LinkedList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(LinkedList<Question> questions) {
        this.questions = new LinkedList<>();
        this.questions.addAll(questions);
    }

    public void printQuestions() {
        for (Question q : this.getQuestions()) {
            System.out.println(q);
        }
    }

    public void readMCQ(String fname) {
        try {
            File fileObj = new File(fname);
            Scanner scanner = new Scanner(fileObj);

            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                String[] items = line.split("\\.");
                String questionText = items[1];
                LinkedList<String> options = new LinkedList<>();
                String firstThreeLetters;
                do {
                    line = scanner.nextLine().trim();
                    firstThreeLetters = line.substring(0, 3);
                    if (!firstThreeLetters.equals("ANS")) {
                        String optionText = line.substring(2);
                        options.add(optionText);
                    }
                } while (!firstThreeLetters.equals("ANS"));
                String correctAnswer = line.substring(4).trim();

                MCQuestion question = new MCQuestion(questionText, correctAnswer, QuestionType.MCQ, options);

                questions.add(question);
                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error file not found: " + fname);
        }
    }

    public void readTFQ(String fname) {
        try {
            File fileObj = new File(fname);
            Scanner scanner = new Scanner(fileObj);

            while (scanner.hasNext()) {
                String line = scanner.nextLine().trim();
                String[] items = line.split("\\.");
                String questionText = items[1];
                LinkedList<String> options = new LinkedList<>();
                String firstThreeLetters;
                do {
                    line = scanner.nextLine().trim();
                    firstThreeLetters = line.substring(0, 3);
                    if (!firstThreeLetters.equals("ANS")) {
                        String optionText = line.substring(2);
                        options.add(optionText);
                    }
                } while (!firstThreeLetters.equals("ANS"));
                String correctAnswer = line.substring(4).trim();

                TFQuestion question = new TFQuestion(questionText, correctAnswer, QuestionType.TFQ);

                questions.add(question);
                System.out.println(line);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error file not found: " + fname);
        }
    }
    public LinkedList<Question> selectRandQuestions(int[] indeces){
        LinkedList<Question> examQuestions = new LinkedList();
        int currentIndex = 0;
        try {
            for (int i : indeces) {
                currentIndex = i;
                Question question = this.getQuestions().get(i);
                examQuestions.add(question);
            }
        }catch (IndexOutOfBoundsException ex){
            System.out.println("Number of questions is " + this.getQuestions().size() + " , Index " + currentIndex + "is out of bound");
        }
        return examQuestions;
    }
}