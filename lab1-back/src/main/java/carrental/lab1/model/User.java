package carrental.lab1.model;

import java.util.Objects;

public final class User {
    private final long passportId;
    private final String name;
    private final String password;
    private final boolean isAdmin;

    public User(long passportId, String name, String password, boolean isAdmin) {
        this.passportId = passportId;
        this.name = name;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public long getPassportId() {
        return passportId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        return "User{" +
                "passportId=" + passportId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return passportId == user.passportId && isAdmin == user.isAdmin && name.equals(user.name) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportId, name, password, isAdmin);
    }
}
