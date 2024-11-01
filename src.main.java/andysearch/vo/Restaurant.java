package andysearch.vo;

public class Restaurant {

    private int restaurantId;
    private String restaurantName;
    private String address;
    private int totalScores;
    private int totalReview;
    private double latitude;
    private double longitude;
    private String restaurantLabel;
    private String openingHours;
    private String homePhone;
    private Integer priceRangeMax;
    private Integer priceRangeMin;
    private String email;
    private Integer serviceCharge;

    // Constructors
    public Restaurant() {}

    public Restaurant(int restaurantId, String restaurantName, String address, int totalScores, int totalReview,
                      double latitude, double longitude, String restaurantLabel, String openingHours, 
                      String homePhone, Integer priceRangeMax, Integer priceRangeMin, 
                      String email, Integer serviceCharge) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.address = address;
        this.totalScores = totalScores;
        this.totalReview = totalReview;
        this.latitude = latitude;
        this.longitude = longitude;
        this.restaurantLabel = restaurantLabel;
        this.openingHours = openingHours;
        this.homePhone = homePhone;
        this.priceRangeMax = priceRangeMax;
        this.priceRangeMin = priceRangeMin;
        this.email = email;
        this.serviceCharge = serviceCharge;
    }

    // Getters and Setters
    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalScores() {
        return totalScores;
    }

    public void setTotalScores(int totalScores) {
        this.totalScores = totalScores;
    }

    public int getTotalReview() {
        return totalReview;
    }

    public void setTotalReview(int totalReview) {
        this.totalReview = totalReview;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRestaurantLabel() {
        return restaurantLabel;
    }

    public void setRestaurantLabel(String restaurantLabel) {
        this.restaurantLabel = restaurantLabel;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public Integer getPriceRangeMax() {
        return priceRangeMax;
    }

    public void setPriceRangeMax(Integer priceRangeMax) {
        this.priceRangeMax = priceRangeMax;
    }

    public Integer getPriceRangeMin() {
        return priceRangeMin;
    }

    public void setPriceRangeMin(Integer priceRangeMin) {
        this.priceRangeMin = priceRangeMin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Integer serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    // toString method for easy output
    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", address='" + address + '\'' +
                ", totalScores=" + totalScores +
                ", totalReview=" + totalReview +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", restaurantLabel='" + restaurantLabel + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", priceRangeMax=" + priceRangeMax +
                ", priceRangeMin=" + priceRangeMin +
                ", email='" + email + '\'' +
                ", serviceCharge=" + serviceCharge +
                '}';
    }
}
