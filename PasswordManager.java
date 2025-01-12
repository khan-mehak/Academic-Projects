import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.util.Base64;

public class PasswordManager extends Frame implements ActionListener {
    
    Label tLabel, wLabel, unameLabel, pswdLabel;
    TextField wField, unameField, pswdField;
    Button addbtn, viewbtn, clearbtn;
    TextArea displayArea;

    
    final String fname = "passwords.txt";

    public PasswordManager() {
        
        setTitle("Password Manager");
        setSize(600, 500);
        setLayout(null);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        
        tLabel = new Label("Password Manager");
        tLabel.setBounds(200, 30, 200, 30);
        tLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(tLabel);

        
        wLabel = new Label("Website:");
        wLabel.setBounds(50, 80, 100, 30);
        add(wLabel);

        wField = new TextField();
        wField.setBounds(150, 80, 200, 30);
        add(wField);

        unameLabel = new Label("Username:");
        unameLabel.setBounds(50, 130, 100, 30);
        add(unameLabel);

        unameField = new TextField();
        unameField.setBounds(150, 130, 200, 30);
        add(unameField);

        pswdLabel = new Label("Password:");
        pswdLabel.setBounds(50, 180, 100, 30);
        add(pswdLabel);

        pswdField = new TextField();
        pswdField.setBounds(150, 180, 200, 30);
        pswdField.setEchoChar('*');
        add(pswdField);

        
        addbtn = new Button("Add Password");
        addbtn.setBounds(50, 230, 120, 30);
        addbtn.addActionListener(this);
        add(addbtn);

        viewbtn = new Button("View Passwords");
        viewbtn.setBounds(200, 230, 120, 30);
        viewbtn.addActionListener(this);
        add(viewbtn);

        clearbtn = new Button("Clear");
        clearbtn.setBounds(350, 230, 120, 30);
        clearbtn.addActionListener(this);
        add(clearbtn);

        // Display area
        displayArea = new TextArea();
        displayArea.setBounds(50, 280, 500, 150);
        displayArea.setEditable(false);
        add(displayArea);
    }

    
    private String encryptPassword(String password) {
        return Base64.getEncoder().encodeToString(password.getBytes());
    }

    
    private String decryptPassword(String encryptedPassword) {
        return new String(Base64.getDecoder().decode(encryptedPassword));
    }

    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addbtn) {
            handleAddPassword();
        } else if (e.getSource() == viewbtn) {
            handleViewPasswords();
        } else if (e.getSource() == clearbtn) {
            clearFields();
            displayArea.setText("");
        }
    }
    
    private void handleAddPassword() {
        String website = wField.getText().trim();
        String username = unameField.getText().trim();
        String password = pswdField.getText().trim();
    
        if (!website.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            try (FileWriter writer = new FileWriter(fname, true)) {
                writer.write(String.format("%s,%s,%s%n", website, username, encryptPassword(password)));
                displayArea.setText("Password added successfully!");
                clearFields();
            } catch (IOException ex) {
                displayArea.setText("Error saving password: " + ex.getMessage());
            }
        } else {
            displayArea.setText("All fields are required!");
        }
    }
    
    private void handleViewPasswords() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                data.append(String.format("Website: %s, Username: %s, Password: %s%n", parts[0], parts[1], decryptPassword(parts[2])));
            }
            displayArea.setText(data.toString());
        } catch (IOException ex) {
            displayArea.setText("Error reading passwords: " + ex.getMessage());
        }
    }
    

    
    private void clearFields() {
        wField.setText("");
        unameField.setText("");
        pswdField.setText("");
    }

    public static void main(String[] args) {
        new PasswordManager();
    }
}
