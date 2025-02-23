package com.parabolagames.glassmaze.framework;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Extends functionality of the LibGDX Actor class. by adding support for textures/animation,
 * collision polygons, movement, world boundaries, and camera scrolling. Most game objects should
 * extend this class; lists of extensions can be retrieved by stage and class name.
 *
 * @see #Actor
 * @author Lee Stemkoski
 */
public class BaseActor extends TableActor {

  private Vector2 velocityVec;
  private Vector2 accelerationVec;
  private float acceleration;
  private float maxSpeed;
  private float deceleration;

  //	// stores size of game world for all actors
  //	private static Rectangle worldBounds;

  public BaseActor(float x, float y) {
    // call constructor from Actor class
    super();

    // perform additional initialization tasks
    setPosition(x, y);

    // initialize animation data
    animation = null;
    elapsedTime = 0;
    animationPaused = false;

    // initialize physics data
    velocityVec = new Vector2(0, 0);
    accelerationVec = new Vector2(0, 0);
    acceleration = 0;
    maxSpeed = 1000;
    deceleration = 0;

    boundaryPolygon = null;
  }

  //	/**
  //	 * If this object moves completely past the world bounds, adjust its position to
  //	 * the opposite side of the world.
  //	 */
  //	public void wrapAroundWorld() {
  //		if (getX() + getWidth() < 0)
  //			setX(worldBounds.width);
  //
  //		if (getX() > worldBounds.width)
  //			setX(-getWidth());
  //
  //		if (getY() + getHeight() < 0)
  //			setY(worldBounds.height);
  //
  //		if (getY() > worldBounds.height)
  //			setY(-getHeight());
  //	}

  /**
   * Align center of actor at given position coordinates.
   *
   * @param x x-coordinate to center at
   * @param y y-coordinate to center at
   */
  public void centerAtPosition(float x, float y) {
    setPosition(x - getWidth() / 2, y - getHeight() / 2);
  }

  /**
   * Repositions this BaseActor so its center is aligned with center of other BaseActor. Useful when
   * one BaseActor spawns another.
   *
   * @param other BaseActor to align this BaseActor with
   */
  public void centerAtActor(BaseActor other) {
    centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
  }

  // ----------------------------------------------
  // physics/motion methods
  // ----------------------------------------------

  /**
   * Set acceleration of this object.
   *
   * @param acc Acceleration in (pixels/second) per second.
   */
  public void setAcceleration(float acc) {
    acceleration = acc;
  }

  /**
   * Set deceleration of this object. Deceleration is only applied when object is not accelerating.
   *
   * @param dec Deceleration in (pixels/second) per second.
   */
  public void setDeceleration(float dec) {
    deceleration = dec;
  }

  /**
   * Set maximum speed of this object.
   *
   * @param ms Maximum speed of this object in (pixels/second).
   */
  public void setMaxSpeed(float ms) {
    maxSpeed = ms;
  }

  /**
   * Calculates the speed of movement (in pixels/second).
   *
   * @return speed of movement (pixels/second)
   */
  public float getSpeed() {
    return velocityVec.len();
  }

  /**
   * Set the speed of movement (in pixels/second) in current direction. If current speed is zero
   * (direction is undefined), direction will be set to 0 degrees.
   *
   * @param speed of movement (pixels/second)
   */
  public void setSpeed(float speed) {
    // if length is zero, then assume motion angle is zero degrees
    if (velocityVec.len() == 0) velocityVec.set(speed, 0);
    else velocityVec.setLength(speed);
  }

  /**
   * Determines if this object is moving (if speed is greater than zero).
   *
   * @return false when speed is zero, true otherwise
   */
  public boolean isMoving() {
    return (getSpeed() > 0);
  }

  /**
   * Get the angle of motion (in degrees), calculated from the velocity vector. <br>
   * To align actor image angle with motion angle, use <code>setRotation( getMotionAngle() )</code>.
   *
   * @return angle of motion (degrees)
   */
  public float getMotionAngle() {
    return velocityVec.angle();
  }

  /**
   * Sets the angle of motion (in degrees). If current speed is zero, this will have no effect.
   *
   * @param angle of motion (degrees)
   */
  public void setMotionAngle(float angle) {
    velocityVec.setAngle(angle);
  }

  /**
   * Update accelerate vector by angle and value stored in acceleration field. Acceleration is
   * applied by <code>applyPhysics</code> method.
   *
   * @param angle Angle (degrees) in which to accelerate.
   * @see #acceleration
   * @see #applyPhysics
   */
  public void accelerateAtAngle(float angle) {
    accelerationVec.add(new Vector2(acceleration, 0).setAngle(angle));
  }

  /**
   * Update accelerate vector by current rotation angle and value stored in acceleration field.
   * Acceleration is applied by <code>applyPhysics</code> method.
   *
   * @see #acceleration
   * @see #applyPhysics
   */
  public void accelerateForward() {
    accelerateAtAngle(getRotation());
  }

