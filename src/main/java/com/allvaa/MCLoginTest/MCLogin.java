package com.allvaa.MCLoginTest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MCLogin {
    private JPanel panel;
    private JButton loginButton;
    private JTextArea textArea1;
    private JTextArea infoLabel;

    public MCLogin() {
        textArea1.setLineWrap(true);
        infoLabel.setLineWrap(true);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[] list = textArea1.getText().split("\n");
                try {
                    for (String usernpass: list) {
                        if (usernpass.equals("")) {
                            infoLabel.setText("Please input the field");
                            return;
                        }
                        String[] arr =  usernpass.split(":");
                        if (arr.length < 2) {
                            infoLabel.setText("Missing email/username or password");
                            return;
                        }

                        JSONObject agentObj = new JSONObject()
                                .put("name", "Minecraft")
                                .put("version", 1);
                        JSONObject obj = new JSONObject()
                                .put("agent", agentObj)
                                .put("username", arr[0])
                                .put("password", arr[1])
                                .put("requestUser", true);

                        HttpResponse<JsonNode> res = Unirest.post("https://authserver.mojang.com/authenticate")
                                .header("Content-Type", "application/json")
                                .body(obj)
                                .asJson();
                        Thread.sleep(2 * 1000);

                        String status = usernpass;
                        if (res.getStatus() == 200) status += " (OK)";
                        else if (res.getStatus() == 403) status += " (Failed)";
                        else status += " (Unknown Error)";
                        infoLabel.setText(infoLabel.getText().equals("") ? infoLabel.getText() + status : infoLabel.getText() + "\n" + status);
                    }
                } catch (InterruptedException err) {
                    err.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame frame = new JFrame("Minecraft Login Test");
        frame.setContentPane(new MCLogin().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
