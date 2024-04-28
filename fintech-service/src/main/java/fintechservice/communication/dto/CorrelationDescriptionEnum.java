package fintechservice.communication.dto;

public enum CorrelationDescriptionEnum {
    PERFECT_POSITIVE("Perfect positive correlation"),
    VERY_STRONG_POSITIVE("Very strong positive correlation"),
    STRONG_POSITIVE("Strong positive correlation"),
    MODERATE_POSITIVE("Moderate positive correlation"),
    WEAK_POSITIVE("Weak positive correlation"),
    NEGLIGIBLE_POSITIVE("Negligible positive correlation"),
    PERFECT_NEGATIVE("Perfect negative correlation"),
    VERY_STRONG_NEGATIVE("Very strong negative correlation"),
    STRONG_NEGATIVE("Strong negative correlation"),
    MODERATE_NEGATIVE("Moderate negative correlation"),
    WEAK_NEGATIVE("Weak negative correlation"),
    NEGLIGIBLE_NEGATIVE("Negligible negative correlation"),
    NO_CORRELATION("No correlation");

    private final String description;

    CorrelationDescriptionEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
