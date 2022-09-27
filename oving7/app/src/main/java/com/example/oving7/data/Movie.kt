package com.example.oving7.data

data class Movie(
    val title:String,
    val director:String,
    val actors: Array<String>)
{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (title != other.title) return false
        if (director != other.director) return false
        if (!actors.contentEquals(other.actors)) return false
        return true
    }

    override fun hashCode(): Int {
        var res = title.hashCode()
        res = 31 * res + director.hashCode()
        res = 31 * res + actors.contentHashCode()
        return res
    }

    override fun toString(): String {
        return "Movie(title='$title', director='$director', actors=${actors.contentToString()})\n"
    }
}
