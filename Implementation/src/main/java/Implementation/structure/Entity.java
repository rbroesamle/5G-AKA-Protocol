package Implementation.structure;

public abstract class Entity {

    void sendMessage(Message message, Entity receiver) {
        Entity sender = this;
        class Send extends Thread {
            @Override
            public void run() {
                receiver.onReceiveMessage(message, sender);
            }
        }
        new Send().start();
    }

    public abstract void onReceiveMessage(Message message, Entity sender);

}
