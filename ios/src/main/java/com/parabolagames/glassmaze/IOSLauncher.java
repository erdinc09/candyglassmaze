package com.parabolagames.glassmaze;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.utils.Logger;
import com.parabolagames.glassmaze.core.GlassMazeMainProxy;
import com.parabolagames.glassmaze.shared.IAddsController;
import com.parabolagames.glassmaze.shared.IPlatformUiFix;
import com.parabolagames.glassmaze.store.TestPurchaseManager;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIDevice;


import libcore.io.Libcore;

// NOT: I see that, GDX game loop thread is also is main thread, which is also the IOS native UI
// thread.
// Therefore, there is no need to 'DispatchQueue.getMainQueue().async(...' mechanism. However I used
// it...
public class IOSLauncher extends IOSApplication.Delegate implements IPlatformUiFix {

    private static final Logger log = new Logger(IOSLauncher.class.getName(), Application.LOG_DEBUG);
    public static final String TAG = "com.parabolagames.glassmaze.IOSLauncher";


    public static void main(String[] argv) {
        //System.setProperty("externalLevelNumber", "37");
        //System.setProperty("candyMode", "true");
        //System.setProperty("drawFps", "true");
        NSAutoreleasePool pool = new NSAutoreleasePool();
        System.out.printf("main(..):Thread:%s%n", Thread.currentThread().getName());
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }

    private Boolean uiAlignmentFromTopNecessary;

    @Override
    protected IOSApplication createApplication() {
        System.out.printf("createApplication():Thread:%s%n", Thread.currentThread().getName());

        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        return
                new IOSApplication(
                        new GlassMazeMainProxy(
                                this,
                                new IAddsController() {
                                    @Override
                                    public void initAds() {
                                    }

                                    @Override
                                    public void showBanner(final boolean show) {
                                    }

                                    @Override
                                    public void showInterstitial() {
                                    }
                                },
                                new TestPurchaseManager()
                        ),
                        config) {
                };
    }


    @Override
    public boolean isUiAlignmentFromTopIsNecessary() {
        if (uiAlignmentFromTopNecessary == null) {
            String machine = Libcore.os.uname().machine;

            if (machine.contentEquals("x86_64")) {
                UIDevice currentDevice = UIDevice.getCurrentDevice();
                String name = currentDevice.getName();
                uiAlignmentFromTopNecessary =
                        name.startsWith("iPhone X")
                                || name.startsWith("iPhone 10")
                                || name.startsWith("iPhone 11")
                                || name.startsWith("iPhone 12")
                ;
            } else {
                uiAlignmentFromTopNecessary =
                        machine.startsWith("iPhoneX")
                                || machine.startsWith("iPhone10")
                                || machine.startsWith("iPhone11")
                                || machine.startsWith("iPhone12")
                ;
            }
        }

        return uiAlignmentFromTopNecessary;
    }

    @Override
    public boolean isThrowingBallPadInCandyModeNecessary() {
        return isUiAlignmentFromTopIsNecessary(); // https://bgr.com/2017/12/12/apple-iphone-x-home-button-gamble-interview/
    }
}
