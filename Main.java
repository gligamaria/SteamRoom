import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ArrayList<Profile> profiles = new ArrayList<>();
        Scanner keyboard = new Scanner(System.in);

        Profile defaultProfile = new Profile(44, 92, "Default");
        SteamRoom steamRoom = new SteamRoom(defaultProfile);

        int chosenOption = 0;

        while(chosenOption != 4) {

            System.out.println("1. Choose a profile");
            System.out.println("2. Create a profile");
            System.out.println("3. Start");
            System.out.println("4. Schedule for later");

            chosenOption = keyboard.nextInt();
            if(chosenOption == 1){
                steamRoom.chooseProfile();
            }
            else if(chosenOption == 2){
                steamRoom.createProfile();
            }
            else if(chosenOption == 3){
                steamRoom.startRunning(25, 45, false, 5);
            }
            else if(chosenOption == 4){
                steamRoom.startRunning(25, 45, true, 5);
            }
        }

    }
}
