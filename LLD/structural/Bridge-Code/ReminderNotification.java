public class ReminderNotification extends Notification {

    public ReminderNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void notifyUser(String message) {
        sender.send("Reminder: " + message);
    }
}
