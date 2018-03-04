package forms
import models._
import play.api.data.Form
import play.api.data.Forms._

object PersonForms {

  val userform = Form(
    mapping(
      "firstName" -> text(minLength = 3),
      "lastName" -> text,
      "age" -> number
    )(Person.apply)(Person.unapply)
  )

}