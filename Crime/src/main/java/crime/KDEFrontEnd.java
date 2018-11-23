package ie.tcd.kde;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class KDEFrontEnd {

    private ButtonGroup radioGroup;
    private JRadioButton qs1, qs2, qs3, qs4, qs5, qs6, qs7, qs8, qs9, qs10;
    private Button button;
    private JPanel panel1, panel2;
    private JFrame frame;
    private JScrollPane scrollPane1, scrollPane2;
    private JLabel myLabel;


    public static void main(String s[]) {

        KDEFrontEnd kde = new KDEFrontEnd();
        kde.createView();

    }

    private void createView() {
        frame = new JFrame("Knowledge And Data Engineering");

        createPanel1();
        createPanel2();

        frame.add(scrollPane1, BorderLayout.NORTH);
        frame.add(scrollPane2, BorderLayout.CENTER);
        frame.setSize(2160, 720);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    private void createPanel2() {
        panel2 = new JPanel();
//        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

//        new Layout

        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));

        myLabel = new JLabel();


        scrollPane2 = new JScrollPane(panel2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  //Let all scrollPanel has scroll bars
        scrollPane2.setPreferredSize(new Dimension(2160, 360));

        myLabel.setText("Nothing to Show");
//        myLabel.setTextAlignment(JXLabel.TextAlignment.CENTER);

        panel2.add(myLabel);

    }

    private void createPanel1() {
        panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

        panel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel1.setAlignmentY(Component.CENTER_ALIGNMENT);

        scrollPane1 = new JScrollPane(panel1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);  //Let all scrollPanel has scroll bars
        scrollPane1.setPreferredSize(new Dimension(2160, 360));

//        JLabel label = new JLabel("This is a label!");
//
//        JButton button = new JButton();
//        button.setText("Press me");
//
//        panel1.add(label);
//        panel1.add(button);

        radioGroup = new ButtonGroup();


        qs1 = new JRadioButton("The station with it's county having highest number of burglaries reported.");
        qs1.setSelected(true);
        radioGroup.add(qs1);
        panel1.add(qs1);

        qs2 = new JRadioButton("The station with it's county having highest number of murders reported.");
        radioGroup.add(qs2);
        panel1.add(qs2);

        qs3 = new JRadioButton("The station with it's county having highest number of thefts reported. ");
        radioGroup.add(qs3);
        panel1.add(qs3);

        qs4 = new JRadioButton("The station with it's county highest number of Dangerous crimes reported.");
        radioGroup.add(qs4);
        panel1.add(qs4);

        qs5 = new JRadioButton("The number of stations in a county.");
        radioGroup.add(qs5);
        panel1.add(qs5);

        qs6 = new JRadioButton("The list of all stations with their county and the total number of crimes reported in descending order.");
        radioGroup.add(qs6);
        panel1.add(qs6);

        qs7 = new JRadioButton("The list of station their county and the type of crimes reported.");
        radioGroup.add(qs7);
        panel1.add(qs7);

        qs8 = new JRadioButton("The list of county and the number of counties adjacent to them.");
        radioGroup.add(qs8);
        panel1.add(qs8);

        qs9 = new JRadioButton("The name of the county in English and Gailic");
        radioGroup.add(qs9);
        panel1.add(qs9);

        qs10 = new JRadioButton("The station with second most total crimes reported.");
        radioGroup.add(qs10);
        panel1.add(qs10);

        button = new Button("Query  And Get Results");
        button.setMaximumSize(new Dimension(360, 50));
        panel1.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (qs1.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(0)));
                } else if (qs2.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(1)));
                } else if (qs3.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(2)));
                } else if (qs4.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(3)));
                } else if (qs5.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(4)));
                } else if (qs6.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(5)));
                } else if (qs7.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(6)));
                } else if (qs8.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(7)));
                } else if (qs9.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(8)));
                } else if (qs10.isSelected()) {
                    printResult(ReadOntology.queryResult(ReadOntology.getQueryString(9)));
                }
            }
        });

    }

    private void printResult(String result) {
        myLabel.setVisible(false);
        panel2.removeAll();
        frame.invalidate();
//        String[] lines = result.split(System.lineSeparator());
//        for (String line : lines) {
//            JLabel label = new JLabel(line+"\n");
//            System.out.println(line);
//            panel2.add(label);
//        }

        String labelText = convertToHTML(result);
        JLabel label = new JLabel(labelText);
        label.setVisible(true);
        label.setBounds(10, 10, 300, 100);
        panel2.add(label);


        frame.revalidate();
        frame.repaint();
    }

    private String convertToHTML(String result) {

        ArrayList<String> title = new ArrayList<>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        String[] lines = result.split(System.lineSeparator());
        int count = 0;
        boolean titleFound = false;
        for (String line : lines) {
//            JLabel label = new JLabel(line);
//            System.out.println(line);
//            panel2.add(label);

            if (line.contains("---") || line.contains("===")) {
                continue;
            }

            ArrayList<String> content = new ArrayList<>();

            String[] contents = line.split("\\|");
            for (int i = 1; i < contents.length; i++) {

                contents[i] = contents[i].replace("|", "").trim();
                System.out.println(contents[i]);

                if (!contents[i].trim().equals("") || contents[i] != null) {
                    content.add(contents[i].trim());
//                    System.out.println(contents[i]);
                }
            }

            if (!titleFound) {
                System.out.println("Title " + content.size());
                title = content;
                titleFound = true;
            } else {
//                data[count] = contents;

                System.out.println("Data " + data.size());
                data.add(content);
                count++;
            }

        }

        System.out.println(title.toString());
        System.out.println(data.toString());


        StringBuilder sb = new StringBuilder();
        sb.append("<html><body><table border='1'>");

        sb.append(getTitle(title));

        for (int i = 0; i < data.size(); i++) {
            sb.append(getContent(data.get(i)));
        }

        sb.append("</table></body>></html>");
        return sb.toString();
    }

    private String getTitle(ArrayList<String> title) {

        StringBuilder titletext = new StringBuilder();
        titletext.append("<tr>");
        for (int i = 0; i < title.size(); i++) {
            titletext.append("<th>");
            titletext.append(title.get(i));
            titletext.append("</th>");
        }
        titletext.append("<tr>");

        return titletext.toString();

    }

    private String getContent(ArrayList<String> content) {

        StringBuilder contentText = new StringBuilder();
        contentText.append("<tr>");
        for (int i = 0; i < content.size(); i++) {
            contentText.append("<td>");
            if (content.get(i).contains("<")) {
                content.set(i, content.get(i).replace("<", "&lt;"));
            }
            contentText.append(content.get(i));
            contentText.append("</td>");
        }
        contentText.append("<tr>");

        return contentText.toString();

    }
}
