package com.care360.findmyfamilyandfriends.HomeScreen.ui.FragmentSafety.EmergencyContacts.ContactsFromPhone;

public class ContactModel {

    private String contactId;
    private String contactName;
    private String contactNumber;

    public ContactModel() {}

    public ContactModel(String contactId, String contactName, String contactNumber) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
