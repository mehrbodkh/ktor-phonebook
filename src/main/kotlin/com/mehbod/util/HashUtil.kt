package com.mehbod.util

import at.favre.lib.crypto.bcrypt.BCrypt

fun hash(input: String): String = BCrypt.withDefaults().hashToString(12, input.toCharArray())

fun verify(input: String, hash: String): Boolean =
    BCrypt.verifyer().verify(input.toCharArray(), hash.toCharArray()).verified