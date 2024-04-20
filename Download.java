package FireData;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Download {
    private static final double SAFE_DISTANCE_KM = 200.0;
    private static final double FIRE_RADIUS_KM = 10.0;

    public Download() {
    }

    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);
        try {
            System.out.print("Enter your longitude: ");
            double inputX = inputScanner.nextDouble();
            System.out.print("Enter your latitude: ");
            double inputY = inputScanner.nextDouble();
            inputScanner.nextLine();
            System.out.print("Enter the date (e.g., 2024-02-26): ");
            String inputDateStr = inputScanner.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate inputDate = LocalDate.parse(inputDateStr, formatter);
            File csvFile = new File("C:\\Users\\110jo\\OneDrive\\Desktop\\Third Year\\CSCI 121\\FirePredictSys\\src\\FireData\\FireData.csv");
            Scanner scanner = new Scanner(csvFile);
            boolean isSafe = true;
            String closestFireId = "";
            String closestFireName = "";
            double closestDistance = Double.MAX_VALUE;
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] values = line.split(",");
                if (values.length >= 10) {
                    try {
                        double fireX = Double.parseDouble(values[5].trim());
                        double fireY = Double.parseDouble(values[6].trim());
                        LocalDate fireDate = LocalDate.parse(values[3].trim().split(" ")[0], formatter);
                        String fireIdentifier = values[9].trim();
                        String fireName = values[4].trim();
                        double distance = calculateDistance(inputY, inputX, fireY, fireX);
                        if (distance < FIRE_RADIUS_KM) {
                            isSafe = false;
                            closestFireId = fireIdentifier;
                            closestFireName = fireName;
                            break;
                        }
                        if (!fireDate.isBefore(inputDate) && distance < SAFE_DISTANCE_KM && distance < closestDistance) {
                            closestDistance = distance;
                            closestFireId = fireIdentifier;
                            closestFireName = fireName;
                            isSafe = false;
                        }
                    } catch (DateTimeParseException | NumberFormatException var31) {
                        System.out.println("Error parsing data: " + var31.getMessage());
                    }
                }
            }

            scanner.close();
            if (isSafe) {
                System.out.println("You are safe from nearby fires.");
            } else {
                System.out.printf("You are dangerously close to %s - %s, which is only %.2f km away.\n", closestFireId, closestFireName, closestDistance);
            }
        } catch (FileNotFoundException var32) {
            var32.printStackTrace();
        } finally {
            inputScanner.close();
        }

    }
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }
}