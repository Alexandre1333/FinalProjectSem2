package com.fp.finalproject;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class Champexamen extends Application {
    private Exam myExam;
    private HashMap<Integer,String> submittedAnswers = new HashMap<>();
    Label labelGrade = new Label("Grade: ");
    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readTFQ("src/main/resources/tfq.txt");

        int[] indices = {11, 0, 5};

        LinkedList<Question> examQuestionList = myBank.selectRandQuestions(indices);
        myExam = new Exam(examQuestionList);
        myExam.printAllQuestions();

        VBox root = new VBox();
        MenuBar menuBarMain = buildMenuBar();
        HBox hBoxGrade = new HBox(labelGrade);
        hBoxGrade.setAlignment(Pos.CENTER);

        VBox[] vBoxesQuestions = buildQuestionVBoxes();
        VBox questionsContainer = new VBox(vBoxesQuestions);
        ScrollPane scrollPane = new ScrollPane(questionsContainer);
        scrollPane.setFitToWidth(true);

        root.getChildren().add(menuBarMain);
        root.getChildren().add(buildBanner());
        root.getChildren().add(hBoxGrade);
        root.getChildren().add(new Separator());
        root.getChildren().addAll(scrollPane);
        root.getChildren().add(new Separator());
        root.getChildren().add(buildFooter());


        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("ChampExamen (R) application (C)");
        stage.setScene(scene);
        stage.show();
    }

    private VBox[] buildQuestionVBoxes(){
        int numberOfQuestionsInExam = myExam.questions.size();
        VBox[] vBoxes = new VBox[numberOfQuestionsInExam];

        for (int i = 0; i < numberOfQuestionsInExam; i++) {
            Question question = myExam.getQuestion(i + 1);
            if (question.getQuestionType() == QuestionType.TFQ) {
                vBoxes[i] = buildTrueFalseQ(i + 1, (TFQuestion) question);
            } else { //it is MCQ
                vBoxes[i] = buildMCQ(i + 1, (MCQuestion) question);
            }
        }
        return vBoxes;
    }

    private void setQuestionAnswer(int questionNumber, String answer) {
        submittedAnswers.put(questionNumber, answer);
    }

        public VBox buildTrueFalseQ(int questionNumber, TFQuestion tfQuestion1) {
        Label labelQuestion = new Label("Q" + questionNumber + ": " + tfQuestion1.getQuestionText());
        RadioButton radioButtonTrue = new RadioButton("True");
        RadioButton radioButtonFalse = new RadioButton("False");

        ToggleGroup toggleGroup = new ToggleGroup();
        radioButtonTrue.setToggleGroup(toggleGroup);
        radioButtonFalse.setToggleGroup(toggleGroup);

        radioButtonTrue.setOnAction(e -> {
            if (radioButtonTrue.isSelected()) radioButtonFalse.setSelected(false);;
            setQuestionAnswer(questionNumber, "true");
        });

        radioButtonFalse.setOnAction(e -> {
            if (radioButtonFalse.isSelected()) radioButtonTrue.setSelected(false);;
            setQuestionAnswer(questionNumber, "false");
        });

        HBox hBox = new HBox(10,radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(5,labelQuestion, hBox);
        return vBox;
    }

    public VBox buildMCQ(int questionNumber, MCQuestion mcQuestion) {
        Label labelQuestionText = new Label(mcQuestion.getQuestionText());
        VBox vBox = new VBox(labelQuestionText);
        LinkedList<String> options = mcQuestion.getOptions();
        ToggleGroup toggleGroup = new ToggleGroup();
        for (String s : options) {
            RadioButton radioButton = new RadioButton(s);
            radioButton.setToggleGroup(toggleGroup);
            vBox.getChildren().add(radioButton);
        }
        return vBox;
    }

    private HBox buildBanner() {
        Image imageObj = new Image("/logo.png");
        ImageView imageViewLogo = new ImageView(imageObj);
        imageObj = new Image("/banner.png");
        ImageView imageViewBanner = new ImageView(imageObj);
        imageViewBanner.setPreserveRatio(true);
        imageViewLogo.setPreserveRatio(true);
        imageViewLogo.setFitHeight(100);
        imageViewBanner.setFitHeight(100);
        HBox hBox = new HBox(imageViewBanner, imageViewLogo);
        return hBox;
    }

    private HBox buildFooter() {
        Button clear = new Button("Clear");
        Button save = new Button("Save");
        Button submit = new Button("Submit");

        clear.setOnAction(e -> clearExamAnswers());
        save.setOnAction(e -> saveExamAnswers());
        submit.setOnAction(new SubmitEventHandler());

        HBox hBoxFooter = new HBox(10, clear, save, submit);
        hBoxFooter.setAlignment(Pos.CENTER);
        return hBoxFooter;
    }

    private void saveExamAnswers() {
        System.out.println("Saving exam answers...");
        for (int i = 1; i <= myExam.questions.size(); i++) {
            String answer = submittedAnswers.getOrDefault(i, "Not Answered");
            System.out.println("Q" + i + ": " + answer);
        }
    }

    private void clearExamAnswers() {
        submittedAnswers.clear();
        System.out.println("All answers cleared.");
    }

    private MenuBar buildMenuBar() {
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuQuiz = new Menu("Quiz");
        Menu menuExtras = new Menu("Extras");
        Menu menuAbout = new Menu("About");

        MenuItem mFileOpen = new MenuItem("Open");
        MenuItem mFileSave =  new MenuItem("Save");
        MenuItem mFileExit = new MenuItem("Exit");
        menuFile.getItems().addAll(mFileOpen, mFileSave, mFileExit);

        MenuItem mEditCut = new MenuItem("Cut");
        MenuItem mEditCopy = new MenuItem("Copy");
        MenuItem mEditPaste = new MenuItem("Paste");
        menuEdit.getItems().addAll(mEditCut, mEditCopy, mEditPaste);

        MenuItem mQuizStart = new MenuItem("Start Quiz");
        MenuItem mQuizResults = new MenuItem("View Results");
        menuQuiz.getItems().addAll(mQuizStart, mQuizResults);

        MenuItem mExtraSettings = new MenuItem("Settings");
        MenuItem mExtraAbout = new MenuItem("About");
        menuExtras.getItems().addAll(mExtraSettings, mExtraAbout);

        MenuItem mHelp = new MenuItem("Help Content");
        MenuItem mHelpAbout = new MenuItem("About App");
        menuAbout.getItems().addAll(mHelp, mHelpAbout);

        MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuQuiz, menuExtras, menuAbout);
        return menuBar;
    }

    public static void main(String[] args) {
        launch();
    }

    public class SubmitEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            int correctCount = 0;

            for (int i = 0; i < myExam.questions.size(); i++) {
                Question q = myExam.getQuestion(i);
                String submitted = submittedAnswers.get(i);
                if (submitted != null && submitted.equalsIgnoreCase(q.getCorrectAnswer())) {
                    correctCount++;
                }
            }
            double grade = (double) correctCount / myExam.questions.size() * 100;
            labelGrade.setText(String.format("Grade: %.2f", grade));
            System.out.printf("Grade: %.2f\n", grade);
        }
    }
}