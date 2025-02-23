package com.parabolagames.glassmaze.desktop;

import com.parabolagames.glassmaze.shared.IAddsController;
import com.parabolagames.glassmaze.shared.IPlatformUiFix;

class EmptyPlatformFixAndAddsController implements IPlatformUiFix, IAddsController {
    static final EmptyPlatformFixAndAddsController INSTANCE = new EmptyPlatformFixAndAddsController();

    private EmptyPlatformFixAndAddsController() {
    }

    @Override
    public void showBanner(boolean show) {
    }

    @Override
    public void showInterstitial() {
    }

    @Override
    public boolean isUiAlignmentFromTopIsNecessary() {
        return false;
    }

    @Override
    public boolean isThrowingBallPadInCandyModeNecessary() {
        return false;
    }

    @Override
    public void initAds() {
    }
}
