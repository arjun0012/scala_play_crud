package models

import com.google.inject.Inject

import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick._
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._


case class Employee(empId:Int,empName:String,empCompany:String,empSalary:Int,empDept:String)

case class EmployeeFormData(empName:String,empCompany:String,empSalary:Int,empDept:String)

object EmployeeForm{
  val form = Form(
    mapping(
      "empName"-> text,
      "empCompany"-> text,
      "empSalary"-> number,
      "empDept"-> text
    )(EmployeeFormData.apply)(EmployeeFormData.unapply)
  )
}

class EmployeeTable(tag: Tag) extends Table[Employee](tag,"Employee"){
    def empId=column[Int]("empId",O.PrimaryKey,O.AutoInc)
    def empName =column[String]("empName")
    def empCompany =column[String]("empCompany")
    def empSalary =column[Int]("empSalary")
    def empDept =column[String]("empDept")

  override def * =(empId,empName,empCompany,empSalary,empDept)<> ((Employee.apply _).tupled,Employee.unapply)
}


class EmployeeList @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{
  var employeeList = TableQuery[EmployeeTable]

  def add(emp:Employee)={
    dbConfig.db
      .run(employeeList+=emp)
      .map(res=>"Employee successfully added")
    listAll.map(x=>x.reduce((a,b)=>if(a.empId>b.empId)a
    else b))

  }

  def delete(empId:Int):Future[Int]={
    dbConfig.db.run(employeeList.filter(_.empId===empId).delete)
  }

  def update(emp:Employee):Future[Int]={
    dbConfig.db
      .run(employeeList.filter(_.empId===emp.empId)
      .map(x=>(x.empName,x.empCompany,x.empSalary,x.empDept))
        .update(emp.empName,emp.empCompany,emp.empSalary,emp.empDept)
      )
  }

  def get(empId:Int):Future[Option[Employee]]={
    dbConfig.db.run(employeeList.filter(_.empId === empId).result.headOption)
  }

  def listAll:Future[Seq[Employee]]={
    dbConfig.db.run(employeeList.result)
  }
}