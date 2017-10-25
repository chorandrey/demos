#### AkkaAskDemo.scala

This demo proves that there is no blocking inside actor if another actor (or client code) use ask pattern and waits for reply from this actor. How was it tested?

I used 2 separate client threads which perform request using ask-pattern (akka.pattern.ask) and receiver actor (using class Receiver). Receiver expects to get 2 messages. First message consumed will be without reply, but we save ActorRef of sender() to send reply a little later. Than we receive second message. We reply to second sender, than reply to first and got expected result. Actor wasn't blocked while first sender was waiting for reply and could consume second message. Test output is shows below:

```scala
Started program
Thread 1 started
receive first message
received message: ask Message from 1 thread
Thread 2 started
context.become (2nd message)
received message: ask Message from 2 thread
Respond to second message
Got result for thread 2: Response to 2nd message
Got result for thread 1: Response to 1st message
```