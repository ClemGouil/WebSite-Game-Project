package game;

import java.time.LocalDate;
import java.util.List;

import org.apache.log4j.Logger;

import game.models.Message;
import game.models.Question;
import game.models.User;
import game.util.PasswordSecurity;

public class GameManagerImpl implements GameManager{
    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManagerImpl.class);

    private GameManagerImpl() {
    }

    public static GameManager getInstance() {
        if (instance==null) instance = new GameManagerImpl();
        return instance;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////// USERS ////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int sizeUsers() {
        int ret = this.getUsers().size();
        logger.info("size " + ret);

        return ret;
    }

    public User addUser(User u) {

        LocalDate date = LocalDate.now();
        Message info = null;
        logger.info("new user : " + u + " should be add");
        Session session = null;
        User user = null;
        int userID = 0;

        try {
            session = FactorySession.openSession();
            userID = session.getID(u);

            if (userID == 0) {
                user = u;
                user.setPassword(PasswordSecurity.encrypt(user.getPassword()));
                session.save(user);
                userID = session.getID(user);             
                logger.info("new user " + u + " added");

            } else {
                logger.warn("user is already existing for this mail");
            }
        } catch (Exception e) {
        } finally {
            session.close();
        }
        return user;
    }

    public User addUser(String mail, String username, String password) {
        return this.addUser(new User(mail, password, username));
    }

    public User getUser(int id) {

        logger.info("we want to get user associated to id " + id);

        Session session = null;
        User user = null;

        try {
            session = FactorySession.openSession();
            user = (User) session.getByID(User.class, id);

        } catch (Exception e) {
        } finally {
            session.close();
        }
        if (user != null) {
            logger.info("the user is " + user);
        } else {
            logger.info("user not found");
        }
        return user;
    }

    @Override
    public User authentification(String mail, String password) {

        Session session = null;
        User user = new User(mail, password, "Zidane");
        User u = null;
        int id = 0;

        try {
            session = FactorySession.openSession();
            id = session.getID(user);

            if (id == 0) {
                logger.info("user not found");
            } else {
                u = this.getUser(id);
                if (!(PasswordSecurity.decrypt(u.getPassword()).equals(password))) {
                    logger.warn("Password wrong");
                    return null;
                } else {
                    logger.warn("user found");
                    return u;
                }
            }
        } catch (Exception e) {
        } finally {
            session.close();
        }

        return null;
    }

    @Override
    public User loginUser(User user) {
        return this.authentification(user.getMail(), user.getPassword());
    }

    @Override
    public List<User> getUsers() {
        Session session = null;
        List<User> users = null;

        try {
            session = FactorySession.openSession();
            users = session.findAll(User.class);
            logger.info("users are : " + users);


        } catch (Exception e) {
        } finally {
            session.close();
        }
        return users;
    }

    @Override
    public User updateUser(User u) {

        Session session = null;
        LocalDate date = LocalDate.now();
        Message info = null;
        User user = null;
        User test = null;
        boolean isUpdate = false;

        try {
            session = FactorySession.openSession();
            int i = session.getID(u);
            if (i == 0) {
                logger.warn("User doesn't exist");
                return null;
            }
            test = getUser(i);
            u.setPassword(PasswordSecurity.encrypt(u.getPassword()));
            if ((test.getPassword().equals(u.getPassword())) && test.getUsername().equals(u.getUsername())) {

                logger.warn("you don't change anything");
                return null;
            }

            isUpdate = session.update(u);
            if (isUpdate) {
                user = u;
                user.setPassword(PasswordSecurity.decrypt(user.getPassword()));
                logger.info("User updated");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public User deleteUser(String mail, String password) {

        Session session = null;
        User user = null;
        int id = 0;

        try {
            session = FactorySession.openSession();
            user = this.authentification(mail, password);
            id = session.getID(user);

            if (id == 0) {
                logger.warn("user isn't exist in database");
                return null;
            } else {
                session.delete(user);
                logger.info("user : "+ user +" deleted");
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////// QUESTIONS ////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Question addQuestion(Question q) {

        // LocalDate date = LocalDate.now();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Posté le dd/MM/yyyy 'à' HH'h'mm");
        // String formattedDate = date.format(formatter);
        logger.info("new question : " + q + " should be add");
        Session session = null;
        Question question = null;

        try {
            logger.info("ICI");
            session = FactorySession.openSession();
            question = q;
            //question.setDate(formattedDate);
            session.save(question);           
            logger.info("new question " + q + " added");
        } catch (Exception e) {
            logger.error("Error adding question: ", e);
        } finally {
            session.close();
        }
        return question;
    }

    @Override
    public List<Question> getQuestions() {
        Session session = null;
        List<Question> questions = null;

        try {
            session = FactorySession.openSession();
            questions = session.findAll(Question.class);
            logger.info("questions are : " + questions);
        } catch (Exception e) {
        } finally {
            session.close();
        }
        return questions;
    }

    @Override
    public Question updateQuestion(Question q) {

        logger.info(" question : " + q + " should be update");
        Session session = null;
        // LocalDate date = LocalDate.now();
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("modifié le dd/MM/yyyy 'à' HH'h'mm");
        // String formattedDate = date.format(formatter);
        Question question = null;
        boolean isUpdate = false;

        try {
            session = FactorySession.openSession();
            isUpdate = session.update(q);
            if (isUpdate) {
                //q.setDate(formattedDate);
                question = q;
                logger.info("Question updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return question;
    }

    @Override
    public Question deleteQuestion(Question q) {

        Session session = null;
        Question question = null;
        int id = 0;
        try {
            session = FactorySession.openSession();
            question = q;
            session.delete(question);
            logger.info("question : "+ q +" deleted");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return question;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////// ANSWERS ////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    // @Override
    // public Answer addAnswer(Answer a) {

    //     LocalDate date = LocalDate.now();
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("Posté le dd/MM/yyyy 'à' HH'h'mm");
    //     String formattedDate = date.format(formatter);
    //     logger.info("new Answer : " + a + " should be add");
    //     Session session = null;
    //     Answer Answer = null;
    
    //     try {
    //         session = FactorySession.openSession();
    //         Answer = a;
    //         Answer.setDate(formattedDate);
    //         session.save(Answer);           
    //         logger.info("new Answer " + a + " added");
    //     } catch (Exception e) {
    //         logger.error("Error adding Answer: ", e);
    //     } finally {
    //         session.close();
    //     }
    //     return Answer;
    // }
    
    // @Override
    // public List<Answer> getAnswers() {
    //     Session session = null;
    //     List<Answer> Answers = null;
    
    //     try {
    //         session = FactorySession.openSession();
    //         Answers = session.findAll(Answer.class);
    //         logger.info("Answers are : " + Answers);
    //     } catch (Exception e) {
    //     } finally {
    //         session.close();
    //     }
    //     return Answers;
    // }

    // @Override
    // public List<Answer> getAnswersOfAQuestion(int id_question) {
    //     Session session = null;
    //     List<Answer> Answers = null;
    
    //     try {
    //         session = FactorySession.openSession();

    //         Answers = session.findAllFor(Answer.class, "id_Question", id_question);
    //         logger.info("Answers are : " + Answers);
    //     } catch (Exception e) {
    //     } finally {
    //         session.close();
    //     }
    //     return Answers;
    // }
    
    // @Override
    // public Answer updateAnswer(Answer a) {
    
    //     Session session = null;
    //     LocalDate date = LocalDate.now();
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("modifié le dd/MM/yyyy 'à' HH'h'mm");
    //     String formattedDate = date.format(formatter);
    //     Answer Answer = null;
    //     boolean isUpdate = false;
    
    //     try {
    //         session = FactorySession.openSession();
    //         isUpdate = session.update(a);
    //         if (isUpdate) {
    //             a.setDate(formattedDate);
    //             Answer = a;
    //             logger.info("Answer updated");
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         session.close();
    //     }
    //     return Answer;
    // }
    
    // @Override
    // public Answer deleteAnswer(Answer a) {
    
    //     Session session = null;
    //     Answer Answer = null;
    //     int id = 0;
    //     try {
    //         session = FactorySession.openSession();
    //         Answer = a;
    //         session.delete(Answer);
    //         logger.info("Answer : "+ a +" deleted");
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     } finally {
    //         session.close();
    //     }
    //     return Answer;
    // }
}
