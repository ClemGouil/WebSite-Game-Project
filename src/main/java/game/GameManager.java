package game;

import java.util.List;

import game.models.User;
import game.models.VOCredentials;

public interface GameManager {

    public int size();
    public User addUser(String  mail, String username, String password);
    public User addUser(User u);
    public User getUser (int id);

    public User loginUser(VOCredentials credenciales);

    //public VOCredentials getCredentials(User u);
    public List<User> getUsers();
    public User deleteUser(String mail, String password);
    public User updateUser(User u);
    public User authentification(String mail, String password);
}
