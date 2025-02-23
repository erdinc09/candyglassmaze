package com.parabolagames.glassmaze.candymode

import com.badlogic.gdx.physics.box2d.*
import com.parabolagames.glassmaze.shared.ActorType
import com.parabolagames.glassmaze.shared.ActorTypeData
import com.parabolagames.glassmaze.shared.IBreakable

internal class ContactController : ContactListener {
    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB

        // glass & glass interaction disabled
        if (areActorsGlassAndGlass(fixtureA, fixtureB)) {
            contact.isEnabled = false
        }

        // candy interaction with anything other than wall disabled
        if ((isGivenType(fixtureB, ActorType.CANDY)
                        && !isGivenType(fixtureA, ActorType.WALL))
                || (isGivenType(fixtureA, ActorType.CANDY)
                        && !isGivenType(fixtureB, ActorType.WALL))) {
            contact.isEnabled = false
        }

        // candies and balls interaction disabled
        if (areActorsCandyAndBall(fixtureA, fixtureB)) {
            contact.isEnabled = false
        }

        // balls break glasses other than the spiny
        if (isGivenType(fixtureB, ActorType.GLASS_BALL)
                && isGivenType(fixtureA, ActorType.GLASS)) {
            ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            if ((fixtureA.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(false)
            }
            contact.isEnabled = false
        }
        if (isGivenType(fixtureB, ActorType.IRON_BALL)
                && isGivenType(fixtureA, ActorType.GLASS)) {
            ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            contact.isEnabled = false
            ((fixtureB.body.userData as ActorTypeData).data as ICrackListener)
                    .crackedByMe(fixtureA.body.position, ActorType.GLASS)
        }
        if (isGivenType(fixtureA, ActorType.IRON_BALL)
                && isGivenType(fixtureB, ActorType.GLASS)) {
            ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            contact.isEnabled = false
            ((fixtureA.body.userData as ActorTypeData).data as ICrackListener)
                    .crackedByMe(fixtureB.body.position, ActorType.GLASS)
        }
        if (isGivenType(fixtureA, ActorType.GLASS_BALL)
                && isGivenType(fixtureB, ActorType.GLASS)) {
            ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            if ((fixtureA.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(false)
            }
            contact.isEnabled = false
        }

        // iron glass breaks the spiny glass
        if (isGivenType(fixtureB, ActorType.IRON_BALL)
                && isGivenType(fixtureA, ActorType.SPINY_GLASS)) {
            ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            contact.isEnabled = false
            ((fixtureB.body.userData as ActorTypeData).data as ICrackListener)
                    .crackedByMe(fixtureA.body.position, ActorType.SPINY_GLASS)
        }
        if (isGivenType(fixtureA, ActorType.IRON_BALL)
                && isGivenType(fixtureB, ActorType.SPINY_GLASS)) {
            ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                    .markForBreak(true)
            contact.isEnabled = false
            ((fixtureA.body.userData as ActorTypeData).data as ICrackListener)
                    .crackedByMe(fixtureB.body.position, ActorType.SPINY_GLASS)
        }

        // glass balls break themselves when interacted with spiny
        if (isGivenType(fixtureB, ActorType.GLASS_BALL)
                && isGivenType(fixtureA, ActorType.SPINY_GLASS)) {
            if ((fixtureB.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(true)
            }
            contact.isEnabled = false
        }
        if (isGivenType(fixtureA, ActorType.GLASS_BALL)
                && isGivenType(fixtureB, ActorType.SPINY_GLASS)) {
            if ((fixtureA.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(true)
            }
            contact.isEnabled = false
        }

        // balls break themselves
        if ((isGivenType(fixtureA, ActorType.GLASS_BALL)
                        || isGivenType(fixtureA, ActorType.IRON_BALL))
                && (isGivenType(fixtureB, ActorType.GLASS_BALL)
                        || isGivenType(fixtureB, ActorType.IRON_BALL))) {
            var soundWillBePlayed = false
            if ((fixtureA.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureA.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(true)
                soundWillBePlayed = true
                contact.isEnabled = false
            }
            if ((fixtureB.body.userData as ActorTypeData).data is IBreakable) {
                ((fixtureB.body.userData as ActorTypeData).data as IBreakable)
                        .markForBreak(!soundWillBePlayed)
                contact.isEnabled = false
            }
        }
        if ((isGivenType(fixtureA, ActorType.IRON_BALL)
                        && isGivenType(fixtureB, ActorType.WALL))
                || (isGivenType(fixtureB, ActorType.IRON_BALL)
                        && isGivenType(fixtureA, ActorType.WALL))) {
            contact.isEnabled = false
        }
        if ((isGivenType(fixtureA, ActorType.GLASS_BALL)
                        && isGivenType(fixtureB, ActorType.WALL))
                || (isGivenType(fixtureB, ActorType.GLASS_BALL)
                        && isGivenType(fixtureA, ActorType.WALL))) {
            contact.isEnabled = false
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {}
    override fun endContact(contact: Contact) {}
    override fun beginContact(contact: Contact) {}


    private fun areActorsGlassAndGlass(fixtureA: Fixture, fixtureB: Fixture): Boolean =
            isGivenType(fixtureA, ActorType.GLASS) && isGivenType(fixtureB, ActorType.GLASS)


    private fun areActorsCandyAndBall(fixtureA: Fixture, fixtureB: Fixture): Boolean =
            (isGivenType(fixtureA, ActorType.CANDY)
                    && (isGivenType(fixtureB, ActorType.GLASS_BALL)
                    || isGivenType(fixtureB, ActorType.IRON_BALL))
                    || isGivenType(fixtureB, ActorType.CANDY)
                    && (isGivenType(fixtureA, ActorType.GLASS_BALL)
                    || isGivenType(fixtureA, ActorType.IRON_BALL)))


    private fun isGivenType(fixture: Fixture, type: ActorType): Boolean = (fixture.body.userData as ActorTypeData).type === type

}