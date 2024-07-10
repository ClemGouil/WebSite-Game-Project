package game;

import game.models.Item;
import game.models.Level;
import game.models.Question;
import game.models.User;
import game.util.QueryHelper;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryHelperTest {


    @Test
    public void testQuerySELECTID1() {
       // Assert.assertEquals("SELECT ID FROM User WHERE mail=?",
         //       QueryHelper.createQuerySELECTID(new User("marin@hotmail.com", "mdolle", "12a")));
    }

    @Test
    public void testQuerySELECTID2() {
        //Assert.assertEquals("SELECT ID FROM Item WHERE name=?",
                //QueryHelper.createQuerySELECTID(new Item("sword", "cut your head", 10, 20, 90, "image.png")));
        System.out.println(QueryHelper.createQuerySELECTbyID(new Level()));
    }

    @Test
    public void testQueryUPDATE() {
       System.out.println(QueryHelper.createQueryUPDATE(new User()));
    }

    @Test
    public void testQueryINSERT() {
        // System.out.println(QueryHelper.createQueryUPDATE(new Item("sword", "cut your head", 10, 20, 90, "image.png")));
        System.out.println(QueryHelper.createQueryINSERT(new Question()));
    }

    @Test
    public void testQuerySELECT() {
        // System.out.println(QueryHelper.createQueryUPDATE(new Item("sword", "cut your head", 10, 20, 90, "image.png")));
        List<String> args = new ArrayList<>();
        args.add("sender");
        args.add("date");
        System.out.println(QueryHelper.createQuerySELECT(Question.class, args));
    }

    @Test
    public void testQueryDELETE() {
        System.out.println(QueryHelper.createQueryDELETE(new User()));
    }
}
