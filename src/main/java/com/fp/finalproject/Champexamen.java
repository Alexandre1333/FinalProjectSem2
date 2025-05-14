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

        int[] indices = {11, 0, 5, 2, 12, 3, 7, 8, 4};

        LinkedList<Question> examQuestionList = myBank.selectRandQuestions(indices);
        System.out.println("Selected questions for exam: " + examQuestionList.size());
        myExam = new Exam(examQuestionList);
        myExam.printAllQuestions();

        VBox root = new VBox();
        MenuBar menuBarMain = buildMenuBar();
        HBox hBoxGrade = new HBox(labelGrade);
        hBoxGrade.setAlignment(Pos.CENTER);

        VBox[] vBoxesQuestions = buildQuestionVBoxes();

        root.getChildren().add(menuBarMain);
        root.getChildren().add(buildBanner());
        root.getChildren().add(hBoxGrade);
        root.getChildren().add(new Separator());
        VBox vBoxAllQuestions = new VBox(20);
        vBoxAllQuestions.getChildren().addAll(vBoxesQuestions);
        ScrollPane scrollPaneForQuestions = new ScrollPane(vBoxAllQuestions);

        root.getChildren().addAll(scrollPaneForQuestions);
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
            int qNumber = i+1;
            Question question = myExam.getQuestion(qNumber);
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
                recordAnswer(questionNumber, "T");
                setQuestionAnswer(questionNumber, "true");
            });

            radioButtonFalse.setOnAction(e -> {
                recordAnswer(questionNumber, "F");
                setQuestionAnswer(questionNumber, "false");
            });

            HBox hBox = new HBox(10,radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(5,labelQuestion, hBox);
        return vBox;
    }

    private void recordAnswer(int n, String answer) {
        this.myExam.addSubmittedAnswer(n, answer);
        System.out.println( n + " -> " + this.myExam.getSubmittedAnswers(n));
    }

    public VBox buildMCQ(int questionNumber, MCQuestion mcQuestion) {
        String qText = String.format("Q%d. %s", questionNumber, mcQuestion.getQuestionText().trim());
        Label labelQuestionText = new Label(qText);
        VBox vBox = new VBox(5,labelQuestionText);
        LinkedList<String> options = mcQuestion.getOptions();
        ToggleGroup toggleGroup = new ToggleGroup();

        String[] optionLetters = {"A","B","C","D","E","F","G"};

        for (int i=0; i<options.size(); i++) {
            String optionText = optionLetters[i]  + ". " + options.get(i);
            RadioButton radioButton = new RadioButton(optionText);
            radioButton.setToggleGroup(toggleGroup);
            vBox.getChildren().add(radioButton);
            int finalI = i;
            radioButton.setOnAction(e -> {
                recordAnswer(questionNumber, optionLetters[finalI]);
                setQuestionAnswer(questionNumber, optionLetters[finalI]);

            });
        }
        return vBox;
    }

    private HBox buildBanner() {
        Image imageObj = new Image("/logo.png");
        ImageView imageViewLogo = new ImageView(imageObj);
        imageObj = new Image("/banner.gif");
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
        submit.setOnAction(new SubmitEventHandler(this.myExam));

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
        myExam.submittedAnswers.clear();
        labelGrade.setText("Grade: ");
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
        mHelpAbout.setOnAction(e -> showAboutAppWindow());
        menuAbout.getItems().addAll(mHelp, mHelpAbout);

        MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuQuiz, menuExtras, menuAbout);
        return menuBar;
    }
    private void showAboutAppWindow() {
        Stage aboutStage = new Stage();
        aboutStage.setTitle("About ChampExamen (R) application (C)");

        Label infoLabel = new Label("ChampExamen (R) v1.0\nCreated by Alexandre And Dylan for Champlain College\nWinter 2025\n All rights reserved.");

        VBox vbox = new VBox(infoLabel);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 200);
        aboutStage.setScene(scene);
        aboutStage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public class SubmitEventHandler implements EventHandler<ActionEvent> {
        private Exam examObj;
        public SubmitEventHandler(Exam myExam) {


            this.examObj = myExam;
        }

        @Override
        public void handle(ActionEvent actionEvent){
            System.out.println("Sumbit button clicked");
            int numberOfQuestions = this.examObj.getQuestions().size();
            int grade = 0;
                    for (int i=1; i <= numberOfQuestions; i++){
                        Question question = this.examObj.getQuestion(i);
                        String correctAnswer = question.getCorrectAnswer();
                        String submittedAnswer = this.examObj.getSubmittedAnswers(i);
                        if (correctAnswer.equals(submittedAnswer)){
                            grade = grade +1;
                        }
                    }
            labelGrade.setText("Grade: " + grade + "/" + numberOfQuestions);
            System.out.println("Debug: Grade = " + grade + "/9");

        }
    }
}