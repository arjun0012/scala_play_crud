package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class EmployeeControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting{

  "EmployeeController GET" should{
    "render the employee list page from the router" in {
      val request = FakeRequest(GET, "/0/emp")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("application/json")
    }
  }
  "EmployeeController DELETE" should {
    "not allow delete employee of different company " in {
      val request = FakeRequest(DELETE, "/2/emp/8")
      val home = route(app, request).get

      status(home) mustBe UNAUTHORIZED
      contentType(home) mustBe Some("application/json")
    }
  }
}
