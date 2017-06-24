package org.hanuna

interface Dispatcheble {
    fun <R> dispatch(dispatcher: Dispatcher<R>): R
}

class A : Dispatcheble {
    override fun <R> dispatch(dispatcher: Dispatcher<R>) = dispatcher.toA(this)
}

class B

class C

interface Dispatcher<R> {
    fun toA(a: A): R
}


interface Node {
    val parent: Node?

    val children: Iterable<Node>
}