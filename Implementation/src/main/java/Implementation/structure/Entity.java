package Implementation.structure;

public abstract class Entity {

    private Send preparedMessage = null;

    private abstract static class Send extends Thread {
        public abstract Message getMessage();

        public abstract Entity getReceiver();

        public abstract void run();
    }

    public void prepareMessage(Message message, Entity receiver) {
        if (message == null || receiver == null) {
            return;
        }
        Entity sender = this;
        class _Send extends Send {
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
        this.preparedMessage = new _Send();
    }

    public synchronized void sendMessage(Message message, Entity receiver) {
        if (message == null || receiver == null) {
            return;
        }
        if (preparedMessage == null) {
            prepareMessage(message, receiver);
        }
        if (preparedMessage != null && preparedMessage.getMessage() == message && preparedMessage.getReceiver() == receiver) {
            this.preparedMessage.start();
            this.preparedMessage = null;
        } else {
            System.err.println("Preparation of the message failed. Message name: " + message.getName());
        }
    }

    public abstract String getName();

    public abstract void onReceiveMessage(Message message, Entity sender);
}
