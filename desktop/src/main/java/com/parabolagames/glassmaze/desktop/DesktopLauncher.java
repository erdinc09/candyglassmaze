package com.parabolagames.glassmaze.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.parabolagames.glassmaze.core.GlassMazeMainProxy;
import com.parabolagames.glassmaze.store.TestPurchaseManager;


public class DesktopLauncher {
    private static final boolean rebuildAtlas = Boolean.getBoolean("rebuildAtlas");

    public static void main(String[] arg) {

        if (rebuildAtlas) {
            AtlasBuilder.buildAtlases();
        } else {
            if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
            createApplication();
        }
    }

    private static void createApplication() {
        new Lwjgl3Application(new GlassMazeMainProxy(
                EmptyPlatformFixAndAddsController.INSTANCE,
                EmptyPlatformFixAndAddsController.INSTANCE,
                new TestPurchaseManager()), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Candy Glass Maze");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(true);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode(450, 800);
        configuration.setWindowIcon("ic_launcher_foreground.png");
        configuration.setResizable(false);
        //configuration.setHdpiMode(HdpiMode.Pixels);
        return configuration;
    }
}
