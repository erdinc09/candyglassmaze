package com.parabolagames.glassmaze.menu

import com.badlogic.gdx.physics.box2d.*
import com.parabolagames.glassmaze.shared.ActorType
import com.parabolagames.glassmaze.shared.ActorTypeData

internal class ContactController : ContactListener {
    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        if (areActorsGlassAndGlass(fixtureA, fixtureB)) {
            contact.isEnabled = false
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {}
    override fun endContact(contact: Contact) {}
    override fun beginContact(contact: Contact) {}

    private fun areActorsGlassAndGlass(fixtureA: Fixture, fixtureB: Fixture): Boolean =
            isGivenType(fixtureA, ActorType.GLASS) && isGivenType(fixtureB, ActorType.GLASS)

    private fun isGivenType(fixture: Fixture, type: ActorType): Boolean = (fixture.body.userData as ActorTypeData).type === type
}