import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Invoice extends JFrame {

    JPanel mainPanel;
    JPanel invoicePanel;
    JPanel productPanel;
    JPanel addressPanel;
    JPanel entryPanel;
    JPanel buttonPanel;

    JLabel custNameLabel;
    JLabel streetNameLabel;
    JLabel cityLabel;
    JLabel stateLabel;
    JLabel zipCodeLabel;
    JLabel productNameLabel;
    JLabel unitPriceLabel;
    JLabel quantityLabel;

    JTextField custNameField;
    JTextField streetNameField;
    JTextField cityField;
    JTextField stateField;
    JTextField zipCodeField;
    JTextField productField;
    JTextField unitPriceField;
    JTextField quantityField;

    JTextArea invoiceArea;

    JButton addButton;
    JButton submitButton;
    JButton clearButton;
    JButton quitButton;

    JScrollPane scrollPane;

    double total = 0;

    ArrayList<LineItem> lineItems = new ArrayList<>();
    Font invoiceFont = new Font("Monospaced", Font.PLAIN, 12);

    public Invoice() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createEntryPanel();
        mainPanel.add(entryPanel, BorderLayout.CENTER);

        createInvoicePanel();
        mainPanel.add(invoicePanel, BorderLayout.EAST);

        createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setTitle("Invoice Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(3*(screenWidth / 4), 3*(screenHeight / 4));
        setLocationRelativeTo(null);
    }

    private void createEntryPanel() {
        entryPanel = new JPanel();
        entryPanel.setLayout(new GridLayout(2, 1));

        addressPanel = new JPanel();
        addressPanel.setLayout(new GridLayout(5, 2));
        addressPanel.setBorder(new TitledBorder(new EtchedBorder(), "Enter Customer Information"));

        productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(3, 2));
        productPanel.setBorder(new TitledBorder(new EtchedBorder(), "Enter Product Information"));

        custNameLabel = new JLabel("Customer Name: ");
        streetNameLabel = new JLabel("Street Address: ");
        cityLabel = new JLabel("City: ");
        stateLabel = new JLabel("State: ");
        zipCodeLabel = new JLabel("Zip Code: ");
        custNameField = new JTextField();
        streetNameField = new JTextField();
        cityField = new JTextField();
        stateField = new JTextField();
        zipCodeField = new JTextField();

        addressPanel.add(custNameLabel);
        addressPanel.add(custNameField);
        addressPanel.add(streetNameLabel);
        addressPanel.add(streetNameField);
        addressPanel.add(cityLabel);
        addressPanel.add(cityField);
        addressPanel.add(stateLabel);
        addressPanel.add(stateField);
        addressPanel.add(zipCodeLabel);
        addressPanel.add(zipCodeField);

        productNameLabel = new JLabel("Product Name: ");
        unitPriceLabel = new JLabel("Unit Price ($): ");
        quantityLabel = new JLabel("Quantity: ");
        productField = new JTextField();
        unitPriceField = new JTextField();
        quantityField = new JTextField();

        productPanel.add(productNameLabel);
        productPanel.add(productField);
        productPanel.add(unitPriceLabel);
        productPanel.add(unitPriceField);
        productPanel.add(quantityLabel);
        productPanel.add(quantityField);

        entryPanel.add(addressPanel);
        entryPanel.add(productPanel);
    }

    private void createInvoicePanel() {
        invoicePanel = new JPanel();
        invoiceArea = new JTextArea(36, 50);
        invoiceArea.setEditable(false);
        invoiceArea.setFont(invoiceFont);
        scrollPane = new JScrollPane(invoiceArea);
        invoiceArea.append("                     Invoice                 \n");
        invoiceArea.append("==================================================\n");
        invoiceArea.append(String.format("%-20s%6s%10s%10s", "Item", "Qty", "Price($)", "Total($)") + "\n");
        invoicePanel.add(scrollPane);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));

        addButton = new JButton("Add Item");
        addButton.addActionListener((ActionEvent ae) -> addItem(productField.getText(), unitPriceField.getText(), quantityField.getText()));
        submitButton = new JButton("Submit");
        submitButton.addActionListener((ActionEvent ae) -> submit());
        clearButton = new JButton("Clear");
        clearButton.addActionListener((ActionEvent ae) -> clear());
        quitButton = new JButton("Quit");
        quitButton.addActionListener((ActionEvent ae) -> {
            int choice = JOptionPane.showConfirmDialog(quitButton, "Do you want to quit?", "Quit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);}});

        buttonPanel.add(addButton);
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
    }

    private void addItem(String productName, String unitPrice, String quantity) {
        try {
            double unitPriceConverted = Double.parseDouble(unitPrice);
            try {
                int quantityConverted = Integer.parseInt(quantity);
                LineItem newItem = new LineItem(productName, unitPriceConverted, quantityConverted);
                lineItems.add(newItem);
                invoiceArea.append(newItem.toString());
                productField.setText("");
                unitPriceField.setText("");
                quantityField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Quantity is not in the correct format!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Unit Price is not in the correct format!");
        }
    }

    private void submit() {
        invoiceArea.setText("");
        invoiceArea.append("                     Invoice                 \n");
        invoiceArea.append(" ------------------------------\n");
        invoiceArea.append("|" + String.format("%-30s", custNameField.getText()) + "|\n");
        invoiceArea.append("|" + String.format("%-30s", streetNameField.getText()) + "|\n");
        invoiceArea.append("|" + String.format("%-30s", cityField.getText() + ", " + stateField.getText() + " " + zipCodeField.getText()) + "|\n");
        invoiceArea.append(" ------------------------------\n");
        invoiceArea.append(String.format("%-20s%6s%10s%10s", "Item", "Qty", "Price($)", "Total($)") + "\n");
        invoiceArea.append("==================================================\n");

        for (LineItem i : lineItems) {
            invoiceArea.append(i.toString());
            total += i.calculatedTotal;
        }

        invoiceArea.append("==================================================\n");
        invoiceArea.append(String.format("%-5s%.2f","Amount Due: $", total));

        custNameField.setText("");
        streetNameField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipCodeField.setText("");
        custNameField.setEditable(false);
        streetNameField.setEditable(false);
        cityField.setEditable(false);
        stateField.setEditable(false);
        zipCodeField.setEditable(false);
        productField.setEditable(false);
        unitPriceField.setEditable(false);
        quantityField.setEditable(false);
    }

    private void clear() {
        invoiceArea.setText("");
        invoiceArea.append("                     Invoice                 \n");
        invoiceArea.append("==================================================\n");
        invoiceArea.append(String.format("%-20s%6s%10s%10s", "Item", "Qty", "Price($)", "Total($)") + "\n");
        custNameField.setText("");
        streetNameField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipCodeField.setText("");
        productField.setText("");
        unitPriceField.setText("");
        quantityField.setText("");
        custNameField.setEditable(true);
        streetNameField.setEditable(true);
        cityField.setEditable(true);
        stateField.setEditable(true);
        zipCodeField.setEditable(true);
        productField.setEditable(true);
        unitPriceField.setEditable(true);
        quantityField.setEditable(true);
        total = 0;
        lineItems.clear();
    }
}
