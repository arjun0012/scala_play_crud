package services

import com.google.inject.Inject
import models.{Employee, EmployeeList}

import java.util.concurrent.TimeUnit
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, duration}

class EmployeeService @Inject()(employeeList: EmployeeList) {
  def addEmp(emp:Employee): Future[Employee] ={
    employeeList.add(emp)
  }


  def deleteEmp(uid:Int,empId:Int):Future[Int]={
    val company1 = employeeList.get(uid) map {
      case Some(value) => value.empCompany
      case None => ""
    }
    employeeList.get(empId) flatMap {
      case Some(value) => if (Await.result(company1, duration.Duration(5, TimeUnit.SECONDS)) == value.empCompany) employeeList.delete(empId)
      else Future(0)
      case None => Future(-1)
    }
  }


  def updateEmp(uid:Int,emp:Employee):Future[Int]={
    val company1 = employeeList.get(uid) map {
      case Some(value) => value.empCompany
      case None => ""
    }
    employeeList.get(emp.empId) flatMap  {
      case Some(value) => if(Await.result(company1,duration.Duration(5,TimeUnit.SECONDS))==value.empCompany) employeeList.update(emp)
      else Future(0)
      case None => Future(-1)
    }

  }


  def getEmp(uid:Int,empId:Int):Future[Option[Employee]]={
    val company = employeeList.get(uid) map {
      case Some(value) => value.empCompany
      case None => ""
    }
    employeeList.get(empId) map {
      case Some(value) =>
        if (Await.result(company, duration.Duration(5, TimeUnit.SECONDS)) == value.empCompany) Some(value)
        else None
      case None => None
    }
  }


  def listAllEmp(uid:Int):Future[Seq[Employee]]= {
    if (uid == 0) employeeList.listAll
    else {
      val company = employeeList.get(uid) map {
        case Some(value) => value.empCompany
        case None => ""
      }
      employeeList.listAll map { empl => empl.filter(_.empCompany == Await.result(company,duration.Duration(5,TimeUnit.SECONDS))) }
    }
  }
}
