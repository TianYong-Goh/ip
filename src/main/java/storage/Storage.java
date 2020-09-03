package storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import command.Command;
import mugexception.MugException;
import parser.Parser;
import tasks.Deadline;
import tasks.Event;
import tasks.Task;
import tasks.Todo;

/**
 * Operations on local file.
 */
public class Storage {

    /** File's path */
    private final String filepath;

    /**
     * Constructs a Storage object(file) at given path.
     *
     * @param filepath File's path
     */
    public Storage(String filepath) {
        this.filepath = filepath;
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException ex) {
            System.out.println("An error occurred!!");
            ex.printStackTrace();
        }
    }

    /**
     * Loads Tasks from the file to arraylist of Task.
     *
     * @return Arraylist of Task.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> taskList = new ArrayList<>();
        try {
            Scanner sc = new Scanner(new File(this.filepath));
            sc.useDelimiter("[\n]");
            String line;

            while (sc.hasNext()) {
                line = sc.next();
                String[] newLine = line.split("[|]");
                Command command = Parser.command(newLine[0]);
                boolean hasDone = Integer.parseInt(newLine[1]) == 1;

                if (command == Command.TODO) {
                    taskList.add(new Todo(newLine[2], hasDone));
                } else if (command == Command.DEADLINE) {
                    LocalDate date = Parser.date(newLine[3]);
                    taskList.add(new Deadline(newLine[2], date, hasDone));
                } else if (command == Command.EVENT) {
                    LocalDate date = Parser.date(newLine[3]);
                    taskList.add(new Event(newLine[2], date, hasDone));
                }
            }

        } catch (FileNotFoundException | MugException ex) {
            System.out.println("WARNING: " + ex.getMessage() + " :WARNING");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("WARNING: There is Something wrong with your Storage :WARNING");
        }
        return taskList;
    }

    /**
     * Adds Task to local file.
     *
     * @param command User command.
     * @param info Task description.
     * @throws MugException When MugException cause by other method.
     */
    public void appendTask(Command command, String info) throws MugException {
        try {
            FileWriter fw = new FileWriter(this.filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            switch (command) {
            case TODO:
                pw.println("TODO|0|" + info);
                break;
            case DEADLINE:
                String[] deadlineInfo = info.split(" /by ");
                Parser.input(command, deadlineInfo.length, true);
                Parser.info(command, deadlineInfo[1], true);
                String deadlineEvent = deadlineInfo[0];
                LocalDate deadlineTime = Parser.date(deadlineInfo[1]);
                pw.println("DEADLINE|0|" + deadlineEvent + "|" + deadlineTime);
                break;
            case EVENT:
                String[] eventInfo = info.split(" /at ");
                Parser.input(command, eventInfo.length, true);
                Parser.info(command, eventInfo[1], true);
                String eventEvent = eventInfo[0];
                LocalDate eventTime = Parser.date(eventInfo[1]);
                pw.println("EVENT|0|" + eventEvent + "|" + eventTime);
                break;
            default:
                break;
            }

            pw.flush();
            pw.close();

        } catch (MugException ex) {
            throw new MugException(ex.getMessage());
        } catch (IOException ex) {
            throw new MugException("Something went wrong. Mug fail to add the Tasks.Task :_:");
        }
    }

    /**
     * Deletes Task from local file.
     *
     * @param taskId Task index
     * @throws MugException When MugException cause by other method.
     */
    public void deleteTask(int taskId) throws MugException {
        String tempFile = "temp.txt";
        File oldFile = new File(this.filepath);
        File newFile = new File(tempFile);
        int taskTrack = 0;
        String line;

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner sc = new Scanner(new File(this.filepath));
            sc.useDelimiter("[\n]");

            while (sc.hasNext()) {
                line = sc.next();
                taskTrack++;
                if (taskTrack != taskId) {
                    pw.println(line);
                }
            }

            sc.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File renameFile = new File(this.filepath);
            newFile.renameTo(renameFile);
        } catch (IOException ex) {
            throw new MugException("Something went wrong. Mug fail to delete the Tasks.Task :_:");
        }
    }

    /**
     * Marks Task done in local file.
     *
     * @param taskId Task index.
     * @throws MugException When MugException cause by other method.
     */
    public void doneTask(int taskId) throws MugException {
        String tempFile = "temp.txt";
        File oldFile = new File(this.filepath);
        File newFile = new File(tempFile);
        int taskTrack = 0;
        String line;

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner sc = new Scanner(new File(this.filepath));
            sc.useDelimiter("[\n]");

            while (sc.hasNext()) {
                line = sc.next();
                taskTrack++;
                if (taskTrack != taskId) {
                    pw.println(line);
                } else {
                    String[] newLine = line.split("[|]" , 3);
                    pw.println(newLine[0] + "|" + 1 + "|" + newLine[2]);
                }
            }
            sc.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File renameFile = new File(this.filepath);
            newFile.renameTo(renameFile);
        } catch (FileNotFoundException e) {
            throw new MugException("File not found");
        } catch (IOException e) {
            throw new MugException("Something went wrong");
        }

    }
}