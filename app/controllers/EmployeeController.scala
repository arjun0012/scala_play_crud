package controllers

import com.google.inject.Inject
import models.{Employee, EmployeeForm}
import services.EmployeeService
import play.api.mvc._
import play.api.libs.json._
import play.mvc.Results._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class EmployeeController @Inject()(cc:ControllerComponents, employeeService: EmployeeService) extends AbstractController(cc) {

  implicit val employeeFormat: OFormat[Employee] = Json.format[Employee]

  def getAll(uid:Int): Action[AnyContent] =Action.async {
    implicit request:Request[AnyContent]=>
      employeeService.listAllEmp(uid) map {employeeList=>Ok(Json.toJson(employeeList))}
  }

  def getById(uid:Int,empId:Int)=Action.async {
    implicit request: Request[AnyContent] =>
      employeeService.getEmp(uid,empId) map { case Some(emp) => Ok(Json.toJson(emp))
      case None=>Unauthorized(Json.toJson(Map("error"->"not allowed")))}
  }

  def add()=Action.async{
    implicit request: Request[AnyContent] =>
      EmployeeForm.form.bindFromRequest().fold(
        errorForm=>{
          errorForm.errors.foreach(println)
          Future.successful(BadRequest("Error!"))
        },
        data=>{
          val newEmployee = Employee(0,data.empName,data.empCompany,data.empSalary,data.empDept)
          employeeService.addEmp(newEmployee).map(emp=>Ok(Json.toJson(emp)))
        }
      )
  }

  def update(uid:Int,empId:Int)=Action.async{
    implicit request: Request[AnyContent] =>
      EmployeeForm.form.bindFromRequest().fold(
        errorForm => {
          errorForm.errors.foreach(println)
          Future.successful(BadRequest("Error!"))
        },
        data => {
          val employee = Employee(empId, data.empName, data.empCompany, data.empSalary, data.empDept)
          employeeService.updateEmp(uid,employee).map{ case 0=> Unauthorized(Json.toJson(Map("error"->"not allowed")))
            case -1=> NotFound(Json.toJson(Map("error"->"employee does not exist")))
            case res => Redirect(routes.EmployeeController.getAll(uid))}
        }
      )
  }

  def delete(uid:Int,empId:Int)=Action.async{
    implicit request: Request[AnyContent] =>
      employeeService.deleteEmp(uid,empId) map{case 0=>Unauthorized(Json.toJson(Map("error"->"not allowed")))
        case -1=> NotFound(Json.toJson(Map("error"->"employee does not exist")))
        case res=> Redirect(routes.EmployeeController.getAll(uid))
      }
  }

}
