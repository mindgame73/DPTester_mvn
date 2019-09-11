
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Controller {

    @FXML
    private Label questionLabel;

    @FXML
    private RadioButton radioOne;

    @FXML
    private RadioButton radioTwo;

    @FXML
    private RadioButton radioThree;

    @FXML
    private RadioButton radioFour;

    @FXML
    private Label userLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Slider sizeSlider;

    @FXML
    private Button completeButton;

    @FXML
    private FlowPane workPlace;

    @FXML
    private FlowPane lowerPane;


    private final File inputFile = new File("D:/input2.txt");
    private final File outputFile = new File("D:/results.txt");

    private Question question;
    private int num = 1;
    private double numRightAns = 0.0;
    private boolean isTimeOn = true;

    @FXML
    void initialize() throws IOException {

        if (!inputFile.exists()) {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Нарушена целостность системы");
            alertError.setHeaderText("Отсутствует файл input.dat");
            alertError.setContentText("Скопируйте файл в корень папки \n с программным обеспечением!");
            alertError.showAndWait();
        }

        workPlace.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case DIGIT1: radioOne.setSelected(true); break;
                    case DIGIT2: radioTwo.setSelected(true); break;
                    case DIGIT3: radioThree.setSelected(true); break;
                    case DIGIT4: radioFour.setSelected(true); break;
                }
            }
        });

        lowerPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()){
                    case DIGIT1: radioOne.setSelected(true); break;
                    case DIGIT2: radioTwo.setSelected(true); break;
                    case DIGIT3: radioThree.setSelected(true); break;
                    case DIGIT4: radioFour.setSelected(true); break;
                }
            }
        });


        radioOne.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().width-25);
        radioTwo.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().width-25);
        radioThree.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().width-25);
        radioFour.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().width-25);
        questionLabel.setPrefWidth(Toolkit.getDefaultToolkit().getScreenSize().width);

        ToggleGroup group = new ToggleGroup();
        radioOne.setToggleGroup(group);
        radioTwo.setToggleGroup(group);
        radioThree.setToggleGroup(group);
        radioFour.setToggleGroup(group);


        ArrayList<String> content = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<String> wrongQuestions = new ArrayList<>();

        if(inputFile.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(inputFile));
                String line;
                while ((line = br.readLine())!=null){
                    content.add(line);
                }
            }
            catch (IOException ex){

            }
        }
        else{
            System.out.println("not exist");
        }

        int k = 1;
        for (int i=0;i<content.size();i++){
            if (i==0){
                question = new Question();
                question.setQuestion(content.get(0));
            }
            else if (i == (questions.size()*5)) {
                question = new Question();
                question.setQuestion(content.get(i));
            }
            else if (k==1) {
                String str = content.get(i).trim();
                if (str.substring(str.length()-1).equals(".")){
                    question.setFirst(str.substring(0,str.length()-1));
                    question.setRightAns(str.substring(0,str.length()-1));
                    k++;
                }
                else
                {
                    question.setFirst(str);
                    k++;
                }
            }
            else if (k==2) {
                String str = content.get(i).trim();
                if (str.substring(str.length()-1).equals(".")){
                    question.setSecond(str.substring(0,str.length()-1));
                    question.setRightAns(str.substring(0,str.length()-1));
                    k++;
                }
                else
                {
                    question.setSecond(str);
                    k++;
                }
            }
            else if (k==3) {
                String str = content.get(i).trim();
                if (str.substring(str.length()-1).equals(".")){
                    question.setThird(str.substring(0,str.length()-1));
                    question.setRightAns(str.substring(0,str.length()-1));
                    k++;
                }
                else
                {
                    question.setThird(str);
                    k++;
                }
            }
            else {
                String str = content.get(i).trim();
                if (str.substring(str.length()-1).equals(".")){
                    question.setFourth(str.substring(0,str.length()-1));
                    question.setRightAns(str.substring(0,str.length()-1));
                    questions.add(question);
                    k = 1;
                }
                else
                {
                    question.setFourth(str);
                    questions.add(question);
                    k = 1;
                }
            }
        }

        Collections.shuffle(questions);
        Iterator<Question> iterator = questions.iterator();
        question = iterator.next();

        questionLabel.setText("Вопрос " + num + " из " + questions.size() + ". " + question);
        radioOne.setText(question.getFirst());
        radioTwo.setText(question.getSecond());
        radioThree.setText(question.getThird());
        radioFour.setText(question.getFourth());

        progressBar.setProgress(0.0);

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Регистрация");
        dialog.setHeaderText("Введите ФИО тестируемого");
        dialog.setContentText("ФИО: ");

        Optional<String> result = dialog.showAndWait();
        if (result.toString().equals("")) Platform.exit();
        result.ifPresent(name -> {
            userLabel.setText(name);
        });

        if(userLabel.getText().equals("")) Platform.exit();

        Thread timerThread = new Thread(new Runnable() {
            int minutes = 20;
            double percent = 0.0;
            @Override
            public void run() {
                while(minutes != 0) {
                    try {
                        Thread.sleep(600);
                        progressBar.setProgress(percent=percent+0.05);
                        if (minutes == 0) isTimeOn = false;
                    } catch (InterruptedException e) {

                    }
                }
            }
        });

        timerThread.start();
        Date startDate = new Date();

        completeButton.setOnAction(event -> {
            if (group.getSelectedToggle() != null){
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                if (iterator.hasNext()){
                    if (isTimeOn){
                        if (selected.getText().equals(question.getRightAns())){
                            numRightAns++;
                            num++;
                            question = iterator.next();
                            questionLabel.setText("Вопрос " + (num) + " из " + questions.size() + ". " +     question);
                            radioOne.setText(question.getFirst());
                            radioTwo.setText(question.getSecond());
                            radioThree.setText(question.getThird());
                            radioFour.setText(question.getFourth());
                            group.selectToggle(null);
                        }
                        else
                        {
                            wrongQuestions.add("Вопрос №" + (num) + ". " + questions);
                            num++;
                            question = iterator.next();
                            questionLabel.setText("Вопрос " + (num) + " из " + questions.size() + ". " +     question);
                            radioOne.setText(question.getFirst());
                            radioTwo.setText(question.getSecond());
                            radioThree.setText(question.getThird());
                            radioFour.setText(question.getFourth());
                            group.selectToggle(null);
                        }
                    }
                    else
                    {
                        double percentage = numRightAns/questions.size()*100;
                        String formattedPercentage =  new DecimalFormat("#0.00").format(percentage);
                        Alert alertComplete = new Alert(Alert.AlertType.INFORMATION);
                        alertComplete.setTitle("Время закончилось");
                        alertComplete.setHeaderText("Вы ответили на " + (int)numRightAns + " из " + questions.size() + " вопросов.");
                        alertComplete.setContentText("Ваш процент правильных ответов - " + formattedPercentage + "%");
                        alertComplete.showAndWait();
                        completeButton.setDisable(true);
                        Date stopDate = new Date();
                        try {
                            writeInFile(userLabel.getText(),startDate,stopDate,(int)numRightAns,formattedPercentage,
                                    questions.size(),wrongQuestions);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    if (selected.getText().equals(question.getRightAns())){
                        numRightAns++;
                    }
                    else
                    {
                        wrongQuestions.add("Вопрос №" + (num) + ". " + questions);
                    }
                    timerThread.stop();
                    double percentage = numRightAns/questions.size()*100;
                    String formattedPercentage =  new DecimalFormat("#0.00").format(percentage);
                    Alert alertComplete = new Alert(Alert.AlertType.INFORMATION);
                    alertComplete.setTitle("Поздравляю с прохождением теста");
                    alertComplete.setHeaderText("Вы ответили на " + (int)numRightAns + " из " + questions.size() + " вопросов.");
                    alertComplete.setContentText("Ваш процент правильных ответов - " + formattedPercentage + "%");
                    alertComplete.showAndWait();
                    completeButton.setDisable(true);
                    Date stopDate = new Date();
                    try {
                        writeInFile(userLabel.getText(),startDate,stopDate,(int)numRightAns,formattedPercentage,
                                questions.size(),wrongQuestions);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Предупреждение");
                alert.setHeaderText("Не выбран ни один вариант ответа!");
                alert.setContentText("Пожалуйста выберите вариант ответа.");
                alert.showAndWait();
            }
        });

        sizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                radioOne.setStyle("-fx-font-style:italic;-fx-font-size:" + sizeSlider.getValue());
                radioTwo.setStyle("-fx-font-style:italic;-fx-font-size:" + sizeSlider.getValue());
                radioThree.setStyle("-fx-font-style:italic;-fx-font-size:" + sizeSlider.getValue());
                radioFour.setStyle("-fx-font-style:italic;-fx-font-size:" + sizeSlider.getValue());
                questionLabel.setStyle("-fx-font-weight:bold;-fx-font-alignment:center;-fx-font-size:" + (sizeSlider.getValue()));
                switch ((int)sizeSlider.getValue()){
                    case 16: workPlace.setVgap(80); break;
                    case 18: workPlace.setVgap(60); break;
                    case 20: workPlace.setVgap(40); break;
                    case 22: workPlace.setVgap(20); break;
                    case 23: workPlace.setVgap(15); break;
                }
            }
        });

    }

    private void writeInFile(String username,Date startDate, Date stopDate, int numRightAns,
                             String percentage, int numberOfQuestions, ArrayList<String> list) throws IOException {

        if (!outputFile.exists()) outputFile.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
        bw.newLine();
        bw.write("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        bw.newLine();
        bw.write("ФИО: " + username);
        bw.newLine();
        bw.write("Время начала: " + startDate.toString());
        bw.newLine();
        bw.write("Время окончания: " + stopDate.toString());
        bw.newLine();
        bw.write("Количество правильных ответов: " + numRightAns + " из " + numberOfQuestions);
        bw.newLine();
        bw.write("Процент правильных ответов: " + percentage + "%");
        if (list.size()!=0){
            bw.newLine();
            bw.write("Неверные ответы были даны в следующих вопросах: ");
            for (String wrongQuestion : list) {
                bw.newLine();
                bw.write(wrongQuestion);
            }
        }
        bw.flush();
        Platform.exit();
    }
}
