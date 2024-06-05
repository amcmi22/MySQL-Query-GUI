/*
 * Students are reminded that this assignment is to be completed independently. 
 * Discussion of solutions, sharing of code, or seeking assistance from peers, 
 * external sources, or ChatGPT is strictly prohibited. Failure to adhere to 
 * this policy constitutes academic misconduct and will result in serious 
 * consequences, including but not limited to penalties outlined in the 
 * academic integrity guidelines.
 * By providing your name and student number, you affirm that this work is exclusively your own. 
 * Name: ___________________
 * Student Number: ______________
 * 
 */
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
/**
 * This code is written to help you practice embedding SQL statement into 
 * a java code. 
 * Since this is not written by you, you cannot share it in public spaces (Github, etc.) 
 * as your own code. 
 * @author The starter code (GUI) is written by Marzieh Ahmadzadeh.
 *
 */
public class QueryManagement {
    static java.util.List<String> selectedAttributesInSelect; 
    static java.util.List<String> selectedTables;
    static String where;
    static java.util.List<String> selectedAttributesInGroupBy; 
    static String having;
    static java.util.List<String> selectedAttributesInOrderBy; 
    static String orderOption;
    static String limit;
    static String offset;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
    /**
     * This method creates a frame that contains several panels. Each panel belongs 
     * to one component such as text box, radio button, check box, and so on.
     * In this method two buttons are defined, submit and exit. When Submit is clicked, 
     * the query should be executed. When Exit is clicked, the program terminates. 
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Query Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.getContentPane().setBackground(Color.LIGHT_GRAY);
        frame.setForeground(Color.BLACK);


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);

        // SELECT List
        //EMBED SQL: change the value of attributeList to contain all the attributes from City, Country and CountryLanguage
        String [] attributeList = {"Option 1", "Option 2", "Option 3", };
        panel.add(createRow("SELECT:", createListPanel('S', attributeList)));
        panel.add(createSeparator());

        // FROM Checkbox
        String [] tableList = {"Country", "City", "CountryLanguage"};
        panel.add(createRow("FROM:", createCheckboxPanel(tableList)));
        panel.add(createSeparator());

        // WHERE Text Field
        panel.add(createRow("WHERE:", createTextFieldPanel('W')));
        panel.add(createSeparator());

        // GROUP BY List
        //EMBED SQL: change the value of attributeList to contain all the attributes from City, Country and CountryLanguage
        String [] groupByList = {"Option 1", "Option 2", "Option 3"};
        panel.add(createRow("GROUP BY:", createListPanel('G', groupByList)));
        panel.add(createSeparator());

        // HAVING Text Field
        panel.add(createRow("HAVING:", createTextFieldPanel('H')));
        panel.add(createSeparator());

        // ORDER BY List
        //EMBED SQL: change the value of attributeList to contain all the attributes from City, Country and CountryLanguage
        String [] orderByList = {"Option 1", "Option 2", "Option 3"};
        panel.add(createRow("ORDER BY:", createListPanel('O', orderByList)));
        panel.add(createSeparator());

        // DESC/ASC Radio Buttons
        panel.add(createRow("Order Direction:", createRadioPanel("DESC", "ASC")));
        panel.add(createSeparator());

        // LIMIT Text Field
        panel.add(createRow("LIMIT:", createTextFieldPanel('L')));
        panel.add(createSeparator());

        // OFFSET Text Field
        panel.add(createRow("OFFSET:", createTextFieldPanel('O')));
        panel.add(createSeparator());
 
        // Submit and Exit Buttons
        panel.add(createButtonPanel("Submit", "Exit"));

        frame.add(panel);
        frame.setVisible(true);
    }
    
    /** 
     * This method is responsible to create a panel that contains two components. 
     * The first component is a label, and the second is one of the component type that 
     * allows for user input (e.g. checkbox, radio button, etc.)
     * @param label is a message that should be written in the label
     * @param component is a type of components used to allow user input.
     * @return The panel that contains a label and user input component is returned. 
     */
    private static JPanel createRow(String label, JComponent component) {
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
        rowPanel.setBackground(Color.LIGHT_GRAY);

        rowPanel.add(new JLabel(label));
        rowPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add some space before the component
        rowPanel.add(component);

        return rowPanel;
    }
    /**
     * This method creates a panel that contains a label and a scrolling list
     * made of the options passed as the input parameter of the method.
     * The user selected items are added to <code> selectedAttributesInSelect </code> attribute.
     * @param options is an array of string that contains all the items that should be shown in the scrolling list.
     * @param clause is used to specify to which clause (i.e. select, group by, order by) the <code>options</code> (i.e., the second input parameter) belong.
     * @return a panel containing two components (a label and list) is returned.
     */
    private static JPanel createListPanel(char clause, String... options) {
        JPanel listPanel = new JPanel();
        JList<String> list = new JList<>(options);

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.X_AXIS));
        listPanel.setBackground(Color.LIGHT_GRAY);

        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        listPanel.add(scrollPane);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                	if (clause == 'S')
                		selectedAttributesInSelect = list.getSelectedValuesList();
                	else if (clause == 'G')
                		selectedAttributesInGroupBy = list.getSelectedValuesList();
                	else
                		selectedAttributesInOrderBy = list.getSelectedValuesList();
                }
            }
        });

        return listPanel;
    }

    /**
     * This method creates a check box out of the given input parameter.
     * @param options is an array of String that contains the check box items.
     * @return it returns a panel that contains the check box.
     */
    private static JPanel createCheckboxPanel(String... options) {
    	JCheckBox checkBox = null;
    	selectedTables = new ArrayList<String>();
    	JPanel checkboxPanel = new JPanel();
        checkboxPanel.setLayout(new BoxLayout(checkboxPanel, BoxLayout.Y_AXIS));

        for (String option : options) {
            checkBox = new JCheckBox(option);
            checkBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                	selectedTables.add(option);
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	selectedTables.remove(option);
                }
            });
            checkboxPanel.add(checkBox);
        }

        return checkboxPanel;
    }
    
    /**
     * This method creates a text field to allow users input data. 
     * @param clause This input parameter specifies to which clauses (i.e. where, having) the user input belongs.
     * @return It return the text field. 
     */
    private static JTextField createTextFieldPanel(char clause) {
        JTextField textField = new JTextField();
        textField.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Add a DocumentListener to the JTextField
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateWhere();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateWhere();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateWhere();
            }

            // Helper method to update the "where","having", "limit" and "offset" variable
            private void updateWhere() {
            	if (clause == 'W') where = textField.getText();
            	else if (clause == 'H') having = textField.getText();
            	else if (clause == 'L') limit = textField.getText();
            	else offset = textField.getText();
            }
        });

        return textField;
    }
    
    /**
     * This method creates a radio button.
     * @param options This variable contains the items of the radio button.
     * @return It returns a panel that contains a set of radio buttons. 
     */
    private static JPanel createRadioPanel(String... options) {
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new BoxLayout(radioPanel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();
        for (String option : options) {
            JRadioButton radioButton = new JRadioButton(option);

            // Add ActionListener directly in the loop
            radioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    orderOption = radioButton.getText();
                }
            });

            group.add(radioButton);
            radioPanel.add(radioButton);
        }

        return radioPanel;
    }



    /**
     * This method creates an empty panel to be used as the separator of other panles. 
     * @return It returns an empty panel.
     */
    private static JPanel createSeparator() {
        JPanel separatorPanel = new JPanel();
        separatorPanel.setBackground(Color.LIGHT_GRAY);
        separatorPanel.add(new JSeparator());
        return separatorPanel;
    }

    /**
     * This method creates a panel that contains a set of checkboxes.
     * @param labels This variable contains all the items of the checkbox
     * @return it returns a panel containg the checkbox. 
     */
    private static JPanel createButtonPanel(String... labels) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        for (String label : labels) {
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (label.equals("Submit")) {
                        executeQuery();
                    } else if (label.equals("Exit")) {
                        System.exit(0);
                    }
                }
            });
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add some space between buttons
        }

        return buttonPanel;
    }

    /**
     * This method is executed if submit button is clicked. 
     * In this method the communication between this program and DBMS takes place and
     * the requested query is executed
     */
    private static void executeQuery() {
        // Implement your logic here to handle the submit action
    	// The values from the components are retrieved below, ready to 
    	// execute the query. All you need to do is to connect to DBMS, and 
    	// run the query securely.
    	
        // Selected attributes in SELECT clause are retrieved
        if (selectedAttributesInSelect != null) {
	        System.out.print("SELECT ");
	        for (Object selectedValue : selectedAttributesInSelect) {
	        	if (selectedValue == null) break;
	            System.out.print((String) selectedValue +  ", ");
	        }
	        System.out.println();
        }

        // Selected tables in FROM clause are retrieved
        if (selectedTables != null) {
	        System.out.print("FROM ");               
	        for (Object selectedValue : selectedTables) {
	        	if (selectedValue == null) break;
	           System.out.print((String) selectedValue + ", ");
	        }
	        System.out.println();
        }

        // entered condition in WHERE clause is retrieved
        System.out.println("WHERE " + where);

        // Selected attributes in GROUP BY clause are retrieved
        if (selectedAttributesInGroupBy != null) {
            System.out.print("GROUP BY ");
            for (Object selectedValue : selectedAttributesInGroupBy) {
            	if (selectedValue == null) break;
                System.out.print((String) selectedValue + ", ");
            }        	
            System.out.println();
        }

        // Entered condition in HAVING clause are retrieved
        System.out.println("having " + having);

        // Selected attributes in ORDER BY clause are retrieved
       if (selectedAttributesInOrderBy != null) {
	    	System.out.print("ORDER BY ");
	        for (Object selectedValue : selectedAttributesInOrderBy) {
	        	if (selectedValue == null) break;
	        	System.out.print((String) selectedValue + ", ");
	        }
        }
 
       // Selected direction of order by is retrieved
       System.out.println(orderOption);

       // Entered number for LIMIT clause is retrieved
        System.out.println("Limit " + limit);

        // Entered number in OFFSET clause is retrieved
        System.out.println("Offset " + offset);
    }
}


