package controllers

package controllers

import org.scalatestplus.play._
import models.TaskListInMemoryModel

class TaskListInMemoryTests extends PlaySpec {
    "TaskListInMemoryModel" must {
        "do valid login for default user" in {
            TaskListInMemoryModel.validateUser("user1", "password1") mustBe true
        }
        "reject login with wrong username" in {
            TaskListInMemoryModel.validateUser("user2", "password1") mustBe false
        }
        "get correct tasks for user1" in {
            TaskListInMemoryModel.getUserTasks("user1") mustBe List("Eat","Sleep","Code")
        }
        "create a new user without any tasks" in {
            TaskListInMemoryModel.createUser("shivam", "password") mustBe true
            TaskListInMemoryModel.getUserTasks("shivam") mustBe Nil
        }
        "not create a duplicate user" in {
            TaskListInMemoryModel.createUser("user1", "password") mustBe false
        }
        "add a task for user1" in {
            TaskListInMemoryModel.addUserTask("user1", "Eat")
            TaskListInMemoryModel.getUserTasks("user1") must contain ("Eat")
        }
    }
}