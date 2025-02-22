package com.parabolagames.glassmaze.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.jetbrains.annotations.NotNull;

public abstract class BaseScreen
        implements Screen, InputProcessor, IScreenDrawer, IPauseResumeFromGame,ICurrentScreenProvider {
    public static final boolean DEBUG_DRAW = Boolean.getBoolean("debugDraw");
    protected final Stage mainStage_1;
    protected final Stage mainStage0;
    protected final Stage mainStage1;
    protected final Stage dialogUiStage;
    protected final Stage uiStage;
    protected final Stage uiStagePlayPause;
    private final Stage explanationCandyStage;
    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private boolean paused = false;
    private LineData lineData;
    private final Stage dialogUiStageForInformation;
    private final InputProcessorProxyWithStage inputProcessorProxyDialogUiStage;

    // menu screen
    public BaseScreen(Stage mainStage0, Stage uiStage, Stage dialogUiStage, Stage dialogUiStageForInformation) {
        this.mainStage0 = mainStage0;
        this.uiStage = uiStage;
        this.dialogUiStage = dialogUiStage;

        this.uiStagePlayPause = null;
        this.mainStage_1 = null;
        this.mainStage1 = null;
        this.explanationCandyStage = null;
        this.inputProcessorProxyDialogUiStage = new InputProcessorProxyWithStage(dialogUiStage, dialogUiStageForInformation);//new InputProcessorProxy(dialogUiStage);
        this.dialogUiStageForInformation = dialogUiStageForInformation;
    }

    // Loading screen
    public BaseScreen(Stage mainStage0, Stage uiStage) {
        this.mainStage0 = mainStage0;
        this.uiStage = uiStage;

        this.uiStagePlayPause = null;
        this.mainStage_1 = null;
        this.mainStage1 = null;
        this.dialogUiStage = null;
        this.explanationCandyStage = null;
        this.inputProcessorProxyDialogUiStage = null;
        this.dialogUiStageForInformation = null;
    }

    // GameScreen:
    // --> candy mode screen
    // --> classic mode screen
    public BaseScreen(
            Stage mainStage_1,
            Stage mainStage0,
            Stage mainStage1,
            Stage uiStage,
            Stage uiStagePlayPause,
            Stage dialogUiStage,
            Stage dialogUiStageForInformation,
            Stage explanationCandyStage) {
        this.mainStage_1 = mainStage_1;
        this.mainStage0 = mainStage0;
        this.mainStage1 = mainStage1;
        this.uiStage = uiStage;
        this.uiStagePlayPause = uiStagePlayPause;
        this.dialogUiStage = dialogUiStage;
        this.explanationCandyStage = explanationCandyStage;
        this.inputProcessorProxyDialogUiStage = new InputProcessorProxyWithStage(dialogUiStage, dialogUiStageForInformation);
        this.dialogUiStageForInformation = dialogUiStageForInformation;
    }

    @NotNull
    @Override
    public Stage getDialogUiStage() {
        return dialogUiStage;
    }


    @NotNull
    @Override
    public Stage getDialogUiStageForInformation() {
        return dialogUiStageForInformation;
    }

    public abstract void update(float dt);

    // Game Loop:
    // (1) process input (discrete handled by listener; continuous in update)
    // (2) update game logic
    // (3) render the graphics
    @Override
    public final void render(float dt) {

        // ACT
        if (!paused) {
            // defined by user
            update(dt);
            // act methods
            uiStage.act(dt);

            if (mainStage_1 != null) {
                mainStage_1.act(dt);
            }

            mainStage0.act(dt);

            if (mainStage1 != null) {
                mainStage1.act(dt);
            }
        }

        if (uiStagePlayPause != null) {
            uiStagePlayPause.act();
        }

        if (dialogUiStage != null) {
            dialogUiStage.act();
        }

        if (dialogUiStageForInformation != null) {
            dialogUiStageForInformation.act();
        }

        if (explanationCandyStage != null) {
            explanationCandyStage.act();
        }

        // DRAW
        // clear the screen
        // Gdx.gl.glClearColor(0, 0, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                        | GL20.GL_DEPTH_BUFFER_BIT
                        | (Gdx.graphics.getBufferFormat().coverageSampling
                        ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                        : 0));

        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (mainStage_1 != null) {
            mainStage_1.getViewport().apply();
            mainStage_1.draw();
        }

        // draw the graphics
        mainStage0.getViewport().apply();
        mainStage0.draw();

        if (mainStage1 != null) {
            mainStage1.getViewport().apply();
            mainStage1.draw();
        }

        uiStage.getViewport().apply();
        uiStage.draw();

        if (uiStagePlayPause != null) {
            uiStagePlayPause.getViewport().apply();
            uiStagePlayPause.draw();
        }

        if (dialogUiStage != null) {
            dialogUiStage.getViewport().apply();
            dialogUiStage.draw();
        }

        if (dialogUiStageForInformation != null) {
            dialogUiStageForInformation.getViewport().apply();
            dialogUiStageForInformation.draw();
        }

        if (explanationCandyStage != null) {
            explanationCandyStage.getViewport().apply();
            explanationCandyStage.draw();
        }

        if (!paused) {
            if (lineData != null) {
                shapeRenderer.setProjectionMatrix(uiStage.getCamera().combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.rectLine(
                        lineData.start.x,
                        lineData.start.y,
                        lineData.end.x,
                        lineData.end.y,
                        lineData.lineWidth,
                        lineData.colorStart,
                        lineData.colorEnd);
                shapeRenderer.end();

                shapeRenderer.setColor(lineData.colorStart);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.circle(lineData.start.x, lineData.start.y, 0.01f, 1000);
                shapeRenderer.circle(lineData.end.x, lineData.end.y, 0.01f, 1000);
                shapeRenderer.end();
            }

            if (DEBUG_DRAW) {
                renderDebug(dt);
            }
        }
    }

    protected void renderDebug(float dt) {
    }

    // methods required by Screen interface
    public void resize(int width, int height) {
        mainStage0.getViewport().update(width, height);
        if (mainStage1 != null) {
            mainStage1.getViewport().update(width, height);
        }
        if (mainStage_1 != null) {
            mainStage_1.getViewport().update(width, height);
        }
        uiStage.getViewport().update(width, height);

        if (dialogUiStage != null) {
            dialogUiStage.getViewport().update(width, height);
        }

        if (dialogUiStageForInformation != null) {
            dialogUiStageForInformation.getViewport().update(width, height);
        }

        if (explanationCandyStage != null) {
            explanationCandyStage.getViewport().update(width, height);
        }
    }

    @Override
    public void pauseFromGame() {
        paused = true;
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        removeInputProcessors();

        if (dialogUiStageForInformation != null) {
            im.addProcessor(dialogUiStageForInformation);
        }

        if (inputProcessorProxyDialogUiStage != null) {
            im.addProcessor(inputProcessorProxyDialogUiStage);
        }

        if (uiStagePlayPause != null) {
            im.addProcessor(uiStagePlayPause);
        }
    }

    @Override
    public void resumeFromGame() {
        paused = false;
        removeInputProcessors();
        addInputProcessors();
    }

    public void pause() {
    }

    public void resume() {
        // also called from when app is minimized, therefore it is empty
    }


    public void dispose() {
        Gdx.app.debug(getClass().getName(), "disposed");
        if (dialogUiStage != null) {
            dialogUiStage.dispose();
        }
        if (dialogUiStageForInformation != null) {
            dialogUiStageForInformation.dispose();
        }
        if (uiStage != null) {
            uiStage.dispose();
        }
        if (mainStage0 != null) {
            mainStage0.dispose();
        }
        if (mainStage1 != null) {
            mainStage1.dispose();
        }
        if (mainStage_1 != null) {
            mainStage_1.dispose();
        }
        if (uiStagePlayPause != null) {
            uiStagePlayPause.dispose();
        }
        if (explanationCandyStage != null) {
            explanationCandyStage.dispose();
        }
        shapeRenderer.dispose();
    }

    public boolean isPaused() {
        return paused;
    }

    /**
     * Called when this becomes the active screen in a Game. Set up InputMultiplexer here, in case
     * screen is reactivated at a later time.
     */
    public void show() {
        addInputProcessors();
    }

    private void addInputProcessors() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        addAdditionalInputProcessors();
        im.addProcessor(this);

        if (dialogUiStageForInformation != null) {
            im.addProcessor(dialogUiStageForInformation);
        }

        if (inputProcessorProxyDialogUiStage != null) {
            im.addProcessor(inputProcessorProxyDialogUiStage);
        }

        im.addProcessor(uiStage);

        if (uiStagePlayPause != null) {
            im.addProcessor(uiStagePlayPause);
        }

        im.addProcessor(mainStage0);
        if (mainStage1 != null) {
            im.addProcessor(mainStage1);
        }

        if (explanationCandyStage != null) {
            im.addProcessor(explanationCandyStage);
        }
    }

    /**
     * Called when this is no longer the active screen in a Game. Screen class and Stages no longer
     * process input. Other InputProcessors must be removed manually.
     */
    public void hide() {
        removeInputProcessors();
    }

    private void removeInputProcessors() {
        InputMultiplexer im = (InputMultiplexer) Gdx.input.getInputProcessor();
        removeAdditionalInputProcessors();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        if (uiStagePlayPause != null) {
            im.removeProcessor(uiStagePlayPause);
        }
        if (inputProcessorProxyDialogUiStage != null) {
            im.removeProcessor(inputProcessorProxyDialogUiStage);
        }
        if (dialogUiStageForInformation != null) {
            im.removeProcessor(dialogUiStageForInformation);
        }
        im.removeProcessor(mainStage0);
        if (mainStage1 != null) {
            im.removeProcessor(mainStage1);
        }

        if (explanationCandyStage != null) {
            im.removeProcessor(explanationCandyStage);
        }
    }

    protected void addAdditionalInputProcessors() {
    }

    protected void removeAdditionalInputProcessors() {
    }

    @Override
    public void drawLine(LineData lineData) {
        this.lineData = lineData;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
}
