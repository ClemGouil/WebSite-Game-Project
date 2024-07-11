package game.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import game.GameManager;
import game.GameManagerImpl;
import game.models.Question;
import game.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/", description = "Endpoint to User Service")
@Path("/")

public class GameService {
    private final GameManager gm;
    final static Logger logger = Logger.getLogger(GameService.class);

    public GameService() {
        this.gm = GameManagerImpl.getInstance();
        if (gm.sizeUsers()==0) {
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////// GET ////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @GET
    @ApiOperation(value = "get all Users", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class, responseContainer="List"),
    })
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {

        List<User> users = this.gm.getUsers();

        GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all Questions", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Question.class, responseContainer="List"),
    })
    @Path("/questions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions() {

        List<Question> questions = this.gm.getQuestions();

        GenericEntity<List<Question>> entity = new GenericEntity<List<Question>>(questions) {};
        return Response.status(201).entity(entity).build();
    }

    // @GET
    // @ApiOperation(value = "get all Answers", notes = "asdasd")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 201, message = "Successful", response = Answer.class, responseContainer="List"),
    // })
    // @Path("/answers")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getAnswers() {

    //     List<Answer> answers = this.gm.getAnswers();

    //     GenericEntity<List<Answer>> entity = new GenericEntity<List<Answer>>(answers) {};
    //     return Response.status(201).entity(entity).build();
    // }

    @GET
    @ApiOperation(value = "get an User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id) {
        User u = this.gm.getUser(id);

        if (u == null) {return Response.status(404).build();}
        else  {return Response.status(201).entity(u).build();}
    }

    @GET
    @ApiOperation(value = "Authenticate User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users/{mail}&{password}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authentificate(@PathParam("mail") String mail, @PathParam("password") String password) {

        User user = this.gm.authentification(mail, password);

        if (user == null) {return Response.status(404).build();}
        else {return Response.status(201).entity(user).build();}
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////// POST /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @POST
    @ApiOperation(value = "login", notes = "Realitzar el login")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/users/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(User user) {
        System.out.println("-----LOGIN-----");
        System.out.println("Mail: "+ user.getMail());
        User u = this.gm.loginUser(user);
        if (u==null)
            return Response.status(500).build();
        else
            return Response.status(201).entity(u).build();
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 400, message = "User already exist for this mail")

    })
    @Path("/users/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newUser(User u) {

        User t = this.gm.addUser(u);

        if (t == null) {return Response.status(400).build();}
        else {return Response.status(201).entity(u).build();}
    }

    @POST
    @ApiOperation(value = "create a new Question", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Question.class),
            @ApiResponse(code = 400, message = "Question already exist")

    })
    @Path("/questions/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newQuestion(Question q) {

        logger.info("ICI");
        logger.info("q" + q);
        Question question = this.gm.addQuestion(q);

        if (question == null) {return Response.status(400).build();}
        else {return Response.status(201).entity(question).build();}
    }

    // @POST
    // @ApiOperation(value = "create a new Answer", notes = "asdasd")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 201, message = "Successful", response = Answer.class),
    //         @ApiResponse(code = 400, message = "Answer already exists or invalid input")
    // })
    // @Path("/answers/create")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response newAnswer(Answer a) {
    //     try {
    //         // Validate input
    //         if (a == null || a.getid_user() == 0 || a.getid_question() == 0 || a.getContent() == null || a.getDate() == null) { // Replace getSomeRequiredField with actual required field check
    //             return Response.status(400).entity("Invalid input").build();
    //         }

    //         Answer answer = this.gm.addAnswer(a);

    //         if (answer == null) {
    //             return Response.status(400).entity("Answer already exists").build();
    //         } else {
    //             return Response.status(201).entity(answer).build();
    //         }
    //     } catch (Exception e) {
    //         // Log the exception (this could be to a file, console, etc.)
    //         e.printStackTrace();
    //         // Return a generic error response
    //         return Response.status(500).entity("Internal Server Error").build();
    //     }
    // }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////// PUT /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @PUT
    @ApiOperation(value = "update an User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful",  response= User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(User u) {

        User user = this.gm.updateUser(u);

        if (user == null) {return Response.status(404).build();}
        else {return Response.status(201).entity(u).build();}
    }

    @PUT
    @ApiOperation(value = "update a Question", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful",  response= Question.class),
            @ApiResponse(code = 404, message = "Question not found")
    })
    @Path("/questions")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateQuestion(Question q) {

        logger.info("ICI Update Question");
        Question question = this.gm.updateQuestion(q);

        if (question== null) {return Response.status(404).build();}
        else {return Response.status(201).entity(q).build();}
    }

    // @PUT
    // @ApiOperation(value = "update a Answer", notes = "asdasd")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 201, message = "Successful", response = Answer.class),
    //         @ApiResponse(code = 404, message = "Answer not found")
    // })
    // @Path("/answers")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response updateAnswer(Answer a) {

    //     Answer answer = this.gm.updateAnswer(a);

    //     if (answer == null) {
    //         return Response.status(404).build();
    //     } else {
    //         return Response.status(201).entity(answer).build();
    //     }
    // }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////// DELETE /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @DELETE
    @ApiOperation(value = "delete an User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/users/delete/{mail}&{password}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("mail") String mail, @PathParam("password") String password) {


        User t = this.gm.deleteUser(mail, password);

        if (t == null) {return Response.status(404).build();}
        else {return Response.status(201).entity(t).build();}
    }

    @DELETE
    @ApiOperation(value = "delete a Question", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= User.class),
            @ApiResponse(code = 404, message = "User not found")
    })
    @Path("/questions/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteQuestion(Question q) {

        logger.info("ICI dELETE Question");
        Question question = this.gm.deleteQuestion(q);

        if (question == null) {return Response.status(404).build();}
        else {return Response.status(201).entity(question).build();}
    }

    // @DELETE
    // @ApiOperation(value = "delete a Answer", notes = "asdasd")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 201, message = "Successful", response= Answer.class),
    //         @ApiResponse(code = 404, message = "Answer not found")
    // })
    // @Path("/answers/delete")
    // @Consumes(MediaType.APPLICATION_JSON)
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response deleteAnswer(Answer a) {

    //     Answer answer = this.gm.deleteAnswer(a);

    //     if (answer == null) {return Response.status(404).build();}
    //     else {return Response.status(201).entity(answer).build();}
    // }

}
