package Implementation.structure;

public abstract class Entity {

    private Sender preparedMessageSender = null;

    private abstract static class Sender extends Thread {
        public abstract Message getMessage();

        public abstract Entity getReceiver();

        public abstract void run();
    }

    public void prepareMessage(Message message, Entity receiver) {
        if (message == null || receiver == null) {
            return;
        }
        Entity sender = this;
        class _Sender extends Sender {
            @Override
            public Message getMessage() {
                return message;
            }

            @Override
            public Entity getReceiver() {
                return receiver;
            }

            @Override
            public void run() {
                receiver.onReceiveMessage(message, sender);
            }
        }
        this.preparedMessageSender = new _Sender();
    }

    public synchronized void sendMessage(Message message, Entity receiver) {
        if (message == null || receiver == null) {
            return;
        }
        if (preparedMessageSender == null) {
            prepareMessage(message, receiver);
        }
        if (preparedMessageSender != null && preparedMessageSender.getMessage() == message && preparedMessageSender.getReceiver() == receiver) {
            this.preparedMessageSender.start();
            this.preparedMessageSender = null;
        } else {
            System.err.println("Preparation of the message failed. Message name: " + message.getName());
        }
    }

    public abstract String getName();

    public abstract void onReceiveMessage(Message message, Entity sender);
}
