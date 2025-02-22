package com.parabolagames.glassmaze.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;

/** @author erdinc09 */
public class TableActor extends Group {

  protected Animation<TextureRegion> animation;
  protected float elapsedTime;
  protected boolean animationPaused;
  protected boolean isAnimationVisible = true;
  protected int frameNumber;
  protected Polygon boundaryPolygon;
  private float widthHeightRatio = -1;
  /**
   * Draws current frame of animation; automatically called by draw method in Stage class. <br>
   * If color has been set, image will be tinted by that color. <br>
   * If no animation has been set or object is invisible, nothing will be drawn.
   *
   * @param batch (supplied by Stage draw method)
   * @param parentAlpha (supplied by Stage draw method)
   * @see #setColor
   * @see #setVisible
   */
  private float drawPad = 0;
  // ----------------------------------------------
  // Animation methods
  // ----------------------------------------------

  public TableActor() {
    // call constructor from Actor class
    super();

    // initialize animation data
    animation = null;
    elapsedTime = 0;
    animationPaused = false;

    boundaryPolygon = null;
  }

  public void setDrawPad(float drawPad) {
    this.drawPad = drawPad;
  }

  /**
   * Sets the animation used when rendering this actor; also sets actor size.
   *
   * @param anim animation that will be drawn when actor is rendered
   */
  public void setAnimation(Animation<TextureRegion> anim) {
    animation = anim;
    TextureRegion tr = animation.getKeyFrame(0);
    float w = tr.getRegionWidth();
    float h = tr.getRegionHeight();
    setSize(w, h);
    widthHeightRatio = w / h;
    setOrigin(w / 2, h / 2);

    //		if (boundaryPolygon == null)
    //			setBoundaryRectangle();
  }

  public float getWidthHeightRatio() {
    return widthHeightRatio;
  }

  public void setWidthHeightFromWidth(float width) {
    setSize(width, width / widthHeightRatio);
  }

  public void setWidthHeightFromHeight(float height) {
    setSize(widthHeightRatio * height, height);
  }

  /**
   * Creates an animation from images stored in separate files.
   *
   * @param fileNames array of names of files containing animation images
   * @param frameDuration how long each frame should be displayed
   * @param loop should the animation loop
   * @return animation created (useful for storing multiple animations)
   */
  public Animation<TextureRegion> loadAnimationFromFiles(
      String[] fileNames, float frameDuration, boolean loop) {
    int fileCount = fileNames.length;
    Array<TextureRegion> textureArray = new Array<>();

    for (int n = 0; n < fileCount; n++) {
      String fileName = fileNames[n];
      Texture texture = new Texture(Gdx.files.internal(fileName));
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      textureArray.add(new TextureRegion(texture));
    }

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);

    if (animation == null) setAnimation(anim);

