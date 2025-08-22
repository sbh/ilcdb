
public enum StatusType
{
	DACA("Deferred Action", "daca"), LPR("Legal Permanent Resident", "lpr"), TPS("Temporary Protected Status", "tps"), CITIZEN("Citizen", "citizen")
    
    private final String description
    private final String datePickerWidgetPrefix

    private StatusType(String description, String datePickerWidgetPrefix)
    {
        this.description = description
        this.datePickerWidgetPrefix = datePickerWidgetPrefix
    }
}
