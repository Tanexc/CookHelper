package ru.tanec.cookhelper.core.utils


fun <T> List<T>.partOfDiv(part: Int, div: Int): List<T> {
    return if ((div * (part + 1) > this.size) and (div * part < this.size)) {
        this.subList(div * part, this.size)
    } else if (div * (part + 1) <= this.size) {
        this.subList(div * part, div * (part + 1))
    } else {
        listOf()
    }

}