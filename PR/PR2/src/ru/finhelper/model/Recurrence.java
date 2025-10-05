package src.ru.finhelper.model;

public enum Recurrence {
    NONE("Одноразовый"),
    MONTHLY("Ежемесячный"),
    YEARLY("Ежегодный");

    private final String displayName;

    Recurrence(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}