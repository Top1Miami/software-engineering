package actor;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasterActor extends UntypedActor {

    List<Response> responseList;

    MasterActor() {
        getContext().setReceiveTimeout(Duration.create(1000, TimeUnit.MILLISECONDS));
    }

    @Override
    public void onReceive(Object o) {
        if (o instanceof String) {
            responseList = new ArrayList<>();
            for (int i = 0; i < Searcher.values().length; i++) {
                ActorRef child = getContext().actorOf(Props.create(ChildActor.class), "actor" + Searcher.values()[i].name());
                child.tell(new Request(Searcher.values()[i], (String) o), getSelf());
            }
        } else if (o instanceof ReceiveTimeout) {
            Keeper.result.addAll(responseList);
            getContext().stop(getSelf());
        } else if (o instanceof Response) {
            responseList.add((Response) o);
            if (responseList.size() == 3) {
                Keeper.result.addAll(responseList);
                getContext().stop(getSelf());
            }
        }
    }
}
