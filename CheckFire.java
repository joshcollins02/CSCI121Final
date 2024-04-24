import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.InputMismatchException;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static java.time.temporal.ChronoUnit.DAYS;

public class CheckFire extends JFrame {
    JLabel userInputs;
    private static final double SAFE_DISTANCE_KM = 200.0;

    // Code for GUI
    public CheckFire() {

        // Initiate some of the JFrame features (title and close operation)
        setTitle("US Forest Fire Database");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Locate button will take user input and check with our data. Other two labels are text in the JFrame
        JButton locateButton = new JButton("Locate");
        locateButton.setBounds(230, 300, 120, 30);
        JLabel titleLabel = new JLabel("Past Forest Fire Database");
        titleLabel.setBounds(220, 0, 200, 20);
        JLabel coTitleLabel = new JLabel("Are you safe?");
        coTitleLabel.setBounds(260, 20, 200, 20);


        // formatting
        JLabel leftLabel = new JLabel("---------------------------------------------------------" +
                "----------------------------------");
        leftLabel.setBounds(110, 40, 500, 20);


        // At the bottom of the JFrame, will initially not show. But after the user clicks 'locate', it will
        // write out the values the user inputted. The reason for this being that clicking reset will also make the
        // JTextFields for user input go blank, so it will be a memory saver for the user.
        userInputs = new JLabel(" ");
        userInputs.setBounds(170, 250, 300, 20);

        // Informs users the requirements for running this program. Also suggests coordinates for the continental US
        JLabel descriptionLabel = new JLabel("Input the coordinates of a location within the " +
                "United States.");
        descriptionLabel.setBounds(110, 70, 400, 20);
        JLabel descriptionTwoLabel = new JLabel("Include the date. Use simple decimal coordinates.");
        descriptionTwoLabel.setBounds(110,90,400, 20);
        JLabel descriptionThreeLabel = new JLabel("Latitude range: " +
                "25.33 to 50.01    Longitude range: -125.21 to -65.34");

        // Sets up the boxes for user inputs with their descriptions to the left (i.e., "longitude:  (JTextField)")
        descriptionThreeLabel.setBounds(110, 120, 400, 20);
        JLabel latitudeLabel = new JLabel("Latitude (25.33 to 50.01): ");
        latitudeLabel.setBounds(140, 155, 200, 20);
        JTextField latitudeField = new JTextField(10);
        latitudeField.setBounds(340, 155, 100,20);
        JLabel longitudeLabel = new JLabel("Longitude (-125.21 to -65.34): ");
        longitudeLabel.setBounds(140,175,200,20);
        JTextField longitudeField = new JTextField(10);
        longitudeField.setBounds(340,175,100,20);
        JLabel dateLabel = new JLabel("Date (e.g., 2024-02-26): ");
        dateLabel.setBounds(140, 195, 200, 20);
        JTextField dateField = new JTextField(25);
        dateField.setBounds(340, 195, 100,20);

        // ActionListener for the Locate button. This will read in the user inputs and run the getDistance() and getTime()
        // functions. getDistance() finds the distance from the user to the fire centroid point, getTime() gives the
        // days away from the fire start date.
        // Uses a try-catch and produces JOptionPanes for errors as well as correct inputs
        locateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // User inputted data
                    double latValue = Double.parseDouble(latitudeField.getText());
                    double longValue = Double.parseDouble(longitudeField.getText());
                    String inputDateStr = String.valueOf(dateField.getText());

                    // Set the format for dateTime and get the CSV File and Scanner (to scan through the CSV file)
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate inputDate = LocalDate.parse(inputDateStr, formatter);
                    File csvFile = new File("FireData.csv");
                    Scanner scanner = new Scanner(csvFile);

                    // isSafe informs the user if they are safe from the fire or not, to initialize we set equal to true
                    boolean isSafe = true;

                    // Initialize a few other objects that are used to compare with the Fire Data and for Results
                    String closestFireId = "";
                    String closestFireName = "";
                    String estimatedTime = "";
                    long daysBetween = 0;
                    String fireCounty = "";
                    String fireState = "";
                    double closestDistance = 0;

                    // Reads through the CSV, ensures there is a nextLine for the data
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }

