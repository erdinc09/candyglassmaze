package com.parabolagames.glassmaze.shared.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.parabolagames.glassmaze.shared.Assets


class GM_HyperTextLabel(text: CharSequence, assets: Assets, fontPath: String, fontScale: Float, private val lineColor: Color, url: String)
    : Label(text, LabelStyle(assets.getBitmapFont(fontPath), null)) {

    private val start: Vector2 = Vector2()
    private val end: Vector2 = Vector2()

    init {
        setFontScale(fontScale)

        addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Gdx.net.openURI(url)
            }
        })
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        super.draw(batch, parentAlpha)
        batch.end()
        shapeRenderer.color = lineColor
        shapeRenderer.projectionMatrix = stage.camera.combined
        start.x = 0f
        start.y = height * 0.1f
        localToStageCoordinates(start)
        end.x = width
        end.y = height * 0.1f
        localToStageCoordinates(end)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.rectLine(
                start.x,
                start.y,
                end.x,
                end.y,
                height * 0.05f)
        shapeRenderer.end()
        Gdx.gl.glLineWidth(1f)
        batch.begin()
    }

    companion object {
        private val shapeRenderer = ShapeRenderer()
    }
}