import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.Float.parseFloat;

public class SteamRoomTesting {

    ArrayList<Profile> profiles = new ArrayList<>();
    Scanner keyboard = new Scanner(System.in);

    private Float currentTemperature;
    private Float currentHumidity;
    private Profile selectedProfile;

    public SteamRoomTesting(Profile defaultProfile){
        profiles.add(defaultProfile);
        this.selectedProfile = profiles.get(0);
    }

    public String chooseProfile(int profileNumber){
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

    public void startRunning(ArrayList<String> inputs) throws InterruptedException {

        this.currentHumidity =  parseFloat(inputs.get(0));
        this.currentTemperature = parseFloat(inputs.get(1));
        boolean stop = false;
        boolean waterSupply = true;
        boolean others = true;
        boolean doorOpen = false;
        int doorTimer = 0;
        int minutesUtilSchedule;
        boolean scheduleLater;
        if(inputs.get(3).equals("True")){
            scheduleLater = true;
        }
        else {
            scheduleLater = false;
        }
        int i = 4;

        if(scheduleLater){
            minutesUtilSchedule = Integer.parseInt(inputs.get(i));
            i++;
        }

        while(!stop && waterSupply && others){

            String command = inputs.get(i);
            i++;

            switch (command) {
                case "1" -> doorOpen = !doorOpen;
                case "2" -> stop = true;
                case "3" -> waterSupply = false;
            }

            if(doorOpen && doorTimer <= 3){
                doorTimer++;
                System.out.println("Door is open!");
                // the temperature and humidity decrease by half a degree because the door is open
                currentHumidity -= 0.5f;
                currentTemperature -= 0.5f;
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
