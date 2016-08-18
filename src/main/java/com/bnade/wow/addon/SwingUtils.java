package com.bnade.wow.addon;

import javax.swing.*;
import java.awt.*;

/**
 * Created by liufeng0103 on 8/18/2016.
 */
public class SwingUtils {

    public static void updateUILookAndFeel(Component c) {
        try {
            String plafName = UIManager.getInstalledLookAndFeels()[1].getClassName();
            UIManager.setLookAndFeel(plafName);
            SwingUtilities.updateComponentTreeUI(c);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showOnScreenCenter(Component c) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        c.setLocation(screenSize.width / 2 - c.getWidth() / 2, screenSize.height / 2 - c.getHeight() / 2);
    }

}
