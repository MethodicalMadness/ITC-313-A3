package task1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.math.RoundingMode;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main extends Application {

    // JDBC driver name and database URL
    static final String driver = "com.mysql.cj.jdbc.Driver";
    static final String dbUrl = "jdbc:mysql://localhost:3306/gradeprocessing?verifyServerCertificate=true&useSSL=false";

    //  Database credentials
    static final String user = "root";
    static final String pass = "admin";

    private double paneWidth = 920;
    private double paneHeight = 500;
    private TextField txId = new TextField();
    private TextField txName = new TextField();
    private TextField txQuiz = new TextField();
    private TextField txA1 = new TextField();
    private TextField txA2 = new TextField();
    private TextField txA3 = new TextField();
    private TextField txExam = new TextField();
    private TextField txResult = new TextField();
    private TextField txGrade = new TextField();
    private TableColumn idCol = new TableColumn("ID");
    private TableColumn nameCol = new TableColumn("StudentName");
    private TableColumn quizCol = new TableColumn("Quiz");
    private TableColumn a1Col = new TableColumn("A1");
    private TableColumn a2Col = new TableColumn("A2");
    private TableColumn a3Col = new TableColumn("A2");
    private TableColumn examCol = new TableColumn("Exam");
    private TableColumn resultsCol = new TableColumn("Results");
    private TableColumn gradeCol = new TableColumn("Grade");
    private TableView tableView = new TableView();
    private Label lbStatus = new Label();
    private Connection connection;
    private ObservableList<Student> studentObsList = FXCollections.observableArrayList();


    @Override
    public void start(Stage primaryStage) throws Exception{
        //load the mysql driver
        loadMysqlDriver();

        //grid for searchRecord and updateRecord fields
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //labels
        Label lbId = new Label("ID");
        Label lbName = new Label("StudentName");
        Label lbQuiz = new Label("Quiz");
        Label lbA1 = new Label("A1");
        Label lbA2 = new Label("A2");
        Label lbA3 = new Label("A3");
        Label lbExam = new Label("Exam");
        Label lbResult = new Label("Result");
        Label lbGrade = new Label("Grade");

        //add labels to grid
        gridPane.add(lbId, 1, 1);
        gridPane.add(lbName, 2, 1);
        gridPane.add(lbQuiz, 3, 1);
        gridPane.add(lbA1, 4, 1);
        gridPane.add(lbA2, 5, 1);
        gridPane.add(lbA3, 6, 1);
        gridPane.add(lbExam, 7, 1);
        gridPane.add(lbResult, 8, 1);
        gridPane.add(lbGrade, 9, 1);


        //add textfields to grid
        gridPane.add(txId, 1, 2);
        gridPane.add(txName, 2, 2);
        gridPane.add(txQuiz, 3, 2);
        gridPane.add(txA1, 4, 2);
        gridPane.add(txA2, 5, 2);
        gridPane.add(txA3, 6, 2);
        gridPane.add(txExam, 7, 2);
        gridPane.add(txResult, 8, 2);
        gridPane.add(txGrade, 9, 2);

        //buttons
        Button btSearch = new Button("Search Table");
        Button btCreateTable = new Button("Create Table");
        Button btUpdate = new Button("Update Record");
        Button btInsert = new Button("Insert Record");
        Button btShowAll = new Button("Show All");
        Button btClearTxFld = new Button("Clear Input");

        //add buttons to grid
        gridPane.add(btShowAll, 1, 3);
        gridPane.add(btSearch, 2, 3);
        gridPane.add(btClearTxFld, 3, 3);
        gridPane.add(btInsert, 4, 3);
        gridPane.add(btUpdate, 5, 3);
        gridPane.add(btCreateTable, 6, 3);

        //button actions
        btSearch.setOnAction(e -> {
            String id = txId.getText();
            if(!isIdValid(id)){
                id = "";
            }
            String name = checkString(txName.getText(), txName);
            String quiz = formatNumberString(txQuiz.getText(), txQuiz);
            String a1 = formatNumberString(txA1.getText(), txA1);
            String a2 = formatNumberString(txA2.getText(), txA2);
            String a3 = formatNumberString(txA3.getText(), txA3);
            String exam = formatNumberString(txExam.getText(), txExam);
            String result = formatNumberString(txResult.getText(), txResult);
            String grade = checkString(txGrade.getText(), txGrade);
            searchRecord(id, name, quiz, a1, a2, a3, exam, result, grade);
        });

        btCreateTable.setOnAction(e -> {
            createTable();
        });

        btClearTxFld.setOnAction(e -> {
            txId.clear();
            txName.clear();
            txQuiz.clear();
            txA1.clear();
            txA2.clear();
            txA3.clear();
            txExam.clear();
            txResult.clear();
            txGrade.clear();

        });

        btUpdate.setOnAction(e -> {
            if (isIdValid(txId.getText())) {
                String id = txId.getText();
                String name = checkString(txName.getText(), txName);
                String quiz = formatNumberString(txQuiz.getText(), txQuiz);
                String a1 = formatNumberString(txA1.getText(), txA1);
                String a2 = formatNumberString(txA2.getText(), txA2);
                String a3 = formatNumberString(txA3.getText(), txA3);
                String exam = formatNumberString(txExam.getText(), txExam);
                txResult.setText("n/a");
                txGrade.setText("n/a");
                updateRecord(id, name, quiz, a1, a2, a3, exam);
            } else {
                lbStatus.setText("Request not sent.");
            }
        });

        btInsert.setOnAction(e -> {
            if (isIdValid(txId.getText())) {
                String id = txId.getText();
                String name = checkString(txName.getText(), txName);
                String quiz = formatNumberString(txQuiz.getText(), txQuiz);
                String a1 = formatNumberString(txA1.getText(), txA1);
                String a2 = formatNumberString(txA2.getText(), txA2);
                String a3 = formatNumberString(txA3.getText(), txA3);
                String exam = formatNumberString(txExam.getText(), txExam);
                txResult.setText("n/a");
                txGrade.setText("n/a");
                insertRecord(id, name, quiz, a1, a2, a3, exam);
            } else {
                lbStatus.setText("Request not sent.");
            }
        });

        btShowAll.setOnAction(e -> {
            showAllRecord();
        });

        //TableView setup
        tableView.setEditable(false);
        tableView.setPadding(new Insets(5, 5, 5, 5));
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
        nameCol.setMinWidth(250);
        nameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        quizCol.setMinWidth(50);
        quizCol.setCellValueFactory(new PropertyValueFactory<Student,String>("quiz"));
        a1Col.setMinWidth(50);
        a1Col.setCellValueFactory(new PropertyValueFactory<Student,String>("a1"));
        a2Col.setMinWidth(50);
        a2Col.setCellValueFactory(new PropertyValueFactory<Student,String>("a2"));
        a3Col.setMinWidth(50);
        a3Col.setCellValueFactory(new PropertyValueFactory<Student,String>("a3"));
        examCol.setMinWidth(50);
        examCol.setCellValueFactory(new PropertyValueFactory<Student,String>("exam"));
        resultsCol.setMinWidth(50);
        resultsCol.setCellValueFactory(new PropertyValueFactory<Student,String>("result"));
        gradeCol.setMinWidth(50);
        gradeCol.setCellValueFactory(new PropertyValueFactory<Student,String>("grade"));
        tableView.getColumns().addAll(idCol,nameCol,quizCol,a1Col,a2Col,a3Col,examCol,resultsCol,gradeCol);


        //layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(gridPane);
        borderPane.setCenter(lbStatus);
        borderPane.setBottom(tableView);

        //create scene and show
        Scene scene = new Scene(borderPane, paneWidth, paneHeight);
        primaryStage.setTitle("Grade Processing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * loads the driver required for sql server connections
     */
    private void loadMysqlDriver(){
        try {
            Class.forName(driver);
            System.out.println("Driver loaded");
            lbStatus.setText("Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not loaded");
            lbStatus.setText("Driver not loaded");
            e.printStackTrace();
        }
    }

    /**
     * connects to database
     */
    private void connectToMysql(){
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection closed");
                connection = null;
                System.out.println("Connection cleared for new connection");
            }
            System.out.println("Attempting connection");
            connection = DriverManager.getConnection(dbUrl, user, pass);
            System.out.println("Database connection established");
        } catch (SQLException e) {
            System.out.println("Database connection failed to establish");
            e.printStackTrace();
        }
    }

    /**
     * populates the observation list to be displayed in the tableView
     * @param resultSet
     */
    private void populateStudentObsList(ResultSet resultSet){
        try {
            while (resultSet.next()) {
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String quiz = resultSet.getString(3);
                String a1 = resultSet.getString(4);
                String a2 = resultSet.getString(5);
                String a3 = resultSet.getString(6);
                String exam = resultSet.getString(7);
                String result = resultSet.getString(8);
                String grade = resultSet.getString(9);
                Student student = new Student(id, name, quiz, a1, a2, a3, exam, result, grade);
                //ignore duplicates
                boolean containsStudent = false;
                for (Student s : studentObsList){
                    if (s.getId() == student.getId()){
                        containsStudent = true;
                        break;
                    }
                }
                if (!containsStudent){
                    studentObsList.add(student);
                }
            }
        } catch (SQLException e) {
            System.out.println("Main.populateStudentObsList(): An error occurred.");
            e.printStackTrace();
        } finally {
            tableView.getItems().clear();
            tableView.getItems().addAll(studentObsList);
            System.out.println("Table updated.");
            tableView.refresh();
        }
    }


    /**
     * closes the connection and nulls the connection variable
     */
    private void closeConnection(){
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Database connection successfully closed.");
                connection = null;
            }
        } catch (SQLException e) {
            System.out.println("Main.closeConnection(): An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * sends a query using the sql statement string
     * @param sql
     */
    private void sendQuery (String sql){
        try {
            connectToMysql();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            populateStudentObsList(resultSet);
            lbStatus.setText("Query executed successfully");
        } catch (SQLException e){
            lbStatus.setText("SQL search error");
        } finally {
            closeConnection();
        }
    }

    /**
     * sends an update using the sql statement string
     * @param sql
     */
    private void sendUpdate (String sql){
        try {
            connectToMysql();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            lbStatus.setText("Update executed successfully");
        } catch (SQLException e){
            lbStatus.setText("SQL search error");
        } finally {
            closeConnection();
        }
    }


    /**
     * creates a table in the database if one does not already exist
     */
    private void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS Java2 "
                + "(ID integer NOT NULL,"
                + "	StudentName text NOT NULL,"
                + "	Quiz float,"
                + "	A1 float,"
                + "	A2 float,"
                + "	A3 float,"
                + "	Exam float,"
                + "	Result float,"
                + "	Grade text,"
                + " PRIMARY KEY (ID));";
        sendUpdate(sql);
        System.out.println("Table was created if one did not already exist.");
        lbStatus.setText("Table was created if one did not already exist.");
    }

    /**
     * inserts a record into the table
     * @param id
     * @param sName
     * @param quiz
     * @param a1
     * @param a2
     * @param a3
     * @param exam
     */
    private void insertRecord
            (String id, String sName, String quiz, String a1, String a2,
             String a3, String exam){
        if (!sName.contains("")) {
            String sql = "INSERT INTO Java2 (ID, StudentName) "
                    + "VALUES ('" + id + "', '" + sName + "')";
            sendUpdate(sql);
            updateRecord(id, "", quiz, a1, a2, a3, exam);
        } else {
            lbStatus.setText("Request not sent.");
        }
    }

    /**
     * searches filled fields
     * @param id
     * @param sName
     * @param quiz
     * @param a1
     * @param a2
     * @param a3
     * @param exam
     * @param result
     * @param grade
     */
    private void searchRecord(String id, String sName, String quiz, String a1, String a2, String a3, String exam, String result, String grade){
        studentObsList.removeAll(studentObsList);
        if (!id.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE ID = " + id;
            sendQuery(sql);
        }
        if (!sName.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE StudentName = '" + sName + "'";
            sendQuery(sql);
        }
        if (!quiz.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE Quiz = " + quiz;
            sendQuery(sql);
        }
        if (!a1.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE A1 = " + a1;
            sendQuery(sql);
        }
        if (!a2.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE A2 = " + a2;
            sendQuery(sql);
        }
        if (!a3.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE A3 = " + a3;
            sendQuery(sql);
        }
        if (!exam.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE Exam = " + exam;
            sendQuery(sql);
        }
        if (!result.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE Result = " + result;
            sendQuery(sql);
        }
        if (!grade.contentEquals("")){
            String sql = "SELECT * FROM Java2 WHERE Grade = '" + grade + "'";
            sendQuery(sql);
        }
    }

    /**
     * shows all entries in the table
     * @return resultSet
     */
    private void showAllRecord(){
        studentObsList.removeAll(studentObsList);
        String sql = "SELECT * FROM Java2";
        sendQuery(sql);
    }

    /**
     * updates table with filled fields
     * @param id
     * @param sName
     * @param quiz
     * @param a1
     * @param a2
     * @param a3
     * @param exam
     */
    private void updateRecord
            (String id, String sName, String quiz, String a1, String a2, String a3, String exam) {
        studentObsList.removeAll(studentObsList);
        int count = 0;
        float result = 0f;
        if (sName != "") {
            String sql = "UPDATE Java2 SET StudentName = '" + sName + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
        }
        if (quiz != "") {
            String sql = "UPDATE Java2 SET Quiz = '" + quiz + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
            count += 1;
        }
        if (a1 != "") {
            String sql = "UPDATE Java2 SET A1 = '" + a1 + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
            count += 1;
        }
        if (a2 != "") {
            String sql = "UPDATE Java2 SET A2 = '" + a2 + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
            count += 1;
        }
        if (a3 != "") {
            String sql = "UPDATE Java2 SET A3 = '" + a3 + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
            count += 1;
        }
        if (exam != "") {
            String sql = "UPDATE Java2 SET Exam = '" + exam + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
            count += 1;
        }
        if (count == 5) {
            //update Record result
            result = calcResults(Float.valueOf(quiz), Float.valueOf(a1),
                    Float.valueOf(a2), Float.valueOf(a3), Float.valueOf(exam));
            String sql = "UPDATE Java2 SET Result = '" + result + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);

            //update Record grade
            String grade = calcGrade(result);
            sql = "UPDATE Java2 SET Grade = '" + grade + "' WHERE ID = '" + id + "'";
            sendUpdate(sql);
        }
        searchRecord(id, "", "", "", "", "", "", "", "");
    }

    /**
     * calulates the result of the assessments
     * @param quiz
     * @param a1
     * @param a2
     * @param a3
     * @param exam
     * @return result
     */
    public static float calcResults(float quiz, float a1, float a2, float a3, float exam){
    float result = (quiz*0.05f)+(a1*0.15f)+(a2*0.2f)+(a3*0.1f)+(exam*.5f);
    return result;
    }

    /**
     * calculates the grade based on the result
     * @param result
     * @return grade
     */
    public static String calcGrade(float result){
        String grade = "FL";
        if(result >= 85f){
            grade = "HD";
        }else if(result >= 75f){
            grade = "DI";
        }else if(result >= 65f){
            grade = "CR";
        }else if(result >= 50f){
            grade = "PS";
        }
        return grade;
    }

    /**
     * checks if string is numeric
     * @param id
     */
    private Boolean isIdValid(String id) {
        boolean isValid = false;
        int idInt;
        for (char c : id.toCharArray()) {
            if (Character.isDigit(c)) {
                isValid = true;
            } else {
                isValid = false;
                txId.setText("Incorrect Value");
                break;
            }
        }
        if (!id.contentEquals("")) {
            if (isValid) {
                idInt = Integer.valueOf(id);
                if (idInt >= 10000000) {
                    isValid = true;
                }
            } else {
                isValid = false;
                txId.setText("Incorrect Value");
            }
        } else {
            isValid = false;
        }
        return isValid;
    }

    /**
     * takes a String and returns a string of digits that are rounded and formatted correctly
     * if the string is incorrect value or empty it returns an empty string
     * @param str
     * @param txFld
     * @return result
     */
    private String formatNumberString(String str, TextField txFld){
        boolean isCorrect = false;
        String result = "";

        Pattern pattern = Pattern.compile("(\\d+(?:\\.\\d+)?)"); //digits and decimals followed by digits
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()){
            DecimalFormat decimalFormat = new DecimalFormat(".##");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);
            float temp = Float.valueOf(str);
            result = String.valueOf(decimalFormat.format(temp));
            txFld.setText(String.valueOf(result)); //display rounding in textfield
        } else if (!str.contentEquals("")){
            txFld.setText("Incorrect Value");
        }
        return result;
    }

    /**
     * takes a String and if the string is incorrect value or empty it returns an empty string
     * @param str
     * @param txFld
     * @return
     */
    private String checkString(String str, TextField txFld){
        String result = "";
        Pattern pattern = Pattern.compile("^[ A-Za-z]+$"); //letters
        Matcher matcher = pattern.matcher(str);
        if (str.contentEquals("")){
            //do nothing
        } else if(matcher.matches()){
            result = str;
        } else {
            txFld.setText("Incorrect Value");
            result = "";
        }
        return result;
    }
}
