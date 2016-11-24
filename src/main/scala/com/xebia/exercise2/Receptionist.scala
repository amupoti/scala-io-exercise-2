package com.xebia
package exercise2

import akka.pattern.ask
import akka.util.Timeout
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

//DONE_TODO mixin your ActorContextCreationSupport trait
class Receptionist extends HttpServiceActor
  with ReverseRoute
  with ActorContextCreationSupport {

  implicit def executionContext = context.dispatcher

  def receive = runRoute(reverseRoute)
}

//DONE_TODO mixin the CreationSupport trait so createChild will be available here
trait ReverseRoute extends HttpService
  with CreationSupport {
  implicit def executionContext: ExecutionContext

  import ReverseActor._

  private val reverseActor = createChild(props, name)

  def reverseRoute:Route = path("reverse") {
    post {
      entity(as[ReverseRequest]) { request =>
        implicit val timeout = Timeout(20 seconds)

        //TODO_DONE based on the result (ReverseResult or PalindromeResult)
        // complete with a ReverseResponse that indicates isPalindrome
        val futureResponse = reverseActor.ask(Reverse(request.value)) map {
          case r: ReverseResult => ReverseResponse(r.value)
          case r: PalindromeResult => ReverseResponse(request.value, isPalindrome = true)
        }

        complete(futureResponse)
      }
    }
  }
}

