package fr.uge.android.goodcount

import android.content.Context

class ElementPicker<T>(private val ourMap: Map<T, Int>) {
    companion object{
        fun <T> buildFromResource(context: Context, resourceId: Int, elementParser: (String) -> T): ElementPicker<T>{
            val inuptStream = context.resources.openRawResource(resourceId)
            val lines = inuptStream.bufferedReader().useLines { line ->
                line.map { val elements = it.split(Regex("\\s+"));
                elementParser(elements[0]) to elements[1].toInt()}.toMap()
             }
            return ElementPicker(lines)
        }
    }
    private val ourList: List<T> = ourMap.bagToList()

    fun pickElements(n: Int): List<T>{
        val listCopy : List<T> = ourList.shuffled()
        return listCopy.subList(0, n)
    }
}

fun <T> Map<T, Int>.bagToList() : List<T>{
    var tmpList = mutableListOf<T>()
    this.forEach{ p -> repeat(p.value){
        tmpList.add(p.key)
    }
    }
    return tmpList
}