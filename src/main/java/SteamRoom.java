import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SteamRoom {

    ArrayList<Profile> profiles = new ArrayList<>();
    Scanner keyboard = new Scanner(System.in);

    private Profile selectedProfile;

    public SteamRoom() throws InterruptedException {
        readProfiles();
        if(!profiles.isEmpty()){
            this.selectedProfile = profiles.get(0);
        }
        int chosenOption = 0;
        while(chosenOption != 5) {

            System.out.println("1. Choose a profile");
            System.out.println("2. Create a profile");
            System.out.println("3. Start");
            System.out.println("4. Schedule for later");

            chosenOption = keyboard.nextInt();
            if(chosenOption == 1){
                chooseProfile();
            }
            else if(chosenOption == 2){
                createProfile();
            }
            else if(chosenOption == 3){
                startRunning(25, 45, false);
            }
            else if(chosenOption == 4){
                startRunning(25, 45, true);
            }
        }
    }

    public void readProfiles(){
        try {
            File myObj = new File("profiles.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String name = myReader.nextLine();
                String temperature = myReader.nextLine();
                String humidity = myReader.nextLine();
                Profile newProfile = new Profile(Integer.parseInt(temperature),  Integer.parseInt(humidity), name);
                profiles.add(newProfile);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public void saveProfile(Profile profile){
        try {
            FileWriter myWriter = new FileWriter("profiles.txt", true);
            myWriter.write(profile.getName() + "\n" + profile.getWantedTemperature() + "\n" + profile.getWantedHumidity() + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
            saveProfile(profile);
            System.out.println("You have successfully created profile " + name + ".");
            System.out.println();
        }

    }

    public void startRunning(float currentTemperature, float currentHumidity, boolean scheduleLater) throws InterruptedException {
        Float currentHumidity1 = currentHumidity;
        Float currentTemperature1 = currentTemperature;
        boolean stop = false;
        boolean waterSupply = true;
        boolean others = true;
        boolean doorOpen = false;
        int doorTimer = 0;
        int minutesUtilSchedule;
        boolean scheduleError = false;

        if(scheduleLater){
            System.out.println("Please insert in how many minutes the system should start.");
            minutesUtilSchedule = keyboard.nextInt();

            if(minutesUtilSchedule < Math.max(selectedProfile.getWantedHumidity() - currentHumidity,
                    selectedProfile.getWantedTemperature()) - currentTemperature){
                System.out.println("There is not enough time for the steam room to be ready.");
                stop = true;
                scheduleError = true;
            }

            if (!stop){
                System.out.println("Schedule programmed.");
                while(minutesUtilSchedule != 0){
                    System.out.println("Steam room will start in " + minutesUtilSchedule + " minutes.");
                    Thread.sleep(1000);
                    minutesUtilSchedule--;
                }
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
                System.out.println("Please close the door.");
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

            if(currentHumidity == selectedProfile.getWantedHumidity() &&
                    currentTemperature == selectedProfile.getWantedTemperature()){
                System.out.println("The steam room has reached the desired settings.");
            }


            Thread.sleep(1000);
        }

        if (!waterSupply){
            // the system has stopped because of the lack of water supply
            System.out.println("The system has stopped because there was no water supply.");
            System.out.println("-----------------------------");
        } else if (stop && !doorOpen && !scheduleError){
            // the system has stopped because the user wanted it to
            System.out.println("The system has stopped successfully.");
            System.out.println("-----------------------------");
        }

    }


}
