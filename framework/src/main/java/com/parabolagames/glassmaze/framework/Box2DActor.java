package com.parabolagames.glassmaze.framework;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.google.common.base.Preconditions;

public class Box2DActor extends TableActor {

    // body definition - used to initialize body
    // body definition data: position, angle,
    // linearVelocity, angularVelocity,
    // type (static, dynamic),
    // fixedRotation (can this object rotate?)

    protected BodyDef bodyDef;

    protected Body body;

    private World world;

    protected World getBaseWorld() {
        return world;
    }

    protected boolean phyisicsInitilized = false;
    protected boolean shapeInitilized = false;
    protected boolean seperatedFromBox2d = false;

    // fixture definition - used to initialize fixture
    // fixture data: shape, density, friction, restituion (0 to 1)
    // *** weight is calculated via density*area

    // fixture - attached to body

    protected FixtureDef fixtureDef;

    public Box2DActor() {
        body = null;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
    }

    public void destroyBody() {
        world.destroyBody(body);
        // Gdx.app.debug("PhysicsEditorActor", String.format("World{%s} body size=%d  ", world,
        // world.getBodyCount()));
    }

    public Object getBodyUserData() {
        return body.getUserData();
    }

    public void setBodyUserData(Object data) {
        Preconditions.checkState(phyisicsInitilized);
        body.setUserData(data);
    }

    public void separateFromBox2d() {
        destroyBody();
        seperatedFromBox2d = true;
    }

    @Override
    public void act(float dt) {
        if (!seperatedFromBox2d) {
            Vector2 center = body.getWorldCenter();
            super.setPosition(center.x - getOriginX(), center.y - getOriginY());
            super.setRotation((float) Math.toDegrees(body.getAngle())); // convert to degrees
        }
        super.act(dt);
    }

    @Override
    public void setPosition(float x, float y) {
        Preconditions.checkState(!phyisicsInitilized);
        super.setPosition(x, y);
    }

    @Override
    public void moveBy(float x, float y) {
        Preconditions.checkState(!phyisicsInitilized);
        super.moveBy(x, y);
    }

    @Override
    public void setRotation(float degrees) {
        Preconditions.checkState(!phyisicsInitilized);
        super.setRotation(degrees);
    }

    @Override
    public void rotateBy(float amountInDegrees) {
        Preconditions.checkState(!phyisicsInitilized);
        super.rotateBy(amountInDegrees);
    }

    @Override
    public void setPosition(float x, float y, int alignment) {
        Preconditions.checkState(!phyisicsInitilized || seperatedFromBox2d);
        super.setPosition(x, y, alignment);
    }

    public void setStatic() {
        bodyDef.type = BodyType.StaticBody;
    }

    public void setDynamic() {
        bodyDef.type = BodyType.DynamicBody;
    }

    public void setKinematic() {
        bodyDef.type = BodyType.KinematicBody;
    }

    public void setFixedRotation() {
        bodyDef.fixedRotation = true;
    }

    // will register overlaps; object is not solid.
    public void setSensor() {
        fixtureDef.isSensor = true;
    }

    public void setBullet() {
        bodyDef.bullet = true;
    }

    public void setGroupIndex(short index) {
        fixtureDef.filter.groupIndex = index;
    }

    public void setShapeCircle() {
        Preconditions.checkState(!shapeInitilized);
        setOriginCenter();
        //		// position must be centered
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
        // resetShapeCirclePosition();
        // position must be centered
        // bodyDef.position.set(getX(), getY());
        CircleShape circ = new CircleShape();
        circ.setRadius(getWidth() / 2);
        fixtureDef.shape = circ;
        // circ.dispose();
        shapeInitilized = true;
    }

    public void resetShapeCirclePosition() {
        setOriginCenter();
        bodyDef.position.set(getX() + getWidth() / 2, getY() + getHeight() / 2);
        fixtureDef.shape.setRadius(getWidth() / 2);
        shapeInitilized = true;
    }

    public void setPhysicsProperties(float density, float friction, float restitution) {
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
    }

    // uses data to initialize object and add to world
    public void initializePhysics(World w) {
        Preconditions.checkState(world == null, "physics already initilized");
        Preconditions.checkState(shapeInitilized);
        this.world = w;
        // initialize a body; automatically added to world
        body = w.createBody(bodyDef);

        // initialize a Fixture and attach it to the body
        // don't need to store it?
        Fixture f = body.createFixture(fixtureDef);
        f.setUserData("main");

        // store reference to this, so can access from collision
        // body.setUserData(this);
        phyisicsInitilized = true;
    }

    public void setOriginCenter() {
        if (getWidth() == 0) System.err.println("error: actor size not set");
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public void setShapeRectangle() {
        Preconditions.checkState(!shapeInitilized);
        setOriginCenter();
        // position must be centered
        // resetShapeRectanglePosition();
        // bodyDef.position.set(getX(), getY());
        bodyDef.position.set((getX() + getOriginX()), (getY() + getOriginY()));
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(getWidth() / 2, getHeight() / 2);
        fixtureDef.shape = rect;
        // rect.dispose();
        shapeInitilized = true;
    }

    public void resetShapeRectanglePosition() {
        setOriginCenter();
        bodyDef.position.set((getX() + getOriginX()), (getY() + getOriginY()));
        ((PolygonShape) fixtureDef.shape).setAsBox(getWidth() / 2, getHeight() / 2);
        shapeInitilized = true;
    }

    protected void poolReset() {
        super.poolReset();
        phyisicsInitilized = false;
        shapeInitilized = false;
        world = null;
        body = null;
        seperatedFromBox2d=false;
    }

    // once game is running...
    public void applyForce(float x, float y) {
        body.applyForceToCenter(x, y, true);
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    public float getSpeed() {
        return getVelocity().len();
    }

    public void setVelocity(float vx, float vy) {
        body.setLinearVelocity(vx, vy);
    }
}