                    // While loop iterating through each of the fire rows, sets each row to an array list which can
                    // be indexed to get that specific fires' information
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] values = line.split(",");

                        // catches to ensure proper entries
                            try {
                                // Description objects of the actual fire (1 row of data)
                                double fireX = Double.parseDouble(values[5].trim());
                                double fireY = Double.parseDouble(values[6].trim());
                                LocalDate fireDate = LocalDate.parse(values[3].trim().split(" ")[0], formatter);
                                String fireIdentifier = values[9].trim();
                                String fireName = values[4].trim();
                                fireCounty = values[7].trim();
                                fireState = values[8].trim().replace("US-","");

                                // Initialize the calculateDistance class object to retrieve distance and time
                                calculateDistance t = new calculateDistance(latValue, longValue, fireY, fireX);
                                double distance = t.getDistance();
                                String time = t.getTime();
                                daysBetween = DAYS.between(inputDate, fireDate);

                                // if the user inputs a date before the fire and within a dangerous distance, and
                                // the fire is within the next 20 days
                                if (distance < SAFE_DISTANCE_KM && inputDate.isBefore(fireDate) == true && daysBetween < 20) {
                                    isSafe = false;
                                    closestDistance = distance;
                                    closestFireId = fireIdentifier;
                                    closestFireName = fireName;
                                    estimatedTime = time;
                                    break;
                                }
                            }
                            catch (DateTimeParseException | NumberFormatException var31) {
                                // This is an error with the parsing of data. If index or format was wrong it would
                                // return this message. Useful for future use of a different data/csv's
                                System.out.println("Error parsing data: " + var31.getMessage());
                            }
                    }

                    // outputs based on if the user isSafe or not
                    if (isSafe) {
                        // if the user is safe from fire
                        // reset the field boxes
                        latitudeField.setText("");
                        longitudeField.setText("");
                        dateField.setText("");

                        // uses a JOption with an image
                        final ImageIcon smokeyImg = new ImageIcon("smokey_the_bear3.jpg");
                        JOptionPane.showMessageDialog(null, "You are safe from nearby fires!",
                                "Smokey the Bear's Inspirational Message", JOptionPane.INFORMATION_MESSAGE, smokeyImg);
                    } else {
                        // if user is NOT safe
                        // reset the field boxes
                        latitudeField.setText("");
                        longitudeField.setText("");
                        dateField.setText("");

                        // uses a JOption with an image and gives the descriptive facts about the fire // user location
                        final ImageIcon fireImg = new ImageIcon("fire_image2.jpg");
                        JOptionPane.showMessageDialog(null, "You are dangerously close to " +
                                        "the fire.\nThe fire known as " + closestFireName + " starts only " +
                                        String.format("%.2f", (closestDistance)) + " km away.\n" + estimatedTime +
                                        "\nIt will take place in " + daysBetween + " day(s)" + " in " +
                                        fireCounty + ", " + fireState + "." +
                                        "\n\nFire ID: " + closestFireId,
                                "Smokey the Bear's Warning", JOptionPane.INFORMATION_MESSAGE, fireImg);
                    }

                    // Shows what the user last inputted at the bottom. Allows for ease of use with application
                    // above if-statements will clear out the JTextFields for user input, while this will remind user
                    // what they entered.
                    userInputs.setText(String.format("Latitude " + latValue + " Longitude " + longValue + " Date " +
                            inputDateStr));

                    // if non-integer inputted, this should rarely be used
                } catch (InputMismatchException exception) {
                    JOptionPane.showMessageDialog(null, "Exception found. Must input an " +
                            "integer, double for precision.");

                    // if non-integer inputted
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Exception found. Must input a number.");
                    // resets both boxes even if only one was incorrect
                    latitudeField.setText("");
                    longitudeField.setText("");

                    // this catch is mandatory for the reading of the CSV. If the file is not found, it will time out
                    // the code.
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);

                    // if date format is not followed or invalid date is entered
                } catch(DateTimeParseException ex) {
                    final ImageIcon dateImg = new ImageIcon("date_example.png");
                    JOptionPane.showMessageDialog(null, "Follow the date format (yyyy-MM-dd).\n" +
                                    "Dates that do not exist won't work either",
                            "Reading Comprehension Skills", JOptionPane.INFORMATION_MESSAGE, dateImg);
                    // resets the improper entry for the date field box
                    dateField.setText("");

                }
            }
        });


        // JFrame layout to null
        setLayout(null);

        // Add all the objects into the window
        add(titleLabel);
        add(coTitleLabel);
        add(leftLabel);
        add(userInputs);
        add(descriptionLabel);
        add(descriptionTwoLabel);
        add(descriptionThreeLabel);
        add(latitudeLabel);
        add(latitudeField);
        add(longitudeLabel);
        add(longitudeField);
        add(dateLabel);
        add(dateField);
        add(locateButton);

        // Setting JFrame size, visibility, and location to be the center of the screen
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Start the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckFire());
    }
}