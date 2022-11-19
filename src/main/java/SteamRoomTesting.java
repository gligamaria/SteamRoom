import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Float.parseFloat;

public class SteamRoomTesting {

    ArrayList<Profile> profiles = new ArrayList<>();

    private Profile selectedProfile;

    public SteamRoomTesting(){
        readProfiles();
        if(!profiles.isEmpty()){
            this.selectedProfile = profiles.get(0);
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

    public String chooseProfile(String profileNumberString){
        int profileNumber = Integer.parseInt(profileNumberString);
        selectedProfile = profiles.get(profileNumber - 1);
        return ("name: " + selectedProfile.getName() + " temperature: " + selectedProfile.getWantedTemperature() +
                " humidity: " +  selectedProfile.getWantedHumidity());

    }

    public String isDataCorrect(String name, int wantedTemperature, int wantedHumidity){
        boolean correct = true;
        for(Profile profile:profiles){
            if(name.equals("")){
                return "The profile should be given a name.";
            }
            if(name.equals(profile.getName())){
                return "The profile should be given a unique name.";
            }
        }
        if(wantedTemperature < 43 || wantedTemperature > 46){
            return "Please insert a temperature >= 43 and <= 46.";
        }
        if(wantedHumidity < 97 || wantedHumidity > 100){
            return "Please insert a humidity >= 97 and <= 100.";
        }
        return "Data is correct.";
    }

    public void createProfile(ArrayList<String> inputs){

        int i;
        String name = inputs.get(0);
        int wantedTemperature = Integer.parseInt(inputs.get(1));
        int wantedHumidity = Integer.parseInt(inputs.get(2));
        i = 3;

        while (!isDataCorrect(name, wantedTemperature, wantedHumidity).equals("Data is correct.")){
            name = inputs.get(i);
            i++;
            wantedTemperature = Integer.parseInt(inputs.get(i));
            i++;
            wantedHumidity = Integer.parseInt(inputs.get(i));
            i++;
        }

        String answer = inputs.get(i);

        if(answer.equals("1")){
            Profile profile = new Profile(wantedTemperature, wantedHumidity, name);
            profiles.add(profile);
        }

    }

    public String startRunning(ArrayList<String> inputs) throws InterruptedException {

        float currentHumidity = parseFloat(inputs.get(0));
        float currentTemperature = parseFloat(inputs.get(1));
        boolean stop = false;
        boolean waterSupply = true;
        boolean others = true;
        boolean doorOpen = false;
        int doorTimer = 0;
        int minutesUtilSchedule;
        String output = "";
        boolean scheduleLater;
        if(inputs.get(3).equals("True")){
            scheduleLater = true;
            output = output.concat("Schedule programmed.\n");
        }
        else {
            scheduleLater = false;
        }
        int i = 4;

        if(scheduleLater){
            minutesUtilSchedule = Integer.parseInt(inputs.get(i));
            i++;

            while(minutesUtilSchedule != 0){
                output = output.concat("Steam room will start in " + minutesUtilSchedule + " minutes.\n");
                minutesUtilSchedule--;
            }
        }

        while(!stop && waterSupply && others){

            output = output.concat("Current temperature: " + currentTemperature + "\n");
            output = output.concat("Current humidity: " + currentHumidity + "\n");

            String command = inputs.get(i);
            i++;

            switch (command) {
                case "1" -> doorOpen = !doorOpen;
                case "2" -> stop = true;
                case "3" -> waterSupply = false;
            }

            if(doorOpen && doorTimer <= 3){
                doorTimer++;
                output = output.concat("Door is open!\n");
                // the temperature and humidity decrease by half a degree because the door is open
                currentHumidity -= 0.5f;
                currentTemperature -= 0.5f;
            }
            else {
                doorTimer = 0;
            }

            if(doorTimer == 2 || doorTimer == 3){
                output = output.concat("Please close the door.\n");
            }

            if (doorTimer > 3){
                stop = true;
                output = output.concat("The system stopped as the door has been open for too long.\n");
                output = output.concat("-----------------------------\n");
            }

            if(currentTemperature < selectedProfile.getWantedTemperature()){
                currentTemperature++;
            }
            else if (currentTemperature == 47){
                others = false;
                output = output.concat("A malfunction happened and the temperature was unsafe.\n");
                output = output.concat("-----------------------------\n");
            }

            if(currentHumidity < selectedProfile.getWantedHumidity()){
                currentHumidity++;
            }

        }

        if (!waterSupply){
            // the system has stopped because of the lack of water supply
            output = output.concat("The system has stopped because there was no water supply.\n");
            output = output.concat("-----------------------------\n");
        } else if (stop && !doorOpen){
            // the system has stopped because the user wanted it to
            output = output.concat("The system has stopped successfully.\n");
            output = output.concat("-----------------------------\n");
        }
        return output;
    }


}
