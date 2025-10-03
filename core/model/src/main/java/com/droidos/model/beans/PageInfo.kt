package com.droidos.model.beans

data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)