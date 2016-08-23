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
    private static final int DEFAULT_HEIGHT = 542;


    public ClientGui() {
        setTitle("BNADE插件客户端");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setResizable(false);

        Client client = new Client();
        String wowPath = ClientProperties.getKeyValue("wowDir");

        // 菜单栏
        JMenu menu = new JMenu("霜之哀伤");
        JMenuItem tsmItem = new JMenuItem("TSM插件数据");
        menu.add(tsmItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // 页面组件
        JTextField wowDirTxt = new JTextField(20);
        if (wowPath != null) {
            wowDirTxt.setText(wowPath);
        }
        wowDirTxt.setEnabled(false);
        TSMAppDataGui tsmAppDataGui = new TSMAppDataGui(wowDirTxt);
        JButton selectBtn = new JButton("浏览");
        JButton updateAddonBtn = new JButton("开始更新");
        JLabel msgLab = new JLabel();

        // 添加组件并布局
        JPanel addonElePanel = new JPanel();
        addonElePanel.add(new JLabel("WOW安装路径:"));
        addonElePanel.add(wowDirTxt);
        addonElePanel.add(selectBtn);
        addonElePanel.add(updateAddonBtn);
        addonElePanel.add(msgLab);
        JPanel addonPanel = new JPanel(new BorderLayout());
        addonPanel.add(addonElePanel, BorderLayout.WEST);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel(new ImageIcon(ClientProperties.getKeyValue("img", "res/main.jpg"))), BorderLayout.NORTH);
        mainPanel.add(addonPanel);

        add(mainPanel);

        // 事件处理
        tsmItem.addActionListener((ActionEvent e) -> {
            tsmAppDataGui.setVisible(true);
        });
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

class TSMAppDataGui extends JFrame {

    public TSMAppDataGui(JTextField wowDirTxt) {
        setTitle("TSM插件的数据更新");
        setSize(500, 70);
        setResizable(false);

        Client client = new Client();

        JTextField realmTxt = new JTextField(10);
        if (ClientProperties.getKeyValue("realm") != null) {
            realmTxt.setText(ClientProperties.getKeyValue("realm"));
        }
        JButton updateBtn = new JButton("开始更新");
        JLabel msgLab = new JLabel();

        //TradeSkillMaster_AppHelper
        JPanel elePanel = new JPanel();
        elePanel.add(new JLabel("服务器:"));
        elePanel.add(realmTxt);
        elePanel.add(updateBtn);
        elePanel.add(msgLab);
        JPanel layout = new JPanel(new BorderLayout());
        layout.add(elePanel, BorderLayout.WEST);

        add(layout);

        //
        updateBtn.addActionListener((ActionEvent e) -> {
            String wowDir = wowDirTxt.getText();
            if ("".equals(wowDir)) {
                msgLab.setText("请先选择魔兽世界安装目录");
            } else {
                if (client.isCorrectWowDir(wowDir)) {
                    if (client.isTradeSkillMasterAppHelperInstalled()) {
                        String realm = realmTxt.getText();
                        Integer realmId = Realm.getIdByName(realm);
                        if (realmId != null) {
                            try {
                                ClientProperties.setKeyValue("realm", realm);
                            } catch (IOException e1) {
                                showErrorMessage("错误:" + e1.getMessage());
                                e1.printStackTrace();
                            }
                            try {
                                if (!client.getTSMAppDataVersion(realmId).equals(client.getLocalTSMAppDataVersion())) {
                                    msgLab.setText("正在下载数据，请稍等...");
                                    HttpClient httpClient = new HttpClient();
                                    String content = httpClient.get("http://www.bnade.com/appData/" + realmId + ".lua").replace("{xxrealmxx}", realm);
//                                    System.out.println(content);
                                    IOUtils.stringToFile(content, wowDir + Client.ADDONS_DIR + "/TradeSkillMaster_AppHelper/AppData.lua");
                                    msgLab.setText("数据更新完毕");
                                } else {
                                    msgLab.setText("已是最新数据");
                                }
                            } catch (IOException e1) {
                                showErrorMessage("错误:" + e1.getMessage());
                                e1.printStackTrace();
                            }
                        } else {
                            msgLab.setText("找不到服务器：" + realm);
                        }
                    } else {
                        msgLab.setText("请先安装TradeSkillMaster_AppHelper插件");
                    }
                } else {
                    msgLab.setText("魔兽世界安装目录不正确");
                }
            }
        });

        SwingUtils.updateUILookAndFeel(JOptionPane.getRootFrame());
        SwingUtils.showOnScreenCenter(this);
        SwingUtils.updateUILookAndFeel(this);
    }

    private void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "错误" ,JOptionPane.ERROR_MESSAGE);
    }
}