  /**
   * Adjust velocity vector based on acceleration vector, then adjust position based on velocity
   * vector. <br>
   * If not accelerating, deceleration value is applied. <br>
   * Speed is limited by maxSpeed value. <br>
   * Acceleration vector reset to (0,0) at end of method. <br>
   *
   * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act
   *     </code> method.
   * @see #acceleration
   * @see #deceleration
   * @see #maxSpeed
   */
  public void applyPhysics(float dt) {
    // apply acceleration
    velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);

    float speed = getSpeed();

    // decrease speed (decelerate) when not accelerating
    if (accelerationVec.len() == 0) speed -= deceleration * dt;

    // keep speed within set bounds
    speed = MathUtils.clamp(speed, 0, maxSpeed);

    // update velocity
    setSpeed(speed);

    // update position according to value stored in velocity vector
    moveBy(velocityVec.x * dt, velocityVec.y * dt);

    // reset acceleration
    accelerationVec.set(0, 0);
  }

  // ----------------------------------------------
  // Collision polygon methods
  // ----------------------------------------------

  /**
   * Determine if this BaseActor overlaps other BaseActor (according to collision polygons).
   *
   * @param other BaseActor to check for overlap
   * @return true if collision polygons of this and other BaseActor overlap
   * @see #setBoundaryRectangle
   * @see #setBoundaryPolygon
   */
  public boolean overlaps(BaseActor other) {
    Polygon poly1 = this.getBoundaryPolygon();
    Polygon poly2 = other.getBoundaryPolygon();

    // initial test to improve performance
    if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) return false;

    return Intersector.overlapConvexPolygons(poly1, poly2);
  }

  /**
   * Implement a "solid"-like behavior: when there is overlap, move this BaseActor away from other
   * BaseActor along minimum translation vector until there is no overlap.
   *
   * @param other BaseActor to check for overlap
   * @return direction vector by which actor was translated, null if no overlap
   */
  public Vector2 preventOverlap(BaseActor other) {
    Polygon poly1 = this.getBoundaryPolygon();
    Polygon poly2 = other.getBoundaryPolygon();

    // initial test to improve performance
    if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) return null;

    MinimumTranslationVector mtv = new MinimumTranslationVector();
    boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);

    if (!polygonOverlap) return null;

    this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
    return mtv.normal;
  }

  /**
   * Determine if this BaseActor is near other BaseActor (according to collision polygons).
   *
   * @param distance amount (pixels) by which to enlarge collision polygon width and height
   * @param other BaseActor to check if nearby
   * @return true if collision polygons of this (enlarged) and other BaseActor overlap
   * @see #setBoundaryRectangle
   * @see #setBoundaryPolygon
   */
  public boolean isWithinDistance(float distance, BaseActor other) {
    Polygon poly1 = this.getBoundaryPolygon();
    float scaleX = (this.getWidth() + 2 * distance) / this.getWidth();
    float scaleY = (this.getHeight() + 2 * distance) / this.getHeight();
    poly1.setScale(scaleX, scaleY);

    Polygon poly2 = other.getBoundaryPolygon();

    // initial test to improve performance
    if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle())) return false;

    return Intersector.overlapConvexPolygons(poly1, poly2);
  }

  //	/**
  //	 * Set world dimensions for use by methods boundToWorld() and scrollTo().
  //	 *
  //	 * @param width  width of world
  //	 * @param height height of world
  //	 */
  //	public static void setWorldBounds(float width, float height) {
  //		worldBounds = new Rectangle(0, 0, width, height);
  //	}

  //	/**
  //	 * Set world dimensions for use by methods boundToWorld() and scrollTo().
  //	 *
  //	 * @param BaseActor whose size determines the world bounds (typically a
  //	 *                  background image)
  //	 */
  //	public static void setWorldBounds(BaseActor ba) {
  //		setWorldBounds(ba.getWidth(), ba.getHeight());
  //	}
  //
  //	/**
  //	 * Get world dimensions
  //	 *
  //	 * @return Rectangle whose width/height represent world bounds
  //	 */
  //	public static Rectangle getWorldBounds() {
  //		return worldBounds;
  //	}
  //
  //	/**
  //	 * If an edge of an object moves past the world bounds, adjust its position to
  //	 * keep it completely on screen.
  //	 */
  //	public void boundToWorld() {
  //		if (getX() < 0)
  //			setX(0);
  //		if (getX() + getWidth() > worldBounds.width)
  //			setX(worldBounds.width - getWidth());
  //		if (getY() < 0)
  //			setY(0);
  //		if (getY() + getHeight() > worldBounds.height)
  //			setY(worldBounds.height - getHeight());
  //	}
  //
  //	/**
  //	 * Center camera on this object, while keeping camera's range of view
  //	 * (determined by screen size) completely within world bounds.
  //	 */
  //	public void alignCamera() {
  //		Camera cam = this.getStage().getCamera();
  //
  //		// center camera on actor
  //		cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);
  //
  //		// bound camera to layout
  //		cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2,
  //				worldBounds.width - cam.viewportWidth / 2);
  //		cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2,
  //				worldBounds.height - cam.viewportHeight / 2);
  //		cam.update();
  //	}
}
