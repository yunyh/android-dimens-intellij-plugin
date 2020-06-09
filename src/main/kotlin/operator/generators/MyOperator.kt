package operator.generators

abstract class MyOperator<T : Any> {
    abstract fun initialize(doing: T)
}