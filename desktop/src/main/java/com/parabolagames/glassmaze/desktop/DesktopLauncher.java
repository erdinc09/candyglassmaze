package com.parabolagames.glassmaze.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.parabolagames.glassmaze.core.GlassMazeMainProxy;
import com.parabolagames.glassmaze.store.TestPurchaseManager;

public class DesktopLauncher {
    private static boolean rebuildAtlas = Boolean.getBoolean("rebuildAtlas");

    public static void main(String[] arg) {

        if (rebuildAtlas) {
            AtlasBuilder.buildAtlases();
        } else {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.width = 450;
            config.height = 800;
            config.samples = 3;
            config.useHDPI = true;
            config.vSyncEnabled = true;
            new LwjglApplication(
                    new GlassMazeMainProxy(
                            EmptyPlatformFixAndAddsController.INSTANCE,
                            EmptyPlatformFixAndAddsController.INSTANCE,
                            new TestPurchaseManager()),
                    config);
        }
    }
}
