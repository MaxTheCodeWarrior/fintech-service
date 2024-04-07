package telran.fintechservice.accounting.dto;

public enum UserRoleEnum {
	USER("USER"),
    MODERATOR("MODERATOR"),
    ADMINISTRATOR("ADMINISTRATOR");
    

    private final String value;

    UserRoleEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}