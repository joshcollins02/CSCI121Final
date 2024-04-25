Fire Prediction System
Created by: Harry Leduc & Josh Collins

Instructions:
For system - Enter a latitude, longitude, and date in the JFrame window.
             once inputted the system will return a message "either safe from fire, or dangerously close to fire"

Function:
For system - Reads a csv file and parses through the array list to find name, date, coordinates, county, and state
             Creates a JFrame prompting user input for coordinates and date
             Takes input coordinates and loops through fire coordinates, then comparing them through a haversine formula that
             takes the distance between the two using geodesic calculation that uses the curvature of the earth as the crow flies.
             Takes distance from user input and center of fire, using average forest fire spread rate, gives the user the approx
             time for fire to reach their location.
             To determine if user is not safe, ensures distance from fire is less than 200KM from fire start point and the user input
             date is before the fire date and days between input date and fire date is less than 20.

Creativity:
Catches - Shows JOptionPanes with images when the user inputs non-integers, nothing, or something
          besides the proper date format. It also resets the box when the wrong date format is inputted. Or a non-integer
          Do not use a JOptionPane if the file is not found because people running it should have the FireData.csv
          file in their project within IntelliJ

Future adjustments:
Adaptions - Allow users to enter the other types of coordinates and have the code manually calculate // rearrange
            the coordinates into the simple decimal system. i.e., Degrees/Minutes/Seconds -> decimals
            Create a formula that can use machine learning to guess where a fire will be in the future based on relative
            location and date of past data

Issues:
Data - The data does not have a reported fire end date, so in order to make 99% of the fires work, we have it set
       that if the fire occurred and it's the newest / closest fire, then the user will be "in danger". However,
       this isn't good because if a fire is the newest fire in the data set, you could put a date one year down the
       line and it was still tell the user they are "in danger", which is unrealistic.  We considered using a baselin
       of 10 days, so after 10 days the user would simply be safe. However, we had difficulty making that function
       and some fires do last a month.

Sources:
Wild Fire Spread Rate - https://wfca.com/wildfire-articles/how-fast-do-wildfires-spread/#:~:text=Wildfires%20spread%20at%
                        20an%20average%20of%2014.27%20miles,such%20as%20weather%20conditions%2C%20fuel%20type%2C%20and%20terrain
Haversine Distance Formula - https://community.esri.com/t5/coordinate-reference-systems-blog/distance-on-a-sphere-the-
                             haversine-formula/ba-p/902128#:~:text=All%20of%20these%20can%20be,longitude%20of%20the%20two%20points

                             Cuts the circle into a plain
                             has a lat phi and lat theta
                             has a long phi and long theta
                             you find theta in terms of phi and lamda -> (c) finds the angle of theta by using artan(x/y)

                             Once you cut the sphere into a circle all you have to do is find the theta in terms of latitude and longitude
                             and when you have the angle theta, then theta times R gets you the distance
