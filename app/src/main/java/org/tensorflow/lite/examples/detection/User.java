package org.tensorflow.lite.examples.detection;

public class User {
    String displayName,email,photo;

    public User(){}

    public User(String displayName, String email, String photo) {
        this.displayName = displayName;
        this.email = email;
        this.photo = photo;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
