package controllers


import javax.inject._
import play.api._
import play.api.mvc._
import models.TaskListInMemoryModel

@Singleton
class TaskList1 @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

    def validate(userName: String, password:String) = Action {
        Ok(s"Hey! $userName, you're Logged in with ${password.flatMap(element => "*")}")
    }

    def validatePost1 = Action {
        request => {
            val postVal = request.body.asFormUrlEncoded;
            postVal.map(args => {
                val userName = args("userName").head;
                val password = args("password").head;

                if(TaskListInMemoryModel.validateUser(userName,password))
                Redirect(routes.TaskList1.taskList).withSession("userName" -> userName)
                else
                Redirect(routes.TaskList1.login1)
            })
        }.getOrElse(Redirect(routes.TaskList1.login1))
    }

    def createUser1 = Action {
        request => {
            val postVal = request.body.asFormUrlEncoded;
            postVal.map(args => {
                val userName = args("userName").head;
                val password = args("password").head;

                if(TaskListInMemoryModel.createUser(userName,password))
                Redirect(routes.TaskList1.taskList).withSession("userName" -> userName)
                else
                Redirect(routes.TaskList1.login1)
            })
        }.getOrElse(Redirect(routes.TaskList1.login1))
    }

    def login1 = Action {
        Ok(views.html.login1())
    }

    def logout1 = Action {
        Redirect(routes.TaskList1.login1).withNewSession
    }

    def taskList = Action { request => {
        request.session.get("userName").map { userName =>
            Ok(views.html.taskList1(TaskListInMemoryModel.getUserTasks(userName)))
        }.getOrElse {   
            Redirect(routes.TaskList1.login1)
        }
    }
    }

    //TODO: 1. Redirect the User to their Desired Task. 2. Logout the User.
  
}
