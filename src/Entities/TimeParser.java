package Entities;
import java.time.*;
import java.time.temporal.*;
import java.io.*;

/** Text parser helper class. Responsible for making sense of string dates and generating IDs to assign.
 * @author Richard Yin
 */
public class TimeParser {

    /** Take in and understand User time input.
     * @param time String input of time
     */
    public LocalDateTime parseTime(String time) throws DateTimeException {
        // YYYY/MM/DD HH:MM
        if (time.matches("^[0-9]{4}\\/[01][0-9]\\/[0-3][0-9] [0-2][0-9]:[0-5][0-9]$"))
            return YYYYMMDDHHMM(time);
        // MM/DD HH:MM
        else if (time.matches("^[01][0-9]\\/[0-3][0-9] [0-2][0-9]:[0-5][0-9]$"))
            return MMDDHHMM(time);
        // DD HH:MM
        else if (time.matches("^[0-3][0-9] [0-2][0-9]:[0-5][0-9]$"))
            return DDHHMM(time);
        // YYYY/MM/DD HH
        else if (time.matches("^[0-9]{4}\\/[01][0-9]\\/[0-3][0-9] [0-2][0-9]$"))
            return YYYYMMDDHH(time);
        // YYYY/MM/DD
        else if (time.matches("^[0-9]{4}\\/[01][0-9]\\/[0-3][0-9]$"))
            return YYYYMMDD(time);
        // MM/DD
        else if (time.matches("^[01][0-9]\\/[0-3][0-9]$"))
            return MMDD(time);
        // DD
        else if (time.matches("^[0-3][0-9]$"))
            return DD(time);
        // Tomorrow
        else if (time.equalsIgnoreCase("tomorrow"))
            return tomorrow();
        // Next day of the week
        else if (time.matches("^([mM]on(day)?)|([tT]ue(s(day)?)?)|([wW]ed(nesday)?)|([tT]hu(r(sday)?)?)|([fF]ri(day)?)|([Ss]at(urday)?)|([Ss]un(day)?)$"))
            return nextDay(time);
        else
            throw new DateTimeException(time);
    }
    private LocalDateTime YYYYMMDDHHMM(String time) throws DateTimeException {
        int year = intSlice(time, 0, 4);
        int month = intSlice(time, 5, 7);
        int day = intSlice(time, 8, 10);
        int hour = intSlice(time, 11, 13);
        int minute = intSlice(time, 14, 16);
        return LocalDateTime.of(year, month, day, hour, minute);
    }
    private LocalDateTime MMDDHHMM(String time) throws DateTimeException {
        int month = intSlice(time, 0, 2);
        int day = intSlice(time, 3, 5);
        int hour = intSlice(time, 6, 8);
        int minute = intSlice(time, 9, 11);
        return LocalDateTime.of(LocalDateTime.now().getYear(), month, day, hour, minute);
    }
    private LocalDateTime DDHHMM(String time) throws DateTimeException {
        int day = intSlice(time, 0, 2);
        int hour = intSlice(time, 3, 5);
        int minute = intSlice(time, 6, 8);
        return LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), day, hour, minute);
    }
    private LocalDateTime YYYYMMDDHH(String time) throws DateTimeException {
        int year = intSlice(time, 0, 4);
        int month = intSlice(time, 5, 7);
        int day = intSlice(time, 8, 10);
        int hour = intSlice(time, 11, 13);
        return LocalDateTime.of(year, month, day, hour, 0);
    }
    private LocalDateTime YYYYMMDD(String time) throws DateTimeException {
        int year = intSlice(time, 0, 4);
        int month = intSlice(time, 5, 7);
        int day = intSlice(time, 8, 10);
        return LocalDateTime.of(year, month, day, 0, 0);
    }
    private LocalDateTime MMDD(String time) throws DateTimeException {
        int month = intSlice(time, 0, 2);
        int day = intSlice(time, 3, 5);
        return LocalDateTime.of(LocalDate.now().getYear(), month, day, 0, 0);
    }
    private LocalDateTime DD(String time) throws DateTimeException {
        int day = Integer.parseInt(time);
        return LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), day, 0, 0);
    }
    private int intSlice(String text, int i, int j) {
        return Integer.parseInt(text.substring(i, j));
    }
    private LocalDateTime tomorrow() {
        LocalDateTime t = LocalDateTime.now().plusDays(1);
        return LocalDateTime.of(t.getYear(), t.getMonthValue(), t.getDayOfMonth(), 0, 0);
    }
    private LocalDateTime nextDay(String time) {
        if (time.matches("^[mM]on(day)?$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        else if (time.matches("^[tT]ue(s(day)?)?$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.TUESDAY));
        else if (time.matches("^[wW]ed(nesday)?$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.WEDNESDAY));
        else if (time.matches("^[tT]hu(r(sday)?)?$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.THURSDAY));
        else if (time.matches("^([fF]ri(day)?)$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
        else if (time.matches("^[Ss]at(urday)?$"))
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SATURDAY));
        else
            return LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    }
}
