package models

import collection.mutable

object TaskListInMemoryModel {
    
    private val users = mutable.Map[String,String]("user1" -> "password1", "user2" -> "password2");
    private val tasks = mutable.Map[String,List[String]]("user1"->List("Eat","Sleep","Code"));  

    def validateUser(username:String,password:String): Boolean = users.get(username).map(_==password).getOrElse(false)

    def getUserTasks(username:String): List[String] = tasks.get(username).getOrElse(Nil);

    def createUser(username:String,password:String):Boolean = {
        if(users.contains(username)) false
        else {
            users += (username -> password)
            true
        }
    }
}