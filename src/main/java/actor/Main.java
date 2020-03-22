package actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("MySystem");
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class), "master");
        masterActor.tell("sentence", ActorRef.noSender());
        sleep(2000);
        system.terminate();
    }
}