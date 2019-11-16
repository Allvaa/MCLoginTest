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
                String newstr = "";
                try {
                    for (String usernpass: list) {
                        if (usernpass.equals("")) {
                            infoLabel.setText("Make sure the field isn't empty!");
                            return;
                        }
                        String[] arr =  usernpass.split(":");
                        if (arr.length < 2) {
                            infoLabel.setText("Missing email or password.");
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
                        switch (res.getStatus()) {
                            case 200:
                                status += " (OK)";
                                break;
                            case 403:
                                status += " (Failed)";
                                break;
                            default:
                                status += " (Unknown Error)";
                                break;
                        }
                        String strlist = newstr += status + "\n";
                        infoLabel.setText(infoLabel.getText().equals("") ? infoLabel.getText() + status : strlist);
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