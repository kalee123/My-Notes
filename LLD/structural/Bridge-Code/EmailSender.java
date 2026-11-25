public class EmailSender implements MessageSender {

    @Override
    public void send(String text) {
        System.out.println("Email: " + text);
    }
}
