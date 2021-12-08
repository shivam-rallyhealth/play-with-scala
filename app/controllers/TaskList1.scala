package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.TaskListInMemoryModel


case class LoginData(userName: String, password: String)    

@Singleton
class TaskList1 @Inject()(controllerComponents: MessagesControllerComponents) extends MessagesAbstractController(controllerComponents) {


    val loginForm = Form(mapping(
        "userName" -> text(3,10),
        "password" -> text(3,10)
    )(LoginData.apply)(LoginData.unapply))

    def validate(userName: String, password:String) = Action {
        Ok(s"Hey! $userName, you're Logged in with ${password.flatMap(element => "*")}")
    }

    def validatePost1 = Action {
        implicit request => {
            val postVal = request.body.asFormUrlEncoded;
            postVal.map(args => {
                val userName = args("userName").head;
                val password = args("password").head;

                if(TaskListInMemoryModel.validateUser(userName,password))
                Redirect(routes.TaskList1.taskList).withSession("userName" -> userName)
                else
                Redirect(routes.TaskList1.login1).flashing("error" -> "Wrong UserName or Password")
            }).getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid Login Attempt"))
        }
    }

    def validateLoginForm = Action {
        implicit request => {
            Ok("")
        }
    }

    def createUser1 = Action {
        implicit request => {
            val postVal = request.body.asFormUrlEncoded;
            postVal.map(args => {
                val userName = args("userName").head;
                val password = args("password").head;

                if(TaskListInMemoryModel.createUser(userName,password))
                Redirect(routes.TaskList1.taskList).withSession("userName" -> userName)
                else
                Redirect(routes.TaskList1.login1).flashing("error" -> "createUser1: Invalid UserName or Password")
            }
            ).getOrElse(Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid Login Attempt"))
        }
    }

    def login1 = Action {implicit request =>
        Ok(views.html.login1(loginForm))
    }

    def logout1 = Action {
        Redirect(routes.TaskList1.login1).withNewSession
    }

    def taskList = Action { implicit request => {
        request.session.get("userName").map { userName =>
            Ok(views.html.taskList1(TaskListInMemoryModel.getUserTasks(userName)))
        }.getOrElse {   
            Redirect(routes.TaskList1.login1).flashing("error" -> "You must be Logged in First!")
        }
    }
    }

    def addTask1 = Action { implicit request =>
        val postVal = request.body.asFormUrlEncoded;
        val username = request.session.get("userName");

        username.map(name => postVal.map(args => {
            val newTask = args("task").head;
            TaskListInMemoryModel.addUserTask(name,newTask)
            Redirect(routes.TaskList1.taskList) 
            
        }).getOrElse(Redirect(routes.TaskList1.taskList))).getOrElse(
        Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid UserName or Password"))
        
    };

    def deleteTask = Action { implicit request =>
        val postVal = request.body.asFormUrlEncoded;
        val username = request.session.get("userName");

        username.map(name => postVal.map(args => {
            val index = args("index").head.toInt;
            TaskListInMemoryModel.deleteTask(name,index);
            Redirect(routes.TaskList1.taskList) 
            
        }).getOrElse(Redirect(routes.TaskList1.taskList))).getOrElse(
        Redirect(routes.TaskList1.login1).flashing("error" -> "Invalid UserName or Password"))
        
    };
    //TODO: 1. Redirect the User to their Desired Task. 2. Logout the User.
  
}
