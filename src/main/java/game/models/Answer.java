package game.models;

public class Answer {
    private int id_question;
    private int id_user;
    private String date;
    private String content;

    public Answer() {}

    // Constructeur
    public Answer(int id_question, int id_user, String date, String content) {
        this.id_question = id_question;
        this.id_user = id_user;
        this.date = date;
        this.content = content;
    }

    // Getters et Setters
    public int getId_question() {
        return id_question;
    }

    public void setId_question(Integer id_question) {
        this.id_question = id_question;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Answer{" +
                "id_question=" + id_question +
                ", id_user=" + id_user +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}