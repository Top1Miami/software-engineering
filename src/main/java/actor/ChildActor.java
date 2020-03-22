package actor;

import akka.actor.UntypedActor;

public class ChildActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof Request) {
            Response response = new Response(((Request) o).getSearcher(), StubServer.getResponse((Request) o));
            getSender().tell(response, getSelf());
            return;
        }
    }
}