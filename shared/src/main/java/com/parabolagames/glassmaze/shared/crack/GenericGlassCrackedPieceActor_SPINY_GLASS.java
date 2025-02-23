package com.parabolagames.glassmaze.shared.crack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.google.common.base.Preconditions;

import com.parabolagames.glassmaze.framework.TableActor;
import com.parabolagames.glassmaze.shared.Assets;
import com.parabolagames.glassmaze.shared.GenericGlassBallCrackedPieceActorData;

// 1 asagi 2 yukari
// 1 2
// 1 	2_1 2_2
// 1_1 1_2 	2
// 1_1 1_2 2_1 2_2
@Deprecated
abstract class GenericGlassCrackedPieceActor_SPINY_GLASS extends TableActor
    implements Pool.Poolable {
  public static final float DEFAULT_FADE_OUT_TIME =
      com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor_GLASS_2.DEFAULT_FADE_OUT_TIME;
  public static final float DEFAULT_DELAY_TIME =
      com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor_GLASS_2.DEFAULT_DELAY_TIME;
  static final int MAX_CRACK_POOL_INSTANCE_COUNT =
      com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor_GLASS_2.MAX_CRACK_POOL_INSTANCE_COUNT;

  public static final Pool<GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_1>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_1.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_1>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_1.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_2>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_2.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_2>
      GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_2.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  public static final Pool<GenericGlassCrackedPieceActor_SPINY_GLASS.RandomPieceActor>
      RANDOM_PIECE_ACTOR_POOL =
          Pools.get(
              GenericGlassCrackedPieceActor_SPINY_GLASS.RandomPieceActor.class,
              MAX_CRACK_POOL_INSTANCE_COUNT);
  static final float CLIMB_DURATION = com.parabolagames.glassmaze.shared.crack.GenericGlassCrackedPieceActor_GLASS_2.CLIMB_DURATION;

  static int crackTurn = 0;

  private GenericGlassCrackedPieceActor_SPINY_GLASS() {}

  public static void createAllPiecesWithSplit(
      float x,
      float y,
      Assets assets,
      float sizeInMeter,
      GenericGlassBallCrackedPieceActorData data,
      float fadeOutTime,
      float delayTime,
      Stage stage) {

    Preconditions.checkArgument(data == GenericGlassBallCrackedPieceActorData.SPINY_GLASS);

    switch (crackTurn) {
      case 0: // 1 2
        {
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1 actor1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.obtain();
          actor1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2 actor2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.obtain();
          actor2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          stage.addActor(actor1);
          stage.addActor(actor2);
        }
        break;
      case 1: // 1 	2_1 2_2
        {
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1 actor1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.obtain();
          actor1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_1 actor2_1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.obtain();
          actor2_1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_2 actor2_2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.obtain();
          actor2_2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          stage.addActor(actor1);
          stage.addActor(actor2_1);
          stage.addActor(actor2_2);
        }
        break;
      case 2: // 1_1 1_2 	2
        {
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_1 actor1_1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.obtain();
          actor1_1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_2 actor1_2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.obtain();
          actor1_2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2 actor2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.obtain();
          actor2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          stage.addActor(actor1_1);
          stage.addActor(actor1_2);
          stage.addActor(actor2);
        }
        break;
      case 3: // 1_1 1_2 2_1 2_2
        {
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_1 actor1_1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.obtain();
          actor1_1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor1_2 actor1_2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.obtain();
          actor1_2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_1 actor2_1 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.obtain();
          actor2_1.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          GenericGlassCrackedPieceActor_SPINY_GLASS.GenericGlassCrackedPieceActor2_2 actor2_2 =
              GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.obtain();
          actor2_2.init(sizeInMeter, sizeInMeter, x, y, assets, data, fadeOutTime, delayTime);
          stage.addActor(actor1_1);
          stage.addActor(actor1_2);
          stage.addActor(actor2_1);
          stage.addActor(actor2_2);
        }
        break;
      default:
        throw new IllegalStateException();
    }

    if (data.getCrack1Atlas() != null) {
      for (int i = MathUtils.random(5, 11); i > 0; i--) {
        float deltaX = MathUtils.random(-sizeInMeter / 4, sizeInMeter / 4);
        float deltaY = MathUtils.random(-sizeInMeter / 4, sizeInMeter / 4);

        GenericGlassCrackedPieceActor_SPINY_GLASS.RandomPieceActor piece =
            RANDOM_PIECE_ACTOR_POOL.obtain();
        piece.init(
            sizeInMeter / MathUtils.random(1.2f, 9),
            sizeInMeter / MathUtils.random(1.2f, 9),
            x + sizeInMeter / 2 + deltaX,
            y + sizeInMeter / 2 + deltaY,
            assets,
            data,
            -1,
            -1);
        stage.addActor(piece);

        float x_XV = MathUtils.random(sizeInMeter / 2, 3 * sizeInMeter / 2); // 0.15f, 0.45f
        piece.addAction(Actions.moveBy(deltaX < 0 ? -x_XV : x_XV, 0, x_XV, Interpolation.fastSlow));
      }
    }
    crackTurn = (crackTurn + 1) % 4;
  }

  protected final void initialize(
      float width,
      float height,
      float x,
      float y,
      Assets assets,
      String atlasName,
      float fadeOutTime,
      float delayTime,
      float frameDuration) {
    if (animation == null) {
      loadAnimationFromTextureRegions(
          assets.getTexturesFromTextureAtlas(atlasName),
          frameDuration,
          Animation.PlayMode.LOOP_PINGPONG);
    }

    setPosition(x, y);
    setSize(width, height);

    if (fadeOutTime > 0) {
      addAction(Actions.sequence(Actions.delay(delayTime), Actions.fadeOut(fadeOutTime)));
    }
  }

  protected abstract void init(
      float width,
      float height,
      float x,
      float y,
      Assets assets,
      GenericGlassBallCrackedPieceActorData data,
      float fadeOutTime,
      float delayTime);

  @Override
  public void reset() {
    poolReset();
    setColor(getColor().r, getColor().g, getColor().b, 1);
    clearActions();
  }

  private static class GenericGlassCrackedPieceActor1
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor1() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece1Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.sequence(
              Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast), Actions.removeActor()));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor1", "freed");
      }
    }
  }

  private static class GenericGlassCrackedPieceActor1_1
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor1_1() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece1_1Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.sequence(
              Actions.parallel(
                  Actions.moveBy(width / 3, 0f, 0.2f),
                  Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast)),
              Actions.removeActor()));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_1_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor1_1", "freed");
      }
    }
  }

  private static class GenericGlassCrackedPieceActor1_2
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor1_2() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece1_2Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.sequence(
              Actions.parallel(
                  Actions.moveBy(-width / 3, 0f, 0.2f),
                  Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast)),
              Actions.removeActor()));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_1_2_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor1_2", "freed");
      }
    }
  }

  private static class GenericGlassCrackedPieceActor2
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor2() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece2Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.sequence(
              Actions.moveBy(0, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
              Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast),
              Actions.removeActor()));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor2", "freed");
      }
    }
  }

  private static class GenericGlassCrackedPieceActor2_1
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor2_1() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece2_1Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.parallel(
              Actions.moveBy(width / 3, 0f, 0.3f),
              Actions.sequence(
                  Actions.moveBy(0, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
                  Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast),
                  Actions.removeActor())));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_1_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor2_1", "freed");
      }
    }
  }

  private static class GenericGlassCrackedPieceActor2_2
      extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private GenericGlassCrackedPieceActor2_2() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width, height, x, y, assets, data.getPiece2_2Atlas(), fadeOutTime, delayTime, 0.04f);
      addAction(
          Actions.parallel(
              Actions.moveBy(-width / 3, 0f, 0.3f),
              Actions.sequence(
                  Actions.moveBy(0, width * 1.33f, CLIMB_DURATION, Interpolation.fastSlow),
                  Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast),
                  Actions.removeActor())));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        GENERIC_GLASS_CRACKED_PIECE_ACTOR_2_2_POOL.free(this);
        Gdx.app.debug("GenericGlassCrackedPieceActor2_2", "freed");
      }
    }
  }

  private static class RandomPieceActor extends GenericGlassCrackedPieceActor_SPINY_GLASS {
    private RandomPieceActor() {}

    @Override
    protected void init(
        float width,
        float height,
        float x,
        float y,
        Assets assets,
        GenericGlassBallCrackedPieceActorData data,
        float fadeOutTime,
        float delayTime) {
      super.initialize(
          width,
          height,
          x,
          y,
          assets,
          data.getCrack1Atlas(),
          data.getCrackFadeOutTime(),
          data.getCrackDelayTime(),
          0.02f);
      addAction(
          Actions.sequence(
              Actions.moveBy(0, -3, 1.3f, Interpolation.slowFast), Actions.removeActor()));
    }

    @Override
    protected void setParent(Group parent) {
      super.setParent(parent);
      if (parent == null) {
        RANDOM_PIECE_ACTOR_POOL.free(this);
        Gdx.app.debug("RandomPieceActor", "freed");
      }
    }
  }
}
