package me.philcali.rss.api.model;

import me.philcali.rss.api.IUserSettings;

public class UserSettings implements IUserSettings {
    private String email;
    private String photo;
    private String firstName;
    private String lastName;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPhoto() {
        return photo;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setPhoto(final String photo) {
        this.photo = photo;
    }
}
