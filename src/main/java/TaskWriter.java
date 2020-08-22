import java.io.*;
import java.time.LocalDate;
import java.util.Scanner;


public class TaskWriter {
    String filepath;

    TaskWriter(String filepath) {
        this.filepath = filepath;
    }

    private Task taskCreator(Command command, boolean isDone, String info, String dTimeSplitter, String eTimeSplitter)
            throws DukeException {
        Task newTask = null;
        switch (command) {
            case TODO:
                newTask = new Todo(info, isDone);
                break;
            case DEADLINE:
                String[] dInfo = info.split(dTimeSplitter);
                Validator.info(command, dInfo.length, true);
                String deadlineEvent = dInfo[0];
                LocalDate deadlineTime = Validator.date(dInfo[1]);
                newTask = new Deadline(deadlineEvent, deadlineTime, isDone);
                break;
            case EVENT:
                String[] eInfo = info.split(eTimeSplitter);
                Validator.info(command, eInfo.length, true);
                String eventEvent = eInfo[0];
                LocalDate eventTime = Validator.date(eInfo[1]);
                newTask = new Event(eventEvent, eventTime, isDone);
                break;
            default:
                break;
        }
        return newTask;
    }

    public String appendTask(Command command, String info) throws IOException {
        try {
            FileWriter fw = new FileWriter(this.filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Task newTask = taskCreator(command, false, info, " /by ", " /at ");

            switch(command) {
                case TODO:
                    pw.println("TODO|0|" + info);
                    break;
                case DEADLINE:
                    String[] dInfo = info.split(" /by ");
                    Validator.info(command, dInfo.length, true);
                    String deadlineEvent = dInfo[0];
                    LocalDate deadlineTime = Validator.date(dInfo[1]);
                    pw.println("DEADLINE|0|" + deadlineEvent + "|" + deadlineTime);
                    break;
                case EVENT:
                    String[] eInfo = info.split(" /at ");
                    Validator.info(command, eInfo.length, true);
                    String eventEvent = eInfo[0];
                    LocalDate eventTime = Validator.date(eInfo[1]);
                    pw.println("EVENT|0|" + eventEvent + "|" + eventTime);
                    break;
                default:
                    break;
            }

            pw.flush();
            pw.close();

            return "Got it. MUG has added this task:\n"
                    + newTask;
        } catch (DukeException ex){
            return ex.getMessage();
        } catch (IOException ex) {
            throw new IOException("Something went wrong. MUG fail to add the Task :_:");
        }
    }

    public String deleteTask(int taskId) throws IOException {
        String tempFile = "temp.txt";
        File oldFile = new File(this.filepath);
        File newFile = new File(tempFile);
        int taskTrack = 0;
        String line;
        Task deleteTask = null;

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner sc = new Scanner(new File(this.filepath));
            sc.useDelimiter("[\n]");

            while(sc.hasNext()) {
                line = sc.next();
                taskTrack++;
                if(taskTrack != taskId) {
                    pw.println(line);
                } else {
                    String[] newLine = line.split("[|]" , 3);
                    boolean isDone = Integer.parseInt(newLine[1]) == 1;
                    Command command = Validator.command(newLine[0]);
                    deleteTask = taskCreator(command, isDone, newLine[2], "[|]", "[|]");
                }
            }
            sc.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File renameFile = new File(this.filepath);
            newFile.renameTo(renameFile);
        } catch (DukeException ex){
            return ex.getMessage();
        } catch (IOException ex) {
            throw new IOException("Something went wrong. MUG fail to delete the Task :_:");
        }

        if (taskId > taskTrack) {
            return "MUG don't have this task to Delete @_@";
        } else {
            return "Noted. MUG has removed this task:\n"
                    + deleteTask;
        }
    }

    public String doneTask(int taskId) {
        String tempFile = "temp.txt";
        File oldFile = new File(this.filepath);
        File newFile = new File(tempFile);
        int taskTrack = 0;
        String line;
        Task doneTask = null;
        boolean marked = false;

        try {
            FileWriter fw = new FileWriter(tempFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            Scanner sc = new Scanner(new File(this.filepath));
            sc.useDelimiter("[\n]");

            while(sc.hasNext()) {
                line = sc.next();
                taskTrack++;
                if(taskTrack != taskId) {
                    pw.println(line);
                } else {
                    String[] newLine = line.split("[|]" , 3);
                    marked = Integer.parseInt(newLine[1]) == 1;
                    pw.println(newLine[0] + "|" + 1 + "|" + newLine[2] );
                    Command command = Validator.command(newLine[0]);
                    doneTask = taskCreator(command, true, newLine[2], "[|]", "[|]");
                }
            }
            sc.close();
            pw.flush();
            pw.close();
            oldFile.delete();
            File renameFile = new File(this.filepath);
            newFile.renameTo(renameFile);
        } catch (DukeException ex){
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (taskId > taskTrack) {
            return "MUG don't have this task to mark as Done :>";
        } else if(marked) {
            return "MUG had marked this task as done:\n"
                    + doneTask;
        } else {
            return "Congratz! MUG has marked this task as done:\n"
                    + doneTask;
        }
    }

    public String readTask() throws FileNotFoundException, DukeException {
        Scanner sc = new Scanner(new File(this.filepath));
        sc.useDelimiter("[\n]");
        String line;
        StringBuilder results = new StringBuilder();
        int taskId = 0;

        while (sc.hasNext()) {
            line = sc.next();
            String[] newLine = line.split("[|]");
            Command command = Validator.command(newLine[0]);
            boolean taskStatus = Integer.parseInt(newLine[1]) == 1;
            taskId++;

            if(taskId != 1) {
                results.append("\n");
            }
            results.append(taskId);
            results.append(". ");

            if (command == Command.TODO) {
                results.append(new Todo(newLine[2], taskStatus));
            } else if (command == Command.DEADLINE) {
                LocalDate date = Validator.date(newLine[3]);
                results.append(new Deadline(newLine[2], date, taskStatus));
            } else if (command == Command.EVENT) {
                LocalDate date = Validator.date(newLine[3]);
                results.append(new Event(newLine[2], date, taskStatus));
            }
        }

        if (taskId == 0) {
            return "MUG don't have any of your task \"_\"";
        } else {
            return results.toString();
        }
    }
}