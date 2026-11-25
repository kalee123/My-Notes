/* Dimension 1: WHAT kind of message it is.
   It holds a MessageSender. That field is the "bridge" between the two dimensions. */
public abstract class Notification {

    protected final MessageSender sender;

    protected Notification(MessageSender sender) {
        this.sender = sender;
    }

    public abstract void notifyUser(String message);
}
