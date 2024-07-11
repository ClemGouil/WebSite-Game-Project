package game;

import java.util.List;

import game.models.Question;
import game.models.User;


public interface GameManager {

    // User Part 

    public int sizeUsers();
    public User addUser(String  mail, String username, String password);
    public User addUser(User u);
    public User getUser (int id);
    public List<User> getUsers();
    public User updateUser(User u);
    public User deleteUser(String mail, String password);
    
    public User loginUser(User user);
    public User authentification(String mail, String password);

    // Question Part 

    public Question addQuestion(Question q);
    public List<Question> getQuestions();
    public Question updateQuestion(Question q);
    public Question deleteQuestion(Question q);

    // Answer Part

    // public Answer addAnswer(Answer q);
    // public List<Answer> getAnswersOfAQuestion(int id_question);
    // public List<Answer> getAnswers();
    // public Answer updateAnswer(Answer q);
    // public Answer deleteAnswer(Answer q);
}
