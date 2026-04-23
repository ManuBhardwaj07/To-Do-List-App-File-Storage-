import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Task {
    String desc;
    boolean done;

    Task(String d, boolean done) {
        this.desc = d;
        this.done = done;
    }

    public String toString() {
        return (done ? "✔ " : "✘ ") + desc;
    }
}

public class TodoGUI {
    static DefaultListModel<Task> model = new DefaultListModel<>();
    static final String FILE = "tasks.txt";

    public static void main(String[] args) {
        loadTasks();

        JFrame frame = new JFrame("To-Do App");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField input = new JTextField();
        JButton addBtn = new JButton("Add Task");
        JButton doneBtn = new JButton("Mark Done");

        JList<Task> list = new JList<>(model);
        JScrollPane scroll = new JScrollPane(list);

        // Layout
        frame.setLayout(new BorderLayout());
        frame.add(input, BorderLayout.NORTH);
        frame.add(scroll, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.add(addBtn);
        panel.add(doneBtn);
        frame.add(panel, BorderLayout.SOUTH);

        // Add Task
        addBtn.addActionListener(e -> {
            String text = input.getText();
            if (!text.isEmpty()) {
                model.addElement(new Task(text, false));
                input.setText("");
                saveTasks();
            }
        });

        // Mark Complete
        doneBtn.addActionListener(e -> {
            int i = list.getSelectedIndex();
            if (i != -1) {
                Task t = model.get(i);
                t.done = true;
                list.repaint();
                saveTasks();
            }
        });

        frame.setVisible(true);
    }

    static void saveTasks() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(FILE))) {
            for (int i = 0; i < model.size(); i++) {
                Task t = model.get(i);
                w.write(t.desc + "|" + t.done);
                w.newLine();
            }
        } catch (Exception e) {}
    }

    static void loadTasks() {
        try (BufferedReader r = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split("\\|");
                model.addElement(new Task(p[0], Boolean.parseBoolean(p[1])));
            }
        } catch (Exception e) {}
    }
}