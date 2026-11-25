public class BridgeMain {

    public static void main(String[] args) {

        /* This class shows example of bridge pattern.
           Two things vary independently: the kind of notification (alert, reminder)
           and the way it is sent (email, sms). Any kind can be paired with any sender.
         */
        System.out.println("Bridge Pattern");

        Notification alertByEmail = new AlertNotification(new EmailSender());
        Notification alertBySms = new AlertNotification(new SmsSender());
        Notification reminderByEmail = new ReminderNotification(new EmailSender());
        Notification reminderBySms = new ReminderNotification(new SmsSender());

        alertByEmail.notifyUser("server down");
        alertBySms.notifyUser("server down");
        reminderByEmail.notifyUser("standup at 10");
        reminderBySms.notifyUser("standup at 10");
    }
}
