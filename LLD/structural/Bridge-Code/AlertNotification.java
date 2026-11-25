public class AlertNotification extends Notification {

    public AlertNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notifyUser(String message) {
        sender.send("[ALERT] " + message.toUpperCase());
    }
}
