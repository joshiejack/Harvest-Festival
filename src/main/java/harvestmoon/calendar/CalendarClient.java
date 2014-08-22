package harvestmoon.calendar;


public class CalendarClient {
    private static CalendarDate date = new CalendarDate(1, Season.SPRING, 1);
    
    public CalendarDate getDate() {
        return date;
    }
}
