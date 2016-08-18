package com.bnade.wow.addon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by luis on 8/18/2016.
 */
public class ClientGui extends JFrame {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 520;
    private String wowPath = ClientProperties.getKeyValue("wowDir");

    public ClientGui() {
        setTitle("BNADE插件客户端");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        Client client = new Client();

        JTextField wowDirTxt = new JTextField(20);
        if (wowPath != null) {
            wowDirTxt.setText(wowPath);
        }
        wowDirTxt.setEnabled(false);
        JButton selectBtn = new JButton("浏览");
        JButton updateAddonBtn = new JButton("开始更新");
        JLabel msgLab = new JLabel();
        selectBtn.addActionListener((ActionEvent e) -> {
            JFileChooser fileChoose = new JFileChooser();
            fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String wowDir = wowPath != null ? wowPath : System.getProperty("user.dir");
//            System.out.println(wowDir);
            fileChoose.setCurrentDirectory(new File(wowDir));
            fileChoose.setDialogTitle("请选择魔兽世界安装路径");
            if (JFileChooser.APPROVE_OPTION == fileChoose.showOpenDialog(null)) {
                String path = fileChoose.getSelectedFile().getAbsolutePath();
                if (!client.isCorrectWowDir(path)) {
                    msgLab.setText("魔兽世界安装目录不正确");
                } else {
                    try {
                        ClientProperties.setKeyValue("wowDir", path);
                    } catch (IOException e1) {
                        showErrorMessage("保存WOW安装路径时出错，错误原因：\n" + e1.getMessage());
                        e1.printStackTrace();
                    }
                    wowDirTxt.setText(path);
                    msgLab.setText("");
                }
            }
        });

        updateAddonBtn.addActionListener((ActionEvent e) -> {
            String wowDir = wowDirTxt.getText();
            if ("".equals(wowDir)) {
                msgLab.setText("请先选择魔兽世界安装目录");
            } else {
                if (client.isCorrectWowDir(wowDir)) {
                    try {
                        String version = client.getLocalVersion();
                        String remoteVersion = client.getVersion();
//                        System.out.println(version);
//                        System.out.println(remoteVersion);
                        if (!remoteVersion.equals(version)) {
                            msgLab.setText("正在更新，请稍等...");
                            client.updateAddon();
                            msgLab.setText("更新完毕，数据日期：" + remoteVersion);
                        } else {
                            msgLab.setText("已是最新版本，数据日期：" + remoteVersion);
                        }
                    } catch (IOException e1) {
                        showErrorMessage("更新插件时出错，错误原因：\n" + e1.getMessage());
                        e1.printStackTrace();
                    }
                } else {
                    msgLab.setText("魔兽世界安装目录不正确");
                }
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel addonPanel = new JPanel(new BorderLayout());
        JPanel addonElePanel = new JPanel();
        addonPanel.add(addonElePanel, BorderLayout.WEST);
        mainPanel.add(new JLabel(new ImageIcon(ClientProperties.getKeyValue("img", "res/main.jpg"))), BorderLayout.NORTH);
        mainPanel.add(addonPanel);
        addonElePanel.add(new JLabel("WOW安装路径:"));
        addonElePanel.add(wowDirTxt);
        addonElePanel.add(selectBtn);
        addonElePanel.add(updateAddonBtn);
        addonElePanel.add(msgLab);
        add(mainPanel);
        SwingUtils.updateUILookAndFeel(JOptionPane.getRootFrame());
        SwingUtils.showOnScreenCenter(ClientGui.this);
        SwingUtils.updateUILookAndFeel(ClientGui.this);
    }

    private void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "错误" ,JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGui mainFrame = new ClientGui();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });
    }

}
