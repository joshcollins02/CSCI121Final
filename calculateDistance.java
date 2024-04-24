// Refer to README for formula sources
public class calculateDistance {
    private double lat1;
    private double lon1;
    private double lat2;
    private double lon2;

    // constructor
    public calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
    }

    // Uses the Haversine formula. The Haversine Formula utilizes trigonometry to measure the distance between
    // two points.
    public double getDistance() {
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

    // Gives the approximate time for the fire to reach the user
    public String getTime() {
        double dist = getDistance();
        double hours = (dist / 22.97); //kmh for average forest fire spread
        int mins =  (int) Math.floor(hours * 60);

        // gives different returns whether the fire is over an hour away or not
        if (mins / 60 >= 1) {
            int hoursT = (int) Math.floor(mins / 60);
            int minsT = (mins % 60);
            return "The fire will take approximately " + hoursT + " hour(s) and " +
                    minsT + " minutes to get to your location.";
        }
        return "The fire will take approximately " + mins + " minutes to get to your location.";
    }
}
