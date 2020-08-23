package Parser;

import Command.Command;
import DukeException.DukeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Test
    public void inputTest(){
        try {
            Parser.input(Command.TODO, 1, true);
        } catch (DukeException ex){
            String expect = "HEY!!! Don't be stingy give MUG more information >.<";
            assertEquals(expect, ex.getMessage());
        }
    }

    @Test
    public void infoTest(){
        try {
            Parser.info(Command.TODO, "", true);
        } catch (DukeException ex){
            String expect = "HEY!!! Don't be stingy give MUG more information >.<";
            assertEquals(expect, ex.getMessage());
        }
    }

    @Test
    public void dateTest(){
        try {
            LocalDate actDate = Parser.date("2019-01-12");
            LocalDate expDate = LocalDate.parse("2019-01-12");
            assertEquals(expDate, actDate);
        } catch (DukeException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void commandTest(){
        try {
            Command command = Parser.command("todo");
            assertEquals(Command.TODO, command);
        } catch (DukeException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void indexTest(){
        try {
            int index = Parser.index("2", 4);
            assertEquals(2, index);
        } catch (DukeException ex){
            ex.printStackTrace();
        }
    }
}
