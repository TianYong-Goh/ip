package Parser;

import Command.Command;
import DukeException.DukeException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {

    public static void input(Command command, int splitNum, boolean timeRelated) throws DukeException {
        if (splitNum < 2 ) {
            if ((timeRelated && command == Command.DEADLINE)
            ) {
                throw new DukeException("HEY!!! Feed me with {/by [date]}. MUG is hungry T_T");
            } else if (timeRelated
                    && command == Command.EVENT) {
                throw new DukeException("HEY!!! Feed me with {/at [date]}. MUG is hungry T_T");
            } else {
                throw new DukeException("HEY!!! Don't be stingy give MUG more information >.<");
            }
        }
    }

    public static void info(Command command, String info, boolean timeRelated) throws DukeException {
        if (info.trim().equals("")) {
            if ((timeRelated && command == Command.DEADLINE)
            ) {
                throw new DukeException("HEY!!! Feed me with {/by [date]}. MUG is hungry T_T");
            } else if (timeRelated
                    && command == Command.EVENT) {
                throw new DukeException("HEY!!! Feed me with {/at [date]}. MUG is hungry T_T");
            } else {
                throw new DukeException("HEY!!! Don't be stingy give MUG more information >.<");
            }
        }
    }

    public static LocalDate date(String date) throws DukeException {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new DukeException("MUG is picky. Give him the correct date format(YYYY-MM-DD) XD");
        }
    }

    public static Command command(String command) throws DukeException {
        try {
            return Command.valueOf(command.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new DukeException("Hey!!! I'm sorry, but MUG don't know what that means :-()");
        }
    }

    public static int index(String strIndex, int splitNum) throws DukeException {
        try {
            int index = Integer.parseInt(strIndex);

            if (splitNum < 2) {
                throw new DukeException("HEY!!! Don't be stingy give MUG more information >.<");
            }
            return index;
        } catch ( NumberFormatException ex) {
            throw new DukeException("Please feed MUG an integer number ~_~");
        }
    }
}

