import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SteamRoom {

    ArrayList<Profile> profiles = new ArrayList<>();
    Scanner keyboard = new Scanner(System.in);

    private Float currentTemperature;
    private Float currentHumidity;
    private Profile selectedProfile;

    public SteamRoom(Profile defaultProfile){
        profiles.add(defaultProfile);
        this.selectedProfile = profiles.get(0);
    }

    public void chooseProfile(){
        System.out.println("List of profiles:");
        for(Profile profile:profiles){
            int index = profiles.indexOf(profile) + 1;
            System.out.print(index + ". ");
            System.out.println(profile.getName());
            System.out.println("Wanted temperature: " + profile.getWantedTemperature());
            System.out.println("Wanted humidity: " + profile.getWantedHumidity());
            System.out.println("-----------------------------");
        }
        System.out.println("Insert the number of the desired profile: ");
        int chosenProfile = keyboard.nextInt();
        selectedProfile = profiles.get(chosenProfile - 1);
        System.out.println("You have successfully selected profile no." + chosenProfile + ".");
        System.out.println();
    }

    public boolean isDataCorrect(String name, int wantedTemperature, int wantedHumidity){
        boolean correct = true;
        for(Profile profile:profiles){
            if(name.equals("")){
                System.out.println("Please input a name.");
                correct = false;
            }
            if(name.equals(profile.getName())){
                System.out.println("Please insert a unique name.");
                correct = false;
            }
        }
        if(wantedTemperature < 43 || wantedTemperature > 46){
            System.out.println("Please insert a temperature >= 43 and <= 46.");
            correct = false;
        }
        if(wantedHumidity < 97 || wantedHumidity > 100){
            System.out.println("Please insert a humidity >= 97 and <= 100.");
            correct = false;
        }
        return correct;
    }

    public void createProfile(){

        System.out.print("Profile name: ");
        String name = keyboard.next();
        System.out.print("Wanted temperature: ");
        int wantedTemperature = keyboard.nextInt();
        System.out.print("Wanted humidity: ");
        int wantedHumidity = keyboard.nextInt();

        while (!isDataCorrect(name, wantedTemperature, wantedHumidity)){
            System.out.print("Profile name: ");
            name = keyboard.next();
            System.out.print("Wanted temperature: ");
            wantedTemperature = keyboard.nextInt();
            System.out.print("Wanted humidity: ");
            wantedHumidity = keyboard.nextInt();
        }

        System.out.println("Create profile?");
        System.out.println("1.Yes   2.No");
        String answer = keyboard.next();

        if(answer.equals("1")){
            Profile profile = new Profile(wantedTemperature, wantedHumidity, name);
            profiles.add(profile);
            System.out.println("You have successfully created profile " + name + ".");
            System.out.println();
        }

    }

    public void startRunning(float currentTemperature, float currentHumidity, boolean scheduleLater, int minutesUtilSchedule) throws InterruptedException {
        this.currentHumidity = currentHumidity;
        this.currentTemperature = currentTemperature;
        boolean stop = false;
        boolean waterSupply = true;
        boolean others = true;
        boolean doorOpen = false;
        int doorTimer = 0;

        if(scheduleLater){
            System.out.println("Please insert in how many minutes the system should start.");
            minutesUtilSchedule = keyboard.nextInt();

            System.out.println("Schedule programmed.");
            while(minutesUtilSchedule != 0){
                System.out.println("Steam room will start in " + minutesUtilSchedule + " minutes.");
                Thread.sleep(1000);
                minutesUtilSchedule--;
            }
        }

        while(!stop && waterSupply && others){

            System.out.println("Wanted temperature: " + selectedProfile.getWantedTemperature());
            System.out.println("Wanted humidity: " + selectedProfile.getWantedHumidity());
            System.out.println("-----------------------------");
            System.out.println("Current temperature: " + currentTemperature);
            System.out.println("Current humidity: " + currentHumidity);
            System.out.println("-----------------------------");
            System.out.println("Press 1 to open/close door.");
            System.out.println("Press 2 to stop the steam room.");
            System.out.println("Press 3 to cut water supply.");
            System.out.println("Press 4 to do nothing.");

            String command = keyboard.next();

            switch (command) {
                case "1" -> doorOpen = !doorOpen;
                case "2" -> stop = true;
                case "3" -> waterSupply = false;
            }

            if(doorOpen && doorTimer <= 3){
                doorTimer++;
                System.out.println("Door is open!");
                // the temperature and humidity decrease by half a degree because the door is open
                currentHumidity -= 0.5;
                currentTemperature -= 0.5;
            }
            else {
                doorTimer = 0;
            }

            if(doorTimer == 2 || doorTimer == 3){
                alertUser();
            }
            if (doorTimer > 3){
                stop = true;
                System.out.println("The system stopped as the door has been open for too long.");
                System.out.println("-----------------------------");
            }

            if(currentTemperature < selectedProfile.getWantedTemperature()){
                currentTemperature++;
            }
            else if (currentTemperature == 47){
                others = false;
                System.out.println("A malfunction happened and the temperature was unsafe.");
                System.out.println("-----------------------------");
            }

            if(currentHumidity < selectedProfile.getWantedHumidity()){
                currentHumidity++;
            }

            Thread.sleep(1000);
        }

        if (!waterSupply){
            // the system has stopped because of the lack of water supply
            System.out.println("The system has stopped because there was no water supply.");
            System.out.println("-----------------------------");
        } else if (stop && !doorOpen){
            // the system has stopped because the user wanted it to
            System.out.println("The system has stopped successfully.");
            System.out.println("-----------------------------");
        }

    }

    public void alertUser(){
        System.out.println("Please close the door.");
    }


}
