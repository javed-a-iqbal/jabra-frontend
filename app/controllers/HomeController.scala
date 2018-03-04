package controllers

import java.util
import javax.inject._

import play.api._
import play.api.i18n.MessagesApi
import play.api.mvc._
import forms.PersonForms._
import models.Person
import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.Controller
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import reactivemongo.bson._
import reactivemongo.api.collections.bson.BSONCollection


import javax.inject.Inject
import scala.concurrent.Future
import scala.concurrent.duration._

import play.api.mvc._
import play.api.libs.ws._
import play.api.http.HttpEntity

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.ExecutionContext


@Singleton
class HomeController @Inject() (ws: WSClient)(val reactiveMongoApi: ReactiveMongoApi) (implicit val messagesApi: MessagesApi, context: ExecutionContext) extends Controller with i18n.I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.index(userform))
  }

  def submit = Action { implicit request =>

    userform.bindFromRequest.fold(
              formWithErrors => {
                BadRequest(views.html.index(formWithErrors))
              },
              successSub => {
                sendData(successSub)
                Ok("Success " + successSub.firstName + " " + successSub.lastName + Json.toJson(successSub))
                }
    )

  }

  def sendData(person : Person): Future[WSResponse] ={
    val url="http://localhost:7900/receive-data"
   ws.url(url).post(Json.toJson(person)).map {
     response =>
       println(response)
       (response)
   }

  }


}
