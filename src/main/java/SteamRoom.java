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
        // read the profiles saved in memory so that past profiles
        // are still taken into account
        readProfiles();
        if(!profiles.isEmpty()){
            // setting the selected profile to the default one, which
            // was manually written into the file
            this.selectedProfile = profiles.get(0);
        }
        int chosenOption = 0;

        // until the user insets the command "5", the application will keep running
        while(chosenOption != 5) {

            System.out.println("1. Choose a profile");
            System.out.println("2. Create a profile");
            System.out.println("3. Start");
            System.out.println("4. Schedule for later");

            chosenOption = keyboard.nextInt();

            // based on the chosen command, different methods are called. In the case
            // of starting the application on the spot and scheduling for later, the same
            // method is called, but with one parameter that differs, as the functions
            // commands imply the same code from a certain point.
            if(chosenOption == 1){
                chooseProfile();
            }
            else if(chosenOption == 2){
                createProfile();
            }
            else if(chosenOption == 3){
                // the initial current temperature and humidity can be manually set
                startRunning(25, 45, false);
            }
            else if(chosenOption == 4){
                // the initial current temperature and humidity can be manually set
                startRunning(25, 45, true);
            }
        }
    }

    public void readProfiles(){
        // the method reads from "profile.txt" file the information about the existing
        // profiles. They are saved in the following order: name, temperature and
        // humidity, each one on a different line.
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
        // The method saves a new profile into the same, previously mentioned
        // file so that the profile will also be available for future executions
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
        // printing out the list of profiles in a user-friendly way
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
        // based on the chosen profile number, the right profile is retrieved from memory
        // and selected profile takes its values in terms of name, temperature and humidity
        selectedProfile = profiles.get(chosenProfile - 1);
        // the user is announced that the action has been completed
        System.out.println("You have successfully selected profile no." + chosenProfile + ".");
        System.out.println();
    }

    public boolean isDataCorrect(String name, int wantedTemperature, int wantedHumidity){

        // the function checks if a name is unique in the list of already existing profiles,
        // if the name has been inserted at all and also if the values for temperature and
        // humidity are in the right range. The user is informed if any of the info is invalid

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

        // The user inputs a name and wanted values for temperature and humidity
        // for the profile to be created

        System.out.print("Profile name: ");
        String name = keyboard.next();
        System.out.print("Wanted temperature: ");
        int wantedTemperature = keyboard.nextInt();
        System.out.print("Wanted humidity: ");
        int wantedHumidity = keyboard.nextInt();

        // Until there is a valid set of data, the user has to keep on inserting data

        while (!isDataCorrect(name, wantedTemperature, wantedHumidity)){
            System.out.print("Profile name: ");
            name = keyboard.next();
            System.out.print("Wanted temperature: ");
            wantedTemperature = keyboard.nextInt();
            System.out.print("Wanted humidity: ");
            wantedHumidity = keyboard.nextInt();
        }

        // The user is asked once again if they want to create a profile based
        // on the inserted data (which at this point is 100% valid)

        System.out.println("Create profile?");
        System.out.println("1.Yes   2.No");
        String answer = keyboard.next();

        if(answer.equals("1")){
            // if the answer is yes, the profile is saved both into the list of profiles
            // and into memory so that it is available right after its creation, but
            // also in the future
            Profile profile = new Profile(wantedTemperature, wantedHumidity, name);
            profiles.add(profile);
            saveProfile(profile);
            System.out.println("You have successfully created profile " + name + ".");
            System.out.println();
        }

    }

    public void startRunning(float currentTemperature, float currentHumidity, boolean scheduleLater) throws InterruptedException {

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

            // there is a check to see if there is enough time for the steam room to reach the
            // desired setting before the moment in time they want it to be ready
            if(minutesUtilSchedule < Math.max(selectedProfile.getWantedHumidity() - currentHumidity,
                    selectedProfile.getWantedTemperature()) - currentTemperature){
                System.out.println("There is not enough time for the steam room to be ready.");
                stop = true;
                scheduleError = true;
            }

            // if there is enough time, the user is announced that the schedule has been programed
            // is shown how much longer it takes until the desired time
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

            // At each iteration, which in reality would represent a minute, various actions could
            // be taken. Some of those are not normally under the user's control - such as the water
            // supply, but they have been added here so that the system's functioning can be properly
            // tested.

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
                // if the door is open for more than 3 minutes, the steam room should stop
                stop = true;
                System.out.println("The system stopped as the door has been open for too long.");
                System.out.println("-----------------------------");
            }

            if(currentTemperature < selectedProfile.getWantedTemperature()){
                // if the desired temperature has not been reached, it normally increases by one degree
                // each minute
                currentTemperature++;
            }
            else if (currentTemperature == 47){
                others = false;
                System.out.println("A malfunction happened and the temperature was unsafe.");
                System.out.println("-----------------------------");
            }

            if(currentHumidity < selectedProfile.getWantedHumidity()){
                // if the desired humidity has not been reached, it normally increases by one percentage
                // each minute
                currentHumidity++;
            }

            if(currentHumidity == selectedProfile.getWantedHumidity() &&
                    currentTemperature == selectedProfile.getWantedTemperature()){
                // the user is informed if both the wanted temperature and wanted humidity have been reached
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
