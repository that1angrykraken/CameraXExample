package hopper.prototype.camerax.until

fun Any.hasSameTypeAs(other: Any): Boolean {
    return this::class == other::class
}
