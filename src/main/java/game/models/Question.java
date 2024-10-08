package game.models;

public class Question {
    private String title;
    private int id_user;
    private String date;
    private String content;

    public Question() {}
    public Question(int id_user, String date, String title, String content) {
        this.id_user = id_user;
        this.date = date;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", id_user=" + id_user +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
