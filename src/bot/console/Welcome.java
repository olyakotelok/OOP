package bot.console;

public class Welcome {
    public static void main(String[] args) {
        System.out.println("Hello! There will be information about me. \n\n What topic is interesting for you?");
        System.out.println("1. History \n 2.Physics");
        System.out.print("Enter number:");
        int topic = System.in.read();
        if (topic == 1) {
            System.out.println("Okay, your choice is History!");
        }
        else {
            System.out.println("Okay, your choice is Physics!");
        }
    }
}
