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

public class CheckFire extends JFrame {
    JLabel userInputs;
    private static final double SAFE_DISTANCE_KM = 200.0;
    private static final double FIRE_RADIUS_KM = 10.0;

    // This handles the called JLabels, Buttons, and Checkboxes and orients them on the window
    public CheckFire() {
        setTitle("Death by Fire (or Bear)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton locateButton = new JButton("Locate");
        locateButton.setBounds(230, 300, 120, 30);
        JLabel titleLabel = new JLabel("Past Forest Fire Database");
        titleLabel.setBounds(220, 0, 200, 20);
        JLabel coTitleLabel = new JLabel("Are you safe?");
        coTitleLabel.setBounds(260, 20, 200, 20);


        JLabel leftLabel = new JLabel("---------------------------------------------------------" +
                "----------------------------------");
        leftLabel.setBounds(110, 40, 500, 20);


        // update with a variable that points to whatever number is inputted by the user
        userInputs = new JLabel("Latitude " + " " + " Longitude " + " ");
        userInputs.setBounds(160, 250, 300, 20);

        JLabel descriptionLabel = new JLabel("Input the coordinates of a location within the continental " +
                "United");
        descriptionLabel.setBounds(110, 70, 400, 20);
        JLabel descriptionTwoLabel = new JLabel("States. Include the date. Use simple decimal coordinates.");
        descriptionTwoLabel.setBounds(110,90,400, 20);
        JLabel descriptionThreeLabel = new JLabel("Latitude range: " +
                "25.33 to 50.01    Longitude range: -125.21 to -65.34");
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

        locateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    double latValue = Double.parseDouble(latitudeField.getText());
                    double longValue = Double.parseDouble(longitudeField.getText());
                    String inputDateStr = String.valueOf(dateField.getText());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate inputDate = LocalDate.parse(inputDateStr, formatter);
                    File csvFile = new File("FireData.csv");
                    Scanner scanner = new Scanner(csvFile);
                    boolean isSafe = true;
                    String closestFireId = "";
                    String closestFireName = "";
                    double closestDistance = Double.MAX_VALUE;
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }

                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] values = line.split(",");
                        if (values.length >= 10) {
                            try {
                                double fireX = Double.parseDouble(values[5].trim());
                                double fireY = Double.parseDouble(values[6].trim());
                                LocalDate fireDate = LocalDate.parse(values[3].trim().split(" ")[0], formatter);
                                String fireIdentifier = values[9].trim();
                                String fireName = values[4].trim();
                                calculateDistance t = new calculateDistance(latValue, longValue, fireY, fireX);
                                double distance = t.getDistance();
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
                                // this is an error with the data. Will only do a system print since it is unlikely
                                // a user of the GUI would be fixing the actual fire data
                                System.out.println("Error parsing data: " + var31.getMessage());
                            }
                        }
                    }

                    if (isSafe) {
                        System.out.println("You are safe from nearby fires.");
                    } else {
                        System.out.printf("You are dangerously close to %s - %s, which is only %.2f km away.\n", closestFireId, closestFireName, closestDistance);
                    }

                    userInputs.setText(String.format("Latitude " + latValue + " Longitude " + longValue + " Date " +
                            inputDateStr));
                } catch (InputMismatchException exception) {
                    JOptionPane.showMessageDialog(null, "Exception found. Must input an " +
                            "integer, double for precision.");
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Exception found. Must input a number.");
                    // resets both boxes even if only one was incorrect (potential future adjustment to the project)
                    latitudeField.setText("");
                    longitudeField.setText("");
                } catch (FileNotFoundException ex) {
                    // this catch is mandatory for the reading of the CSV. If the file is not found, it will time out
                    // the code.
                    throw new RuntimeException(ex);
                } catch(DateTimeParseException ex) {
                    final ImageIcon dateImg = new ImageIcon("date_example.png");
                    JOptionPane.showMessageDialog(null, "Hey! Let's try reading the " +
                                    "instructions!  \nGo ahead... try putting the date in the proper format :)",
                            "Lacking Reading Comprehension Skills", JOptionPane.INFORMATION_MESSAGE, dateImg);
                    // resets the improper entry
                    dateField.setText("");

                }
            }
        });


        setLayout(null);

        // Here is just adding all the features above into the window
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

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckFire());
    }
}


// Creativity:
// catches - shows JOptionPanes with images when the user inputs non-integers, nothing, or something
//           besides the proper date format. It also resets the box when the wrong date format is inputted. Or a non-integer
//           Do not use a JOptionPane if the file is not found because people running it should have the FireData.csv
//           file in their project within IntelliJ