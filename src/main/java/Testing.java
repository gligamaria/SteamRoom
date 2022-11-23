import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Float.min;
import static java.lang.Float.parseFloat;

public class Testing {

    ArrayList<Profile> profiles = new ArrayList<>();
    Scanner keyboard = new Scanner(System.in);
    ArrayList<String> input;
    int inputIndex = 0;
    public String output = "";
    private Profile selectedProfile;

    public Testing(ArrayList<String> input) throws InterruptedException {
        this.input = input;
        readProfiles();
        if(!profiles.isEmpty()){
            this.selectedProfile = profiles.get(0);
        }
        test();
    }

    public void test () throws InterruptedException {
        int chosenOption = 0;
        while(chosenOption != 5) {

            chosenOption = Integer.parseInt(input.get(inputIndex));
            inputIndex++;
            if(chosenOption == 1){
                chooseProfile();
            }
            else if(chosenOption == 2){
                createProfile();
            }
            else if(chosenOption == 3){
                startRunning();
            }
            else if(chosenOption == 4){
                startRunning();
            }
        }
        System.out.println(output);
        //return output;
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

        int profileNumber = Integer.parseInt(input.get(inputIndex));
        inputIndex++;
        selectedProfile = profiles.get(profileNumber - 1);
        output = output.concat("Chosen profile: "+ selectedProfile.getName() + ", temperature: " + selectedProfile.getWantedTemperature() +
                ", humidity: " +  selectedProfile.getWantedHumidity() + "\n");

    }

    public String isDataCorrect(String name, int wantedTemperature, int wantedHumidity){

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

    public void createProfile(){

        String name = input.get(inputIndex);
        inputIndex++;
        int wantedTemperature = Integer.parseInt(input.get(inputIndex));
        inputIndex++;
        int wantedHumidity = Integer.parseInt(input.get(inputIndex));
        inputIndex++;

        while (!isDataCorrect(name, wantedTemperature, wantedHumidity).equals("Data is correct.")){
            output = output.concat(isDataCorrect(name, wantedTemperature, wantedHumidity) + "\n");
            name = input.get(inputIndex);
            inputIndex++;
            wantedTemperature = Integer.parseInt(input.get(inputIndex));
            inputIndex++;
            wantedHumidity = Integer.parseInt(input.get(inputIndex));
            inputIndex++;
        }

        String answer = input.get(inputIndex);
        inputIndex++;
        if(answer.equals("1")){
            Profile profile = new Profile(wantedTemperature, wantedHumidity, name);
            profiles.add(profile);
            saveProfile(profile);
            output = output.concat("Profile created.\n");
        }

    }

    public void startRunning() throws InterruptedException {
        float currentHumidity = parseFloat(input.get(inputIndex));
        inputIndex++;
        float currentTemperature = parseFloat(input.get(inputIndex));
        inputIndex++;
        boolean stop = false;
        boolean waterSupply = true;
        boolean others = true;
        boolean doorOpen = false;
        int doorTimer = 0;
        int minutesUtilSchedule;
        boolean scheduleLater;
        boolean scheduleError = false;

        if(input.get(inputIndex).equals("True")){
            scheduleLater = true;
            output = output.concat("Schedule programmed.\n");
        }
        else {
            scheduleLater = false;
        }
        inputIndex++;

        if(scheduleLater){

            minutesUtilSchedule = Integer.parseInt(input.get(inputIndex));
            inputIndex++;

            if(minutesUtilSchedule < Math.max(selectedProfile.getWantedHumidity() - currentHumidity,
                    selectedProfile.getWantedTemperature() - currentTemperature)){
                output = output.concat("There is not enough time for the steam room to be ready.");
                stop = true;
                scheduleError = true;
            }

            if (!stop){
                while(minutesUtilSchedule != 0){
                    output = output.concat("Steam room will start in " + minutesUtilSchedule + " minutes.\n");
                    minutesUtilSchedule--;
                }
            }

        }

        while(!stop && waterSupply && others){

            output = output.concat("Current temperature: " + currentTemperature + "\n");
            output = output.concat("Current humidity: " + currentHumidity + "\n");

            String command = input.get(inputIndex);
            inputIndex++;

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
            if(currentHumidity == selectedProfile.getWantedHumidity() &&
                    currentTemperature == selectedProfile.getWantedTemperature()){
                output = output.concat("The steam room has reached the desired settings.\n");
            }


        }

        if (!waterSupply){
            // the system has stopped because of the lack of water supply
            output = output.concat("The system has stopped because there was no water supply.\n");
            output = output.concat("-----------------------------\n");
        } else if (stop && !doorOpen && !scheduleError){
            // the system has stopped because the user wanted it to
            output = output.concat("The system has stopped successfully.\n");
            output = output.concat("-----------------------------\n");

        }


    }


}
