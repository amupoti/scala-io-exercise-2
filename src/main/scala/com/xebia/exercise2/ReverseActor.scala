package com.xebia
package exercise2

import akka.actor.{Actor, Props}

object ReverseActor {
  def props = Props(new ReverseActor)
  def name = "reverser"

  case class Reverse(value:String)
  case class ReverseResult(value:String)
  case class PalindromeResult()
}

class ReverseActor extends Actor {
  import ReverseActor._

  def receive = {
    case Reverse(value) if value.reverse.equals(value) => sender ! PalindromeResult()
    case Reverse(value) =>
      //TODO_DONE verify if the value is a palindrome, return a PalindromeResult if this is the case,
      sender ! ReverseResult(value.reverse)
  }
}
