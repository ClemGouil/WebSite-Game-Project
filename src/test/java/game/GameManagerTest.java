package game;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import game.models.User;
import game.models.VOCredentials;

public class GameManagerTest {

    //////////////////////////////////////////////// Users ////////////////////////////////////////////////

    @Test
    public void addUserTest() {
        GameManager gm = GameManagerImpl.getInstance();
        User u = new User("mama@gmail.com", "oool", "123");
        User user = gm.addUser(u);
        User sameUser1 = gm.addUser(u);
        User sameUser2 = gm.addUser(new User("mama@gmail.com", "zdhzked", "gejhdu"));
        Assert.assertEquals(user, u);
        Assert.assertEquals(sameUser1, null);
        Assert.assertEquals(sameUser2, null);
    }

    @Test
    public void getUserByIDTest() {
        GameManager gm = GameManagerImpl.getInstance();
        User user1;
        User user2;
        user1 = gm.getUser(47);
        user2 = gm.getUser(10);
        System.out.println(user1);
        Assert.assertEquals(user2, null);
    }

    @Test
    public void authentificationTest() {
        GameManager gm = GameManagerImpl.getInstance();
        VOCredentials v0 = new VOCredentials("mama@gmail.com", "oool");
        VOCredentials v1 = new VOCredentials("mama@gmail.com", "zedfghj");
        VOCredentials v2 = new VOCredentials("ooausi@gmail.com", "azertyui");
        User u = gm.loginUser(v0);
        User f1 = gm.loginUser(v1);
        User f2 = gm.loginUser(v2);
        Assert.assertEquals(f1, null);
        Assert.assertEquals(f2, null);
        Assert.assertEquals(u.getMail(), "mama@gmail.com");
        Assert.assertEquals(u.getPassword(), "oool");
        Assert.assertEquals(u.getUsername(), "123");
    }

    @Test
    public void findAllTest() {
        GameManager gm = GameManagerImpl.getInstance();
        List<User> users;
        users = gm.getUsers();
        System.out.println(users);
    }

    @Test
    public void upadateTest() {
        GameManager gm = GameManagerImpl.getInstance();
        User u = new User("clement@gmail.com", "1234", "cacaboudin");
        User u1 = new User("clertyu", "1234", "cacaboudin");
        User user = gm.updateUser(u);
        User user1 = gm.updateUser(u1);
        Assert.assertEquals(user, u);
        Assert.assertEquals(user1, null);
    }


    //////////////////////////////////////////////// Items ////////////////////////////////////////////////
}
