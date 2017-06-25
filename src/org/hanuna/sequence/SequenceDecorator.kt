package org.hanuna.sequence

abstract class SequenceDecorator<oldValue : Any, oldId : Any, out newValue : Any, out newId : Any>(
        protected val delegate: KtlnSequence<oldValue, oldId>
) : KtlnSequence<newValue, newId> {
    override val lineNumber get() = delegate.lineNumber
    override val positionInLine get() = delegate.positionInLine
    override val tokenIndex get() = delegate.tokenIndex
    override fun advance() = delegate.advance()
    override fun fallback(prevTokenIndex: Int) = delegate.fallback(prevTokenIndex)

    override val currentValue: newValue?
        get() {
            checkConsistency()
            if (delegate.currentValue == null) return null
            return transformValue(delegate.currentId!!, delegate.currentValue!!)
        }
    override val currentId: newId?
        get() {
            checkConsistency()
            if (delegate.currentId == null) return null
            return transformId(delegate.currentId!!, delegate.currentValue!!)
        }

    protected abstract fun transformValue(oldId: oldId, oldValue: oldValue): newValue
    protected abstract fun transformId(oldId: oldId, oldValue: oldValue): newId

    private fun checkConsistency() {
        assert((delegate.currentId == null) == (delegate.currentValue == null)) {
            "delegateId = ${delegate.currentId}, delegateValue = ${delegate.currentValue}"
        }
    }
}

abstract class SequenceIdDecorator<value: Any, oldId : Any, newId: Any>(
        delegate: KtlnSequence<value, oldId>
) : SequenceDecorator<value, oldId, value, newId>(delegate) {
    override fun transformValue(oldId: oldId, oldValue: value) = oldValue
}