package com.nawrot.mateusz.compass.domain.base


inline fun <A, B> ifNotNull(a: A?, b: B?, function: (A, B) -> Unit) {
    if (a != null && b != null) {
        function(a, b)
    }
}