    return anim;
  }

  public Animation<TextureRegion> loadAnimationFromTextures(
      Texture[] textures, float frameDuration, boolean loop) {
    int fileCount = textures.length;
    Array<TextureRegion> textureArray = new Array<>();

    for (int n = 0; n < fileCount; n++) {
      Texture texture = textures[n];
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
      textureArray.add(new TextureRegion(texture));
    }

    return textureRegions(textureArray, frameDuration, loop);
  }

  public Animation<TextureRegion> textureRegions(
      Array<TextureRegion> textureRegions, float frameDuration, boolean loop) {
    Animation<TextureRegion> anim = new Animation<>(frameDuration, textureRegions);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);
    setAnimation(anim);

    return anim;
  }

  public Animation<TextureRegion> loadAnimationFromTextureRegions(
      Array<? extends TextureRegion> textures, float frameDuration, boolean loop) {

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textures);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);

    // if (animation == null)
    setAnimation(anim);

    return anim;
  }

  public void setAnimationAlignedFromActor(TableActor actor) {
    elapsedTime = animation.getFrameDuration() * actor.frameNumber;
  }

  public void setRandomFrame() {
    elapsedTime = animation.getFrameDuration() * MathUtils.random(60);
  }

  public Animation<TextureRegion> loadAnimationFromTextureRegions(
      Array<? extends TextureRegion> textures, float frameDuration, boolean loop, int frameNumber) {
    this.frameNumber = frameNumber;
    elapsedTime = frameNumber * frameDuration;
    return loadAnimationFromTextureRegions(textures, frameDuration, frameNumber, loop);
  }

  public Animation<TextureRegion> loadAnimationFromTextureRegions(
      Array<? extends TextureRegion> textures, float frameDuration, int frameNumber, boolean loop) {

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textures);
    elapsedTime = frameNumber * frameDuration;
    this.frameNumber = frameNumber;
    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

    if (animation == null) setAnimation(anim);

    return anim;
  }

  public Animation<TextureRegion> loadAnimationFromFilesWithRespectToRatio(
      String[] fileNames, float frameDuration, boolean loop) {
    int fileCount = fileNames.length;
    Array<TextureRegion> textureArray = new Array<>();

    for (int n = 0; n < fileCount; n++) {
      String fileName = fileNames[n];
      Texture texture = new Texture(Gdx.files.internal(fileName));
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

      int fixedHeight =
          (int)
              (texture.getWidth()
                  * (((double) Gdx.app.getGraphics().getHeight())
                      / Gdx.app.getGraphics().getWidth()));

      TextureRegion backgroundTextureRegion = null;
      if (fixedHeight <= texture.getHeight()) {
        backgroundTextureRegion =
            new TextureRegion(
                texture,
                texture.getWidth(),
                (int)
                    (texture.getWidth()
                        * (((double) Gdx.app.getGraphics().getHeight())
                            / Gdx.app.getGraphics().getWidth())));
      } else {
        int fixedWidth =
            (int)
                (texture.getHeight()
                    * (((double) Gdx.app.getGraphics().getWidth())
                        / Gdx.app.getGraphics().getHeight()));
        backgroundTextureRegion =
            new TextureRegion(
                texture, (texture.getWidth() - fixedWidth) / 2, 0, fixedWidth, texture.getHeight());
      }

      textureArray.add(backgroundTextureRegion);
    }

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);

    if (animation == null) setAnimation(anim);

    return anim;
  }

  public Animation<TextureRegion> loadAnimationFromTexturesWithRespectToRatio(
      Texture[] textures, float frameDuration, boolean loop) {
    int fileCount = textures.length;
    Array<TextureRegion> textureArray = new Array<>();

    for (int n = 0; n < fileCount; n++) {
      Texture texture = textures[n];
      texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

      int fixedHeight =
          (int)
              (texture.getWidth()
                  * (((double) Gdx.app.getGraphics().getHeight())
                      / Gdx.app.getGraphics().getWidth()));

      TextureRegion backgroundTextureRegion = null;
      if (fixedHeight <= texture.getHeight()) {
        backgroundTextureRegion =
            new TextureRegion(
                texture,
                texture.getWidth(),
                (int)
                    (texture.getWidth()
                        * (((double) Gdx.app.getGraphics().getHeight())
                            / Gdx.app.getGraphics().getWidth())));
      } else {
        int fixedWidth =
            (int)
                (texture.getHeight()
                    * (((double) Gdx.app.getGraphics().getWidth())
                        / Gdx.app.getGraphics().getHeight()));
        backgroundTextureRegion =
            new TextureRegion(
                texture, (texture.getWidth() - fixedWidth) / 2, 0, fixedWidth, texture.getHeight());
      }

      textureArray.add(backgroundTextureRegion);
    }

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);

    if (animation == null) setAnimation(anim);

    return anim;
  }

  /**
   * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
   *
   * @param fileName name of file containing spritesheet
   * @param rows number of rows of images in spritesheet
   * @param cols number of columns of images in spritesheet
   * @param frameDuration how long each frame should be displayed
   * @param loop should the animation loop
   * @return animation created (useful for storing multiple animations)
   */
  // TODO: AssetLoader
  public Animation<TextureRegion> loadAnimationFromSheet(
      String fileName, int rows, int cols, float frameDuration, boolean loop) {
    Texture texture = new Texture(Gdx.files.internal(fileName), true);
    texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    int frameWidth = texture.getWidth() / cols;
    int frameHeight = texture.getHeight() / rows;

    TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

    Array<TextureRegion> textureArray = new Array<>();

    for (int r = 0; r < rows; r++) for (int c = 0; c < cols; c++) textureArray.add(temp[r][c]);

    Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);

    if (loop) anim.setPlayMode(Animation.PlayMode.LOOP);
    else anim.setPlayMode(Animation.PlayMode.NORMAL);

    if (animation == null) setAnimation(anim);

    return anim;
  }

  @Deprecated
  public Animation<TextureRegion> loadTexture(String fileName) {
    String[] fileNames = new String[1];
    fileNames[0] = fileName;
    return loadAnimationFromFiles(fileNames, 1, true);
  }

  public Animation<TextureRegion> loadTexture(Texture texture) {
    Texture[] textures = new Texture[1];
    textures[0] = texture;
    return loadAnimationFromTextures(textures, 1, true);
  }

  public Animation<TextureRegion> loadTextureRegion(TextureRegion texture) {
    Array<TextureRegion> tr = new Array<>();
    tr.add(texture);
    return loadAnimationFromTextureRegions(tr, 1, true);
  }

  public Animation<TextureRegion> loadTextureWithRespectToRatio(String fileName) {
    String[] fileNames = new String[1];
    fileNames[0] = fileName;
    return loadAnimationFromFilesWithRespectToRatio(fileNames, 1, true);
  }

  public Animation<TextureRegion> loadTextureWithRespectToRatio(Texture texture) {
    Texture[] textures = new Texture[1];
    textures[0] = texture;
    return loadAnimationFromTexturesWithRespectToRatio(textures, 1, true);
  }

  public boolean isAnimationPaused() {
    return animationPaused;
  }

  /**
   * Set the pause state of the animation.
   *
   * @param pause true to pause animation, false to resume animation
   */
  public void setAnimationPaused(boolean pause) {
    animationPaused = pause;
  }

  /**
   * Checks if animation is complete: if play mode is normal (not looping) and elapsed time is
   * greater than time corresponding to last frame.
   *
   * @return
   */
  public boolean isAnimationFinished() {
    return animation.isAnimationFinished(elapsedTime);
  }

  /**
   * Sets the opacity of this actor.
   *
   * @param opacity value from 0 (transparent) to 1 (opaque)
   */
  public void setOpacity(float opacity) {
    this.getColor().a = opacity;
  }

  /**
   * Set rectangular-shaped collision polygon. This method is automatically called when animation is
   * set, provided that the current boundary polygon is null.
   *
   * @see #setAnimation
   */
  public void setBoundaryRectangle() {
    float w = getWidth();
    float h = getHeight();

    float[] vertices = {0, 0, w, 0, w, h, 0, h};
    boundaryPolygon = new Polygon(vertices);
  }

  /**
   * Returns bounding polygon for this BaseActor, adjusted by Actor's current position and rotation.
   *
   * @return bounding polygon for this BaseActor
   */
  public Polygon getBoundaryPolygon() {
    boundaryPolygon.setPosition(getX(), getY());
    boundaryPolygon.setOrigin(getOriginX(), getOriginY());
    boundaryPolygon.setRotation(getRotation());
    boundaryPolygon.setScale(getScaleX(), getScaleY());
    return boundaryPolygon;
  }

  /**
   * Replace default (rectangle) collision polygon with an n-sided polygon. <br>
   * Vertices of polygon lie on the ellipse contained within bounding rectangle. Note: one vertex
   * will be located at point (0,width); a 4-sided polygon will appear in the orientation of a
   * diamond.
   *
   * @param numSides number of sides of the collision polygon
   */
  public void setBoundaryPolygon(int numSides) {
    float w = getWidth();
    float h = getHeight();

    float[] vertices = new float[2 * numSides];
    for (int i = 0; i < numSides; i++) {
      float angle = i * 6.28f / numSides;
      // x-coordinate
      vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
      // y-coordinate
      vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
    }
    boundaryPolygon = new Polygon(vertices);
  }

  // ----------------------------------------------
  // Actor methods: act and draw
  // ----------------------------------------------

  /**
   * Processes all Actions and related code for this object; automatically called by act method in
   * Stage class.
   *
   * @param dt elapsed time (second) since last frame (supplied by Stage act method)
   */
  @Override
  public void act(float dt) {
    super.act(dt);
    if (!animationPaused) elapsedTime += dt;
  }

  public Animation<TextureRegion> loadAnimationFromTextureRegions(
      Array<? extends TextureRegion> textures, float frameDuration, PlayMode playMode) {
    Animation<TextureRegion> anim = new Animation<>(frameDuration, textures);
    anim.setPlayMode(playMode);
    setAnimation(anim);
    return anim;
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {

    // apply color tint effect
    Color c = getColor();
    batch.setColor(c.r, c.g, c.b, Math.min(c.a, parentAlpha));

    if (animation != null && isVisible() && isAnimationVisible)
      batch.draw(
          animation.getKeyFrame(elapsedTime),
          getX() + drawPad,
          getY() + drawPad,
          getOriginX(),
          getOriginY(),
          getWidth() - 2 * drawPad,
          getHeight() - 2 * drawPad,
          getScaleX(),
          getScaleY(),
          getRotation());

    super.draw(batch, parentAlpha);
  }

  protected void poolReset() {
    elapsedTime = 0;
    frameNumber = 0;
    isAnimationVisible = true;
  }

  public void resetAnimation() {
    elapsedTime = 0;
    frameNumber = 0;
  }
}
