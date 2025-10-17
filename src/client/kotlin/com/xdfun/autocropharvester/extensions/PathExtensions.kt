package com.xdfun.autocropharvester.extensions

import java.io.File
import java.nio.file.Path
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries

fun Path.findFiles(fileName: String = "*", extensions: Iterable<String>): List<File> {
    if (!isDirectory()) {
        throw IllegalStateException("$this is not a directory")
    }

    var extensionPattern = "{"

    if (extensions.any()) {
        extensionPattern += extensions.joinToString(separator = ",") { it }
        extensionPattern += "}"
    } else {
        extensionPattern = "*"
    }

    return listDirectoryEntries("$fileName.$extensionPattern").map { it.toFile() }
}

fun Path.findFiles(fileName: String = "*", vararg extensions: String): List<File> {
    return findFiles(fileName, extensions.asIterable())
}