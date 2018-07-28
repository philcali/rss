package me.philcali.rss.dynamo.model;

import com.amazonaws.services.dynamodbv2.document.Item;

import me.philcali.rss.api.IUserSettings;

public class UserSettingsDynamo implements IUserSettings {
    public static final String EMAIL = "email";
    private static final String PHOTO = "photo";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private final Item item;

    public UserSettingsDynamo(final Item item) {
        this.item = item;
    }

    public static Item toItem(final IUserSettings settings) {
        return new Item()
                .withString(EMAIL, settings.getEmail())
                .withString(PHOTO, settings.getPhoto())
                .withString(FIRST_NAME, settings.getFirstName())
                .withString(LAST_NAME, settings.getLastName());
    }

    @Override
    public String getEmail() {
        return item.getString(EMAIL);
    }

    @Override
    public String getPhoto() {
        return item.getString(PHOTO);
    }

    @Override
    public String getFirstName() {
        return item.getString(FIRST_NAME);
    }

    @Override
    public String getLastName() {
        return item.getString(LAST_NAME);
    }
}
