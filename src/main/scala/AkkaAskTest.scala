import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration.{SECONDS}

class TestThread (threadNum: Int, actorRef: ActorRef) extends Thread{
  override def run(): Unit = {
    println("Thread " + threadNum + " started")
    val askFuture = actorRef.ask("ask Message from " + threadNum + " thread")(Duration(5, SECONDS), ActorRef.noSender)
    val result = Await.result(askFuture, Duration(5, SECONDS))
    println(s"Got result for thread $threadNum: " + result.toString)
  }
}

object AkkaAskTest {
  val system = ActorSystem("userSystem")
  val responder = system.actorOf(Props[Responder], "responder")

  def main(args: Array[String]): Unit = {
    println("Started program")
    val thread1 = new TestThread(1, responder)
    val thread2 = new TestThread(2, responder)
    thread1.start()
    Thread.sleep(200)
    thread2.start()

    println("Finished executing")
    Thread.sleep(10000)
  }
}


class Responder extends Actor{
  var sender1: ActorRef = null

  override def receive: Receive = {
    case s: String => { // first message - string
      println("receive first message")
      println("received message: " + s)
      sender1 = sender()
      context.become(receive2)
    }
  }

  def receive2: Receive = {
    case s: String => {
      println("context.become (2nd message)")
      println("received message: " + s)
      println("Respond to second message")
      sender() ! "Response to 2nd message"
      sender1 ! "Response to 1st message"
      context.unbecome()
    }
  }
}