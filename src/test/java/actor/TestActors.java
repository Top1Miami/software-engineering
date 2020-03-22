package actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class TestActors {

    private ActorSystem system;

    @BeforeEach
    void init() {
        system = ActorSystem.create("MySystem");
    }

    @AfterEach
    void terminate() {
        Keeper.result.clear();
        system.terminate();
    }

    @Test
    void withoutHang() throws InterruptedException {
        StubServer.hangProbability = 0;
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class), "master");
        masterActor.tell("boi", ActorRef.noSender());
        sleep(1000);
        Assertions.assertEquals(Keeper.result.size(), 3);
        Set<String> searcherNames = new HashSet<>();
        for (Response response : Keeper.result) {
            String name = response.getSearcher().name();
            Assertions.assertNotEquals(name, "Random");
            searcherNames.add(name);
        }
        Assertions.assertEquals(searcherNames.size(), 3);
    }

    @Test
    void withHang() throws InterruptedException {
        StubServer.hangProbability = 1;
        ActorRef masterActor = system.actorOf(Props.create(MasterActor.class), "master");
        masterActor.tell("girl", ActorRef.noSender());
        sleep(2000);
        Assertions.assertTrue(Keeper.result.isEmpty());
    }
}