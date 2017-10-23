package gbpec.comida.reciever_module;

/**
 * Created by Parva Singhal on 20-10-2017.
 */

public class Food {
    private String donor;
    private String contact;
    private String details;
    private String pickupTime;
    private String validDate;
    private String validTime;

    public Food(String donor, String contact, String details, String pickupTime, String validDate, String validTime) {
        this.donor = donor;
        this.contact = contact;
        this.details = details;
        this.pickupTime = pickupTime;
        this.validDate = validDate;
        this.validTime = validTime;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }



    public Food() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDonor() {
        return donor;

    }

    public void setDonor(String donor) {
        this.donor = donor;
    }


}