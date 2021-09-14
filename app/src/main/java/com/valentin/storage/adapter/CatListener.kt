package com.valentin.storage.adapter

import com.valentin.storage.models.Cat

interface CatListener {
    fun deleteCat(cat: Cat)
    fun updateCat(cat: Cat)
}