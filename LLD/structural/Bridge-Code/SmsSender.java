public class SmsSender implements MessageSender {

    @Override
    public void send(String text) {
        System.out.println("SMS: " + text);
    }
}
