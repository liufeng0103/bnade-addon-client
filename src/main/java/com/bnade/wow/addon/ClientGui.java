package com.bnade.wow.addon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by luis on 8/18/2016.
 */
public class ClientGui extends JFrame {

    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public ClientGui() {
        setTitle("BNADE插件客户端");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Client client = new Client();


        JTextField wowDirTxt = new JTextField(20);
        wowDirTxt.setEnabled(false);
        JButton selectBtn = new JButton("选择");
        JButton updateAddonBtn = new JButton("更新");
        JLabel msgLab = new JLabel();

        selectBtn.addActionListener((ActionEvent e) -> {
            JFileChooser fileChoose = new JFileChooser();
            fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String wowDir = ClientProperties.getKeyValue("wowDir") != null ? ClientProperties.getKeyValue("wowDir") : System.getProperty("user.dir");
//            System.out.println(wowDir);
            fileChoose.setCurrentDirectory(new File(wowDir));
            fileChoose.setDialogTitle("请选择魔兽世界安装目录");
            if (JFileChooser.APPROVE_OPTION == fileChoose.showOpenDialog(null)) {
                String path = fileChoose.getSelectedFile().getAbsolutePath();
                wowDirTxt.setText(path);
                if (!client.isCorrectWowDir(path)) {
                    msgLab.setText("请选择正确的魔兽世界目录");
                } else {

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
                        if (!remoteVersion.equals(version)) {
                            msgLab.setText("开始更新");
                            client.updateAddon();
                            msgLab.setText("更新完毕");
                        } else {
                            msgLab.setText("已是最新版本");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    msgLab.setText("魔兽世界安装目录不正确");
                }
            }
        });

        JPanel mainPanel = new JPanel();
        mainPanel.add(wowDirTxt);
        mainPanel.add(selectBtn);
        mainPanel.add(updateAddonBtn);
        mainPanel.add(msgLab);
        add(mainPanel);

        SwingUtils.showOnScreenCenter(ClientGui.this);
        SwingUtils.updateUILookAndFeel(ClientGui.this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGui mainFrame = new ClientGui();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setVisible(true);
        });
    }

}
