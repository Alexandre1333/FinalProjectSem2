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
import java.util.LinkedList;

public class Champexamen extends Application {
    private Exam myExam;

    @Override
    public void start(Stage stage) throws IOException {

        QuestionBank myBank = new QuestionBank();
        myBank.readMCQ("src/main/resources/mcq.txt");
        myBank.readMCQ("src/main/resources/tfq.txt");

        int[] indices = {11, 0, 5};

        LinkedList<Question> examQuestionList = myBank.selectRandQuestions(indices);
        myExam = new Exam(examQuestionList);
        myExam.printAllQuestions();

        VBox root = new VBox();
        MenuBar menuBarMain = buildMenuBar();
        Label labelGrade = new Label("Grade: ");
        HBox hBoxGrade = new HBox(labelGrade);
        hBoxGrade.setAlignment(Pos.CENTER);

        VBox[] vBoxesQuestions = buildQuestionVBoxes();

        root.getChildren().add(menuBarMain);
        root.getChildren().add(buildBanner());
        root.getChildren().add(hBoxGrade);
        root.getChildren().add(new Separator());
        root.getChildren().addAll(vBoxesQuestions);
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
                vBoxes[i] = buildTrueFalseQ(1, (TFQuestion) question);
            } else { //it is MCQ
                vBoxes[i] = buildMCQ(2, (MCQuestion) question);
            }
        }
        return vBoxes;
    }


    public VBox buildTrueFalseQ(int questionNumber, TFQuestion tfQuestion1) {
        Label labelQuestion = new Label(tfQuestion1.getQuestionText());
        RadioButton radioButtonTrue = new RadioButton("True");
        RadioButton radioButtonFalse = new RadioButton("False");
        HBox hBox = new HBox(10, radioButtonTrue, radioButtonFalse);
        VBox vBox = new VBox(labelQuestion, hBox);
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
    }

    private void clearExamAnswers() {
    }

    private MenuBar buildMenuBar() {
        Menu menuFile = new Menu("File");
        Menu menuEdit = new Menu("Edit");
        Menu menuQuiz = new Menu("Quiz");
        Menu menuExtras = new Menu("Extras");
        Menu menuAbout = new Menu("About");
        MenuBar menuBar = new MenuBar(menuFile, menuEdit, menuQuiz, menuExtras, menuAbout);
        return menuBar;
    }

    public static void main(String[] args) {
        launch();
    }

    public class SubmitEventHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Submit Clicked");
        }
    }
}