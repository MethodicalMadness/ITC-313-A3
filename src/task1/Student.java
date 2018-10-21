package task1;

import javafx.beans.property.SimpleStringProperty;

public class Student {

    private final SimpleStringProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty quiz;
    private final SimpleStringProperty a1;
    private final SimpleStringProperty a2;
    private final SimpleStringProperty a3;
    private final SimpleStringProperty exam;
    private final SimpleStringProperty result;
    private final SimpleStringProperty grade;

    public Student(String id, String name, String quiz,
                   String a1, String a2, String a3,
                   String exam, String result, String grade) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quiz = new SimpleStringProperty(quiz);
        this.a1 = new SimpleStringProperty(a1);
        this.a2 = new SimpleStringProperty(a2);
        this.a3 = new SimpleStringProperty(a3);
        this.exam = new SimpleStringProperty(exam);
        this.result = new SimpleStringProperty(result);
        this.grade = new SimpleStringProperty(grade);
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

    public String getQuiz() {
        return quiz.get();
    }

    public String getA1() {
        return a1.get();
    }

    public String getA2() {
        return a2.get();
    }

    public String getA3() {
        return a3.get();
    }

    public String getExam() {
        return exam.get();
    }

    public String getResult() {
        return result.get();
    }

    public String getGrade() {
        return grade.get();
    }
